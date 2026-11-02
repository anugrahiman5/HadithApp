package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.SimilarHadith
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetSimilarHadithUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(bookSlug: String, number: Int): List<SimilarHadith> {
        return repository.getSimilarHadith(bookSlug, number)
    }
}
