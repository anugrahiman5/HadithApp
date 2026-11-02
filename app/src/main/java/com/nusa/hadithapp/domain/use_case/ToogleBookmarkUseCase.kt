package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import javax.inject.Inject

@Deprecated("Typo in name, use ToggleBookmarkUseCase instead", ReplaceWith("ToggleBookmarkUseCase"))
class ToogleBookmarkUseCase @Inject constructor(
    private val delegate: ToggleBookmarkUseCase
) {
    suspend operator fun invoke(hadith: Hadith, bookSlug: String, isCurrentlyBookmarked: Boolean) {
        delegate(hadith, bookSlug, isCurrentlyBookmarked)
    }
}
