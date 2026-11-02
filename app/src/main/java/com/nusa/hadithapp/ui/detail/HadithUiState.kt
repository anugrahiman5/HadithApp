package com.nusa.hadithapp.ui.detail

import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.PerawiProfile
import com.nusa.hadithapp.domain.model.ScholarComment
import com.nusa.hadithapp.domain.model.SimilarHadith

sealed interface HadithUiState {
    data object Empty : HadithUiState
    data object Loading : HadithUiState
    data class Success(val hadith: Hadith) : HadithUiState
    data class Error(val message: String) : HadithUiState
}

sealed interface PerawiUiState {
    data object Empty : PerawiUiState
    data object Loading : PerawiUiState
    data class Success(val profile: PerawiProfile) : PerawiUiState
    data class Error(val message: String) : PerawiUiState
}

sealed interface ScholarCommentUiState {
    data object Empty : ScholarCommentUiState
    data object Loading : ScholarCommentUiState
    data class Success(val comments: List<ScholarComment>) : ScholarCommentUiState
    data class Error(val message: String) : ScholarCommentUiState
}

sealed interface SimilarHadithUiState {
    data object Empty : SimilarHadithUiState
    data object Loading : SimilarHadithUiState
    data class Success(val items: List<SimilarHadith>) : SimilarHadithUiState
    data class Error(val message: String) : SimilarHadithUiState
}

sealed interface AiResponseState {
    data object Idle : AiResponseState
    data object Loading : AiResponseState
    data class Success(val answer: String, val prompt: String) : AiResponseState
    data class Error(val message: String, val isApiKeyMissing: Boolean = false) : AiResponseState
}

