package com.nusa.hadithapp.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.data_source.local.dao.BookmarkDao
import com.nusa.hadithapp.data.data_source.local.dao.CachedHadithDao
import com.nusa.hadithapp.data.data_source.local.dao.NoteDao
import com.nusa.hadithapp.data.data_source.local.dao.ReadingHistoryDao
import com.nusa.hadithapp.data.data_source.local.entity.BookmarkEntity
import com.nusa.hadithapp.data.data_source.local.entity.NoteEntity
import com.nusa.hadithapp.data.data_source.local.entity.ReadingHistoryEntity
import com.nusa.hadithapp.data.preferences.UserProfilePreferencesRepository
import com.nusa.hadithapp.domain.repository.ReadingHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepo: UserProfilePreferencesRepository,
    private val bookmarkDao: BookmarkDao,
    private val noteDao: NoteDao,
    private val readingHistoryRepo: ReadingHistoryRepository,
    private val readingHistoryDao: ReadingHistoryDao,
    private val cachedHadithDao: CachedHadithDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                userProfileRepo.profileFlow,
                bookmarkDao.getBookmarkCount(),
                noteDao.getNoteCount(),
                readingHistoryRepo.getBooksReadingProgress(),
                readingHistoryRepo.getAllHistory()
            ) { profile, bookmarks, notes, booksProgress, allHistory ->
                val distinctBooks = booksProgress.size
                val totalRead = booksProgress.sumOf { it.readCount }
                val heatmap = com.nusa.hadithapp.util.StudyHeatmapGenerator.generate(allHistory, weeksCount = 16)
                val streak = com.nusa.hadithapp.util.StudyHeatmapGenerator.calculateStreak(allHistory)
                val totalRecent = heatmap.flatMap { it.days }.sumOf { it.count }

                ProfileUiState(
                    userProfile = profile,
                    bookmarkCount = bookmarks,
                    noteCount = notes,
                    totalReadHadithCount = totalRead,
                    distinctBooksCount = distinctBooks,
                    booksProgress = booksProgress,
                    studyHeatmap = heatmap,
                    currentStreakDays = streak,
                    totalRecentReadCount = totalRecent
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun updateProfile(name: String, roleTitle: String) {
        viewModelScope.launch {
            userProfileRepo.updateProfile(name.trim(), roleTitle.trim())
        }
    }

    fun updatePin(pin: String) {
        viewModelScope.launch {
            val enabled = pin.isNotBlank()
            userProfileRepo.updatePin(pin, enabled)
        }
    }

    fun clearCache(onComplete: () -> Unit) {
        viewModelScope.launch {
            cachedHadithDao.clearAllCache()
            onComplete()
        }
    }

    fun exportAllData(context: Context) {
        viewModelScope.launch {
            val bookmarks = bookmarkDao.getAllBookmarks().first()
            val notes = noteDao.getAllNotes().first()
            val history = readingHistoryRepo.getAllHistory().first()
            val profile = userProfileRepo.profileFlow.first()

            val rootJson = JSONObject().apply {
                put("app", "HadithApp")
                put("version", 1)
                put("exportedAt", System.currentTimeMillis())
                put("user", JSONObject().apply {
                    put("name", profile.name)
                    put("role", profile.roleTitle)
                })

                val bookmarksArray = JSONArray()
                bookmarks.forEach { bm ->
                    bookmarksArray.put(JSONObject().apply {
                        put("id", bm.id)
                        put("bookSlug", bm.bookSlug)
                        put("number", bm.number)
                        put("bookName", bm.bookName)
                        put("kitabTitle", bm.kitabTitle)
                        put("babTitle", bm.babTitle)
                        put("arabicText", bm.arabicText)
                        put("translation", bm.translation)
                        put("grade", bm.grade)
                        put("folderTags", bm.foldertags)
                        put("sanadSnippet", bm.sanadSnippet)
                        put("userNote", bm.userNote)
                        put("createdAt", bm.createdAt)
                    })
                }
                put("bookmarks", bookmarksArray)

                val notesArray = JSONArray()
                notes.forEach { note ->
                    notesArray.put(JSONObject().apply {
                        put("id", note.id)
                        put("bookSlug", note.bookSlug)
                        put("number", note.number)
                        put("bookName", note.bookName)
                        put("title", note.title)
                        put("content", note.content)
                        put("categoryTag", note.categoryTag)
                        put("tags", note.tags)
                        put("arabicSnippet", note.arabicSnippet)
                        put("translationSnippet", note.translationSnippet)
                        put("firstSanad", note.firstSanad)
                        put("referencesJson", note.referencesJson)
                        put("isPinned", note.isPinned)
                        put("updatedAt", note.updatedAt)
                    })
                }
                put("notes", notesArray)

                val historyArray = JSONArray()
                history.forEach { h ->
                    historyArray.put(JSONObject().apply {
                        put("bookSlug", h.bookSlug)
                        put("number", h.number)
                        put("bookName", h.bookName)
                        put("kitab", h.kitab)
                        put("chapter", h.chapter)
                        put("arabicSnippet", h.arabicSnippet)
                        put("translationSnippet", h.translationSnippet)
                        put("readAt", h.readAt)
                    })
                }
                put("readingHistory", historyArray)
            }

            val exportText = rootJson.toString(2)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, exportText)
                putExtra(Intent.EXTRA_TITLE, "Cadangan_HadithApp_${System.currentTimeMillis()}.json")
                type = "application/json"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Cadangkan Data Kajian HadithApp"))
        }
    }

    fun restoreDataFromUri(
        context: Context,
        uri: Uri,
        onSuccess: (bookmarkCount: Int, noteCount: Int, historyCount: Int) -> Unit,
        onError: (message: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val jsonString = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        stream.bufferedReader().readText()
                    }
                } ?: throw IllegalArgumentException("Tidak dapat membaca berkas cadangan.")

                val rootJson = JSONObject(jsonString)

                // 1. Pulihkan Profil jika ada
                val userObj = rootJson.optJSONObject("user") ?: rootJson.optJSONObject("profile")
                if (userObj != null) {
                    val name = userObj.optString("name", "")
                    val role = userObj.optString("role", "")
                    if (name.isNotBlank()) {
                        userProfileRepo.updateProfile(name, if (role.isNotBlank()) role else "Thalibul Ilmi")
                    }
                }

                // 2. Pulihkan Bookmark
                var bmRestored = 0
                val bookmarksArray = rootJson.optJSONArray("bookmarks")
                if (bookmarksArray != null) {
                    for (i in 0 until bookmarksArray.length()) {
                        val item = bookmarksArray.optJSONObject(i) ?: continue
                        val bookSlug = item.optString("bookSlug", "")
                        val number = item.optInt("number", 0)
                        val id = item.optString("id", if (bookSlug.isNotBlank() && number > 0) "${bookSlug.lowercase()}_$number" else "")
                        if (id.isNotBlank() && bookSlug.isNotBlank() && number > 0) {
                            val entity = BookmarkEntity(
                                id = id,
                                bookSlug = bookSlug,
                                number = number,
                                bookName = item.optString("bookName", bookSlug),
                                kitabTitle = item.optString("kitabTitle", ""),
                                babTitle = item.optString("babTitle", ""),
                                arabicText = item.optString("arabicText", ""),
                                translation = item.optString("translation", ""),
                                grade = item.optString("grade", "Shahih"),
                                foldertags = item.optString("folderTags", "Semua Bookmark"),
                                sanadSnippet = item.optString("sanadSnippet", ""),
                                userNote = item.optString("userNote", ""),
                                createdAt = item.optLong("createdAt", System.currentTimeMillis())
                            )
                            bookmarkDao.insertBookmark(entity)
                            bmRestored++
                        }
                    }
                }

                // 3. Pulihkan Catatan Kajian
                var notesRestored = 0
                val notesArray = rootJson.optJSONArray("notes")
                if (notesArray != null) {
                    for (i in 0 until notesArray.length()) {
                        val item = notesArray.optJSONObject(i) ?: continue
                        val id = item.optString("id", UUID.randomUUID().toString())
                        val content = item.optString("content", "")
                        if (content.isNotBlank()) {
                            val entity = NoteEntity(
                                id = id,
                                bookSlug = item.optString("bookSlug", ""),
                                number = item.optInt("number", 0),
                                bookName = item.optString("bookName", ""),
                                title = item.optString("title", "Catatan Kajian"),
                                content = content,
                                categoryTag = item.optString("categoryTag", "Kajian Umum"),
                                tags = item.optString("tags", ""),
                                arabicSnippet = item.optString("arabicSnippet", ""),
                                translationSnippet = item.optString("translationSnippet", ""),
                                firstSanad = item.optString("firstSanad", ""),
                                referencesJson = item.optString("referencesJson", ""),
                                isPinned = item.optBoolean("isPinned", false),
                                updatedAt = item.optLong("updatedAt", System.currentTimeMillis())
                            )
                            noteDao.saveNote(entity)
                            notesRestored++
                        }
                    }
                }

                // 4. Pulihkan Riwayat Bacaan
                var historyRestored = 0
                val historyArray = rootJson.optJSONArray("readingHistory")
                if (historyArray != null) {
                    for (i in 0 until historyArray.length()) {
                        val item = historyArray.optJSONObject(i) ?: continue
                        val bookSlug = item.optString("bookSlug", "")
                        val number = item.optInt("number", 0)
                        if (bookSlug.isNotBlank() && number > 0) {
                            val entity = ReadingHistoryEntity(
                                id = "${bookSlug.lowercase()}_$number",
                                bookSlug = bookSlug,
                                number = number,
                                bookName = item.optString("bookName", bookSlug),
                                kitab = item.optString("kitab", ""),
                                chapter = item.optString("chapter", ""),
                                arabicSnippet = item.optString("arabicSnippet", ""),
                                translationSnippet = item.optString("translationSnippet", ""),
                                readAt = item.optLong("readAt", System.currentTimeMillis())
                            )
                            readingHistoryDao.insertOrUpdate(entity)
                            historyRestored++
                        }
                    }
                }

                if (bmRestored == 0 && notesRestored == 0 && historyRestored == 0 && userObj == null) {
                    onError("Berkas JSON tidak memuat data cadangan HadithApp yang valid.")
                } else {
                    onSuccess(bmRestored, notesRestored, historyRestored)
                }
            } catch (e: Exception) {
                onError("Gagal memulihkan data: ${e.localizedMessage ?: "Format berkas tidak sesuai"}")
            }
        }
    }

    fun resetProfile(onComplete: () -> Unit) {
        viewModelScope.launch {
            userProfileRepo.resetProfile()
            onComplete()
        }
    }
}
