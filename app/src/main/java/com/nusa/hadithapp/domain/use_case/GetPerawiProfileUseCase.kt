package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.PerawiProfile
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetPerawiProfileUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(kodeRawi: Int): PerawiProfile? {
        return repository.getPerawiProfile(kodeRawi)
    }
}