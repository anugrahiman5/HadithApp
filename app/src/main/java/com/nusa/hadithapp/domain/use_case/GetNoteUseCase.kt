package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(bookSlug: String, number: Int): Flow<HadithNote?> {
        return repository.getNote(bookSlug, number)
    }
}