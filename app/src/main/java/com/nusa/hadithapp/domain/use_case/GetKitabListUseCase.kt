package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.KitabItem
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetKitabListUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(bookSlug: String): List<KitabItem> {
        return repository.getAllBooks(bookSlug)
    }
}