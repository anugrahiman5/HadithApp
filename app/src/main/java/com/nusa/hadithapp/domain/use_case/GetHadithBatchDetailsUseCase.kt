package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.HadithSearchItem
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithBatchDetailsUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(bookSlug: String, numbers: List<Int>): List<HadithSearchItem> {
        return repository.getHadithBatchDetails(bookSlug, numbers)
    }
}
