package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetPerawiTotalHadithUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(bookSlug: String, kodeRawi: Int): List<Int> {
        return repository.getPerawiTotalHadith(bookSlug, kodeRawi)
    }
}
