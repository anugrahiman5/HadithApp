package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.BabItem
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetBabListUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(bookSlug: String, start: Int, end: Int): List<BabItem> {
        return repository.getAllChapters(bookSlug, start, end)
    }
}