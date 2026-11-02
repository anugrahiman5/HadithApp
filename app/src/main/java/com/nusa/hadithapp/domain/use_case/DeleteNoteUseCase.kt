package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteNoteById(id)
    }

    suspend operator fun invoke(bookSlug: String, number: Int) {
        repository.deleteNote(bookSlug, number)
    }
}
