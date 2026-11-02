package com.nusa.hadithapp.domain.repository

import com.nusa.hadithapp.domain.model.HadithNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<HadithNote>>
    fun getNoteById(id: String): Flow<HadithNote?>
    fun getNote(bookSlug: String, number: Int): Flow<HadithNote?>
    suspend fun saveNote(note: HadithNote)
    suspend fun saveQuickNote(bookSlug: String, number: Int, content: String)
    suspend fun deleteNoteById(id: String)
    suspend fun deleteNote(bookSlug: String, number: Int)
}
