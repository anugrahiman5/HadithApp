package com.nusa.hadithapp.ui.classification.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.data_source.remote.config.HadithClassificationMetadata
import com.nusa.hadithapp.domain.use_case.GetHadithThemeBatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeResultViewModel @Inject constructor(
    private val getHadithThemeBatchUseCase: GetHadithThemeBatchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ThemeResultUiState>(ThemeResultUiState.Empty)
    val uiState: StateFlow<ThemeResultUiState> = _uiState.asStateFlow()
    private val _selectedBookSlug = MutableStateFlow<String?>(null)
    val selectedBookSlug: StateFlow<String?> = _selectedBookSlug.asStateFlow()

    private var currentStart = 1
    private val batchSize = 10
    private var totalMaxCount = 18921
    private var currentThemeName = ""

    fun loadHadithByTheme(themeName: String) {
        currentThemeName = themeName
        _selectedBookSlug.value = null
        currentStart = 1

        totalMaxCount = HadithClassificationMetadata.getTotalCount(themeName)

        viewModelScope.launch {
            fetchBatch(themeName, currentStart, isLoadMore = false)
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState is ThemeResultUiState.Success && !currentState.isLoadingMore && currentState.hasMore) {
            _uiState.value = currentState.copy(isLoadingMore = true)
            currentStart += batchSize
            viewModelScope.launch {
                fetchBatch(currentThemeName, currentStart, isLoadMore = true)
            }
        }
    }

    fun loadHadithByBook(themeName: String, bookSlug: String) {
        currentThemeName = themeName
        _selectedBookSlug.value = bookSlug
        currentStart = HadithClassificationMetadata.getStartingGlobalIndex(themeName, bookSlug)
        val bookCount = HadithClassificationMetadata.getBookCount(themeName, bookSlug)

        totalMaxCount = currentStart + bookCount - 1

        viewModelScope.launch {
            fetchBatch(themeName, currentStart, isLoadMore = false)
        }
    }

    private suspend fun fetchBatch(themeName: String, start: Int, isLoadMore: Boolean) {
        if (!isLoadMore) {
            _uiState.value = ThemeResultUiState.Loading
        }

        try {
            val targetEnd = minOf(start + batchSize - 1, totalMaxCount)
            if (start > totalMaxCount) {
                if (isLoadMore && _uiState.value is ThemeResultUiState.Success) {
                    val current = _uiState.value as ThemeResultUiState.Success
                    _uiState.value = current.copy(isLoadingMore = false, hasMore = false)
                }
                return
            }

            val result = getHadithThemeBatchUseCase(
                themeName = themeName,
                start,
                targetEnd
            )

            val selectedSlug = _selectedBookSlug.value
            val filteredResult = if (!selectedSlug.isNullOrEmpty()) {
                val cleanSelected = selectedSlug.lowercase()
                result.filter { item ->
                    item.bookSlug.lowercase().contains(cleanSelected) ||
                            cleanSelected.contains(item.bookSlug.lowercase())
                }
            } else {
                result
            }

            if (isLoadMore) {
                val currentState = _uiState.value
                if (currentState is ThemeResultUiState.Success) {
                    val updatedItems = currentState.items + filteredResult
                    _uiState.value = currentState.copy(
                        items = updatedItems,
                        isLoadingMore = false,
                        hasMore = start + batchSize <= totalMaxCount && filteredResult.isNotEmpty()
                    )
                }
            } else {
                if (filteredResult.isNotEmpty()) {
                    _uiState.value = ThemeResultUiState.Success(
                        themeName = themeName,
                        items = result,
                        isLoadingMore = false,
                        hasMore = start + batchSize <= totalMaxCount
                    )
                } else {
                    _uiState.value =
                        ThemeResultUiState.Error("Tidak ada Hadits ditemukan untuk kategori ini.")
                }
            }
        } catch (e: Exception) {
            Log.e("THEME_VM_ERROR", "Gagal memuat hadits: ", e)
            if (isLoadMore) {
                val currentState = _uiState.value
                if (currentState is ThemeResultUiState.Success) {
                    _uiState.value = currentState.copy(isLoadingMore = false)
                }
            } else {
                _uiState.value = ThemeResultUiState.Error(
                    e.localizedMessage ?: "Terjadi kendala saat memuat data."
                )
            }
        }
    }
}