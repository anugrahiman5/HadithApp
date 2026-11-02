package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.repository.NoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(bookSlug: String, number: Int, content: String) {
        val trimmed = content.trim()
        if (trimmed.isNotBlank()) {
            repository.saveQuickNote(bookSlug, number, trimmed)
        } else {
            repository.deleteNote(bookSlug, number)
        }
    }
}
