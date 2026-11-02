package com.nusa.hadithapp.ui.saved.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.LastReadPreferencesRepository
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.domain.model.ReadingHistoryItem
import com.nusa.hadithapp.domain.model.ReferencedHadith
import com.nusa.hadithapp.domain.repository.ReadingHistoryRepository
import com.nusa.hadithapp.domain.use_case.GetBookmarksUseCase
import com.nusa.hadithapp.domain.use_case.GetHadithDetailUseCase
import com.nusa.hadithapp.domain.use_case.GetNoteByIdUseCase
import com.nusa.hadithapp.domain.use_case.SaveStudyNoteUseCase
import com.nusa.hadithapp.ui.home.LastReadHadith
import com.nusa.hadithapp.util.BookSlugUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class StudyNoteEditorUiState(
    val noteId: String = "",
    val title: String = "",
    val content: String = "",
    val categoryBinder: String = "Syarah Fathul Bari",
    val tags: List<String> = listOf("Ibn Hajar Al-Asqalani", "Bab Niat"),
    val isPinned: Boolean = false,
    val references: List<ReferencedHadith> = emptyList(),
    val isSaved: Boolean = false
)

@HiltViewModel
class StudyNoteEditorViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveStudyNoteUseCase: SaveStudyNoteUseCase,
    private val getHadithDetailUseCase: GetHadithDetailUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val readingHistoryRepository: ReadingHistoryRepository,
    private val lastReadPreferencesRepository: LastReadPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyNoteEditorUiState())
    val uiState: StateFlow<StudyNoteEditorUiState> = _uiState.asStateFlow()

    private val _quickSearchResult = MutableStateFlow<ReferencedHadith?>(null)
    val quickSearchResult: StateFlow<ReferencedHadith?> = _quickSearchResult.asStateFlow()

    private val undoStack = mutableListOf<String>()
    private val redoStack = mutableListOf<String>()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    val bookmarksFlow: StateFlow<List<Bookmark>> = getBookmarksUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingHistoryFlow: StateFlow<List<ReadingHistoryItem>> = readingHistoryRepository.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastReadFlow: StateFlow<LastReadHadith?> = lastReadPreferencesRepository.lastReadFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        val noteId = savedStateHandle.get<String>("noteId") ?: ""
        val bookSlug = savedStateHandle.get<String>("bookSlug") ?: ""
        val number = savedStateHandle.get<Int>("number") ?: -1

        if (noteId.isNotBlank()) {
            loadExistingNote(noteId)
        } else if (bookSlug.isNotBlank() && number > 0) {
            attachHadithReferenceFromApi(bookSlug, number)
        } else {
            _uiState.update { it.copy(noteId = UUID.randomUUID().toString()) }
        }
    }

    private fun loadExistingNote(id: String) {
        viewModelScope.launch {
            getNoteByIdUseCase(id).collect { note ->
                note?.let {
                    _uiState.update { current ->
                        current.copy(
                            noteId = it.id,
                            title = it.title,
                            content = it.content,
                            categoryBinder = it.categoryTag,
                            tags = it.tags,
                            isPinned = it.isPinned,
                            references = it.references
                        )
                    }
                }
            }
        }
    }

    private fun attachHadithReferenceFromApi(slug: String, num: Int) {
        viewModelScope.launch {
            try {
                val hadith = getHadithDetailUseCase(slug, num)
                val sanadAwwal =
                    hadith.sanadJalurList.firstOrNull()?.lastOrNull()?.name ?: "Sahabat Nabi"

                val ref = ReferencedHadith(
                    bookSlug = slug,
                    number = num,
                    bookName = BookSlugUtil.getOfficialBookName(slug),
                    grade = hadith.grade,
                    arabicText = hadith.arabicText,
                    translation = hadith.translation,
                    firstSanad = "Sanad Tertinggi: $sanadAwwal"
                )

                _uiState.update {
                    it.copy(
                        noteId = "${slug.lowercase()}_$num",
                        references = listOf(ref),
                        title = if (it.title.isBlank()) "Kajian ${
                            BookSlugUtil.getOfficialBookName(
                                slug
                            )
                        } No. $num" else it.title
                    )
                }
            } catch (e: Exception) {
                // Abaikan jika offline
            }
        }
    }

    fun addReferencedHadith(newRef: ReferencedHadith) {
        _uiState.update { state ->
            val filtered = state.references.filterNot { it.bookSlug == newRef.bookSlug && it.number == newRef.number }
            val updated = filtered + newRef
            state.copy(
                references = updated,
                title = if (state.title.isBlank()) "Kajian ${newRef.bookName} No. ${newRef.number}" else state.title
            )
        }

        // Jika sanad kosong atau hanya teks ringkasan (snippet), perkaya secara asinkron dari detail use case
        if (newRef.firstSanad.isBlank() || newRef.translation.length <= 150) {
            viewModelScope.launch {
                try {
                    val fullHadith = getHadithDetailUseCase(newRef.bookSlug, newRef.number)
                    val sanadAwwal = fullHadith.sanadJalurList.firstOrNull()?.lastOrNull()?.name ?: "Sahabat Nabi"
                    val enriched = newRef.copy(
                        bookName = BookSlugUtil.getOfficialBookName(newRef.bookSlug),
                        grade = fullHadith.grade,
                        arabicText = fullHadith.arabicText,
                        translation = fullHadith.translation,
                        firstSanad = "Sanad: $sanadAwwal"
                    )
                    swapReferencedHadith(newRef, enriched)
                } catch (e: Exception) {
                    // Abaikan jika offline
                }
            }
        }
    }

    fun removeReferencedHadith(ref: ReferencedHadith) {
        _uiState.update { state ->
            state.copy(references = state.references - ref)
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun updateContent(newContent: String) {
        val current = _uiState.value.content
        if (current != newContent) {
            if (undoStack.isEmpty() || undoStack.last() != current) {
                undoStack.add(current)
                if (undoStack.size > 50) undoStack.removeAt(0)
            }
            redoStack.clear()
            _canUndo.value = undoStack.isNotEmpty()
            _canRedo.value = false
            _uiState.update { it.copy(content = newContent) }
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val prev = undoStack.removeAt(undoStack.lastIndex)
            redoStack.add(_uiState.value.content)
            _canUndo.value = undoStack.isNotEmpty()
            _canRedo.value = redoStack.isNotEmpty()
            _uiState.update { it.copy(content = prev) }
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val next = redoStack.removeAt(redoStack.lastIndex)
            undoStack.add(_uiState.value.content)
            _canUndo.value = undoStack.isNotEmpty()
            _canRedo.value = redoStack.isNotEmpty()
            _uiState.update { it.copy(content = next) }
        }
    }

    fun clearContent() {
        updateContent("")
    }

    fun insertText(insert: String) {
        val current = _uiState.value.content
        updateContent(if (current.isBlank()) insert else "$current $insert")
    }

    fun updateCategoryBinder(category: String) {
        _uiState.update { it.copy(categoryBinder = category) }
    }

    fun togglePinned() {
        _uiState.update { it.copy(isPinned = !it.isPinned) }
    }

    fun addTag(tag: String) {
        val cleanTag = tag.trim().removePrefix("#")
        if (cleanTag.isNotBlank() && !_uiState.value.tags.contains(cleanTag)) {
            _uiState.update { it.copy(tags = it.tags + cleanTag) }
        }
    }

    fun removeTag(tag: String) {
        _uiState.update { it.copy(tags = it.tags - tag) }
    }

    fun saveStudyNote(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val primaryHadith = state.references.firstOrNull()

            val note = HadithNote(
                id = if (state.noteId.isNotBlank()) state.noteId else UUID.randomUUID().toString(),
                bookSlug = primaryHadith?.bookSlug ?: "",
                number = primaryHadith?.number ?: 0,
                bookName = primaryHadith?.bookName ?: "",
                title = if (state.title.isBlank()) "Catatan Tanpa Judul" else state.title,
                content = state.content,
                categoryTag = state.categoryBinder,
                tags = state.tags,
                arabicSnippet = primaryHadith?.arabicText ?: "",
                translationSnippet = primaryHadith?.translation ?: "",
                firstSanad = primaryHadith?.firstSanad ?: "",
                references = state.references,
                isPinned = state.isPinned,
                updatedAt = System.currentTimeMillis()
            )

            saveStudyNoteUseCase(note)
            onSuccess()
        }
    }

    fun swapReferencedHadith(oldRef: ReferencedHadith, newRef: ReferencedHadith) {
        _uiState.update { state ->
            val updated = state.references.map { current ->
                if (current.bookSlug == oldRef.bookSlug && current.number == oldRef.number) newRef else current
            }
            state.copy(references = updated)
        }
    }

    fun lookupHadith(bookSlug: String, number: Int) {
        viewModelScope.launch {
            try {
                val hadith = getHadithDetailUseCase(bookSlug, number)
                val sanadAwal = hadith.sanadJalurList.firstOrNull()?.lastOrNull()?.name ?: "Sahabat Nabi"
                _quickSearchResult.value = ReferencedHadith(
                    bookSlug = bookSlug,
                    number = number,
                    bookName = BookSlugUtil.getOfficialBookName(bookSlug),
                    grade = hadith.grade,
                    arabicText = hadith.arabicText,
                    translation = hadith.translation,
                    firstSanad = "Sanad: $sanadAwal"
                )
            } catch (e: Exception) {
                _quickSearchResult.value = null
            }
        }
    }
}
