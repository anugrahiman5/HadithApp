package com.nusa.hadithapp.ui.search

import com.nusa.hadithapp.domain.model.RawiSearchItem

sealed interface RawiSearchUiState {
    data object Idle : RawiSearchUiState
    data object Loading : RawiSearchUiState
    data class Success(val rawiList: List<RawiSearchItem>) : RawiSearchUiState
    data class Error(val message: String) : RawiSearchUiState
    data object Empty : RawiSearchUiState
}