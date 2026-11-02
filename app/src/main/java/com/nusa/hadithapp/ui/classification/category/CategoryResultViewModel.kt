package com.nusa.hadithapp.ui.classification.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.data_source.remote.config.HadithClassificationMetadata
import com.nusa.hadithapp.domain.use_case.GetHadithCategoryBatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryResultViewModel @Inject constructor(
    private val getHadithCategoryBatchUseCase: GetHadithCategoryBatchUseCase
) : ViewModel(){
    private val _uiState = MutableStateFlow<CategoryResultUiState>(CategoryResultUiState.Empty)
    val uiState: StateFlow<CategoryResultUiState> = _uiState.asStateFlow()
    private val _selectedBookSlug = MutableStateFlow<String?>(null)
    val selectedBookSlug: StateFlow<String?> = _selectedBookSlug.asStateFlow()

    private var currentStart = 1
    private val batchSize = 10
    private var totalMaxCount = 100
    private var currentCategoryName = ""

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState is CategoryResultUiState.Success && !currentState.isLoadingMore && currentState.hasMore) {
            _uiState.value = currentState.copy(isLoadingMore = true)
            currentStart += batchSize
            viewModelScope.launch {
                fetchBatch(currentCategoryName, currentStart, isLoadMore = true)
            }
        }
    }

    fun loadHadithByCategory(categoryName: String) {
        currentCategoryName = categoryName
        _selectedBookSlug.value = null
        currentStart = 1

        totalMaxCount = HadithClassificationMetadata.getTotalCount(categoryName)

        viewModelScope.launch {
            fetchBatch(categoryName, currentStart, isLoadMore = false)
        }
    }

    fun loadHadithByBook(categoryName: String, bookSlug: String) {
        currentCategoryName = categoryName
        _selectedBookSlug.value = bookSlug
        currentStart = HadithClassificationMetadata.getStartingGlobalIndex(categoryName, bookSlug)
        val bookCount = HadithClassificationMetadata.getBookCount(categoryName, bookSlug)

        totalMaxCount = currentStart + bookCount - 1

        viewModelScope.launch {
            fetchBatch(categoryName, currentStart, isLoadMore = false)
        }
    }

    private suspend fun fetchBatch(categoryName: String, start: Int, isLoadMore: Boolean) {
        if (!isLoadMore) {
            _uiState.value = CategoryResultUiState.Loading
        }

        try {
            val targetEnd = minOf(start + batchSize - 1, totalMaxCount)

            if (start > totalMaxCount) {
                if (isLoadMore && _uiState.value is CategoryResultUiState.Success) {
                    val current = _uiState.value as CategoryResultUiState.Success
                    _uiState.value = current.copy(isLoadingMore = false, hasMore = false)
                }
                return
            }

            val result = getHadithCategoryBatchUseCase(
                categoryName = categoryName,
                startVMember = start,
                endVMember = targetEnd
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
                if (currentState is CategoryResultUiState.Success) {
                    val updatedItems = currentState.items + filteredResult
                    _uiState.value = currentState.copy(
                        items = updatedItems,
                        isLoadingMore = false,
                        hasMore = start + batchSize <= totalMaxCount && filteredResult.isNotEmpty()
                    )
                }
            } else {
                if (filteredResult.isNotEmpty()) {
                    _uiState.value = CategoryResultUiState.Success(
                        categoryName = categoryName,
                        items = filteredResult,
                        isLoadingMore = false,
                        hasMore = start + batchSize <= totalMaxCount
                    )
                } else {
                    _uiState.value =
                        CategoryResultUiState.Error("Tidak ada Hadits ditemukan untuk kategori ini.")
                }
            }
        } catch (e: Exception) {
            Log.e("CATEGORY_VM_ERROR", "Gagal memuat hadits kategori: ", e)
            if (isLoadMore) {
                val currentState = _uiState.value
                if (currentState is CategoryResultUiState.Success) {
                    _uiState.value = currentState.copy(isLoadingMore = false)
                }
            } else {
                _uiState.value = CategoryResultUiState.Error(
                    e.localizedMessage ?: "Terjadi kendala saat memuat data."
                )
            }
        }
    }
}