package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetHadithDetailByThemeUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(themeName: String, vMember: Int): Hadith {
        return repository.getHadithByThemeVMember(themeName, vMember)
    }
}