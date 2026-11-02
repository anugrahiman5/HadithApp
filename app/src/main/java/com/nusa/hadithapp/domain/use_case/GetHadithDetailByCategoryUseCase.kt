package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithDetailByCategoryUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(categoryName: String, vMember: Int): Hadith {
        return repository.getHadithByCategoryVMember(categoryName, vMember)
    }
}