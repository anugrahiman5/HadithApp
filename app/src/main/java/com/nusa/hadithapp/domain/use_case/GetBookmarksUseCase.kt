package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case untuk mengambil SEMUA hadits yang tersimpan di bookmark.
 */
class GetBookmarksUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(): Flow<List<Bookmark>> = repository.getAllBookmarks()
}