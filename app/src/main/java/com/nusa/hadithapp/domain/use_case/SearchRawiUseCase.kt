package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.RawiSearchItem
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class SearchRawiUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(
        name: String,
        kuniyah: String,
        classify: String,
        level: String
    ): List<RawiSearchItem> {
        return repository.searchRawi(
            name = name,
            kuniyah = kuniyah,
            classify = classify,
            level = level
        )
    }
}
