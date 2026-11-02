package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.HadithCategoryItem
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithCategoryBatchUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(categoryName: String, startVMember: Int, endVMember: Int): List<HadithCategoryItem> {
        return repository.getHadithCategoryBatch(categoryName, startVMember, endVMember)
    }
}