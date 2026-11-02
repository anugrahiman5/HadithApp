package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case untuk mengambil data status dan folder dari SATU hadits spesifik.
 */
class GetBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(bookSlug: String, number: Int): Flow<Bookmark?> {
        return repository.getBookmark(bookSlug, number)
    }
}