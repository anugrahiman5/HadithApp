package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithDetailUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(bookSlug: String, number: Int): Hadith {
        return repository.getHadithByBook(bookSlug, number)
    }
}