package com.nusa.hadithapp.data.repository

import com.nusa.hadithapp.data.data_source.local.dao.NoteDao
import com.nusa.hadithapp.data.data_source.local.entity.NoteEntity
import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.domain.model.ReferencedHadith
import com.nusa.hadithapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    override fun getAllNotes(): Flow<List<HadithNote>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNoteById(id: String): Flow<HadithNote?> {
        return noteDao.getNoteById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getNote(bookSlug: String, number: Int): Flow<HadithNote?> {
        val id = "${bookSlug.lowercase()}_$number"
        return getNoteById(id)
    }

    override suspend fun saveNote(note: HadithNote) {
        val referencesJson = if (note.references.isNotEmpty()) {
            try {
                json.encodeToString(note.references)
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }

        val entity = NoteEntity(
            id = note.id,
            bookSlug = note.bookSlug,
            number = note.number,
            bookName = note.bookName,
            title = note.title,
            content = note.content,
            categoryTag = note.categoryTag,
            tags = note.tags.joinToString(","),
            arabicSnippet = note.arabicSnippet,
            translationSnippet = note.translationSnippet,
            firstSanad = note.firstSanad,
            referencesJson = referencesJson,
            isPinned = note.isPinned,
            updatedAt = System.currentTimeMillis()
        )
        noteDao.saveNote(entity)
    }

    override suspend fun saveQuickNote(bookSlug: String, number: Int, content: String) {
        val id = "${bookSlug.lowercase()}_$number"
        val existingEntity = NoteEntity(
            id = id,
            bookSlug = bookSlug,
            number = number,
            title = "Catatan Hadits",
            content = content,
            updatedAt = System.currentTimeMillis()
        )
        noteDao.saveNote(existingEntity)
    }

    override suspend fun deleteNoteById(id: String) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun deleteNote(bookSlug: String, number: Int) {
        val id = "${bookSlug.lowercase()}_$number"
        deleteNoteById(id)
    }

    private fun NoteEntity.toDomain(): HadithNote {
        val tagList = tags.split(",").map { it.trim() }.filter { it.isNotBlank() }
        val loadedReferences = if (referencesJson.isNotBlank()) {
            try {
                json.decodeFromString<List<ReferencedHadith>>(referencesJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else if (bookName.isNotBlank() || arabicSnippet.isNotBlank()) {
            listOf(
                ReferencedHadith(
                    bookSlug = bookSlug,
                    number = number,
                    bookName = bookName,
                    arabicText = arabicSnippet,
                    translation = translationSnippet,
                    firstSanad = firstSanad
                )
            )
        } else {
            emptyList()
        }

        return HadithNote(
            id = id,
            bookSlug = bookSlug,
            number = number,
            bookName = bookName,
            title = title,
            content = content,
            categoryTag = categoryTag,
            tags = tagList,
            arabicSnippet = arabicSnippet,
            translationSnippet = translationSnippet,
            firstSanad = firstSanad,
            references = loadedReferences,
            isPinned = isPinned,
            updatedAt = updatedAt
        )
    }
}
