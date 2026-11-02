package com.nusa.hadithapp.data.session

import com.nusa.hadithapp.domain.model.HadithSearchItem
import com.nusa.hadithapp.util.BookSlugUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager khusus untuk menampung data pencarian aktif saat pengguna berpindah
 * dari SearchScreen ke HadithDetailScreen (untuk fitur Next / Previous Hadits).
 *
 * Memisahkan state ini dari Repository menjaga Repository tetap Stateless & Clean.
 */
@Singleton
class SearchSessionManager @Inject constructor() {

    private val _activeSearchItems = mutableListOf<HadithSearchItem>()
    val activeSearchItems: List<HadithSearchItem>
        get() = _activeSearchItems

    private val _selectedTargetBooks = MutableStateFlow<Set<String>>(BookSlugUtil.ALL_BOOK_SLUGS)
    val selectedTargetBooksFlow: StateFlow<Set<String>> = _selectedTargetBooks.asStateFlow()

    var selectedTargetBooks: Set<String>
        get() = _selectedTargetBooks.value
        set(value) {
            _selectedTargetBooks.value = if (value.isNotEmpty()) value else BookSlugUtil.ALL_BOOK_SLUGS
        }

    fun updateSearchItems(items: List<HadithSearchItem>) {
        _activeSearchItems.clear()
        _activeSearchItems.addAll(items)
    }

    fun getItemByIndex(index: Int): HadithSearchItem? {
        return _activeSearchItems.getOrNull(index)
    }

    fun getIndex(bookSlug: String, number: Int): Int {
        return _activeSearchItems.indexOfFirst {
            it.bookSlug.equals(bookSlug, ignoreCase = true) && it.number == number
        }
    }

    fun clearSession() {
        _activeSearchItems.clear()
    }
}
