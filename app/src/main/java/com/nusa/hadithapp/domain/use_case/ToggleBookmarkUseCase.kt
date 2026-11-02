package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(hadith: Hadith, bookSlug: String, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            repository.removeBookmark(bookSlug, hadith.number)
        } else {
            repository.addBookmark(hadith, bookSlug)
        }
    }
}
