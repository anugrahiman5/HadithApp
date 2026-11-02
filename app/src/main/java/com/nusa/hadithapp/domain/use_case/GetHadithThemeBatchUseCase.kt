package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.HadithThemeItem
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithThemeBatchUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(
        themeName: String,
        startVMember: Int,
        endVMember: Int
    ): List<HadithThemeItem> {
        return repository.getHadithThemeBatch(themeName, startVMember, endVMember)
    }
}