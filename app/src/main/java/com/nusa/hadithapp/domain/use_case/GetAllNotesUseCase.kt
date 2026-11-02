package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<HadithNote>> {
        return repository.getAllNotes()
    }
}
