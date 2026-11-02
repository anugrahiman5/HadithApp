package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsBookmarkedUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(bookSlug: String, number: Int): Flow<Boolean> {
        return repository.isBookmarked(bookSlug, number)
    }
}