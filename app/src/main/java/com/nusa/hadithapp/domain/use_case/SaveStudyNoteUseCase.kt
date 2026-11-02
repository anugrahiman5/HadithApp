package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.HadithNote
import com.nusa.hadithapp.domain.repository.NoteRepository
import javax.inject.Inject

class SaveStudyNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: HadithNote) {
        repository.saveNote(note)
    }
}
