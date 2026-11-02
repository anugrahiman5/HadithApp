package com.nusa.hadithapp.ui.musthalah

import androidx.lifecycle.ViewModel
import com.nusa.hadithapp.data.data_source.local.catalog.MusthalahCatalog
import com.nusa.hadithapp.data.data_source.local.catalog.MusthalahQuizBank
import com.nusa.hadithapp.domain.model.musthalah.MusthalahCategory
import com.nusa.hadithapp.domain.model.musthalah.MusthalahQuizState
import com.nusa.hadithapp.domain.model.musthalah.MusthalahTerm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MusthalahViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        MusthalahUiState(
            terms = MusthalahCatalog.ALL_TERMS,
            totalCount = MusthalahCatalog.ALL_TERMS.size
        )
    )
    val uiState: StateFlow<MusthalahUiState> = _uiState.asStateFlow()

    private val _quizState = MutableStateFlow(MusthalahQuizState())
    val quizState: StateFlow<MusthalahQuizState> = _quizState.asStateFlow()

    fun startQuiz(count: Int = 5) {
        val questions = MusthalahQuizBank.generateDiverseQuizSession(count = count)
        _quizState.value = MusthalahQuizState(
            isDialogVisible = true,
            currentQuestionIndex = 0,
            totalQuestions = questions.size,
            score = 0,
            selectedOptionId = null,
            isAnswerSubmitted = false,
            isFinished = false,
            questions = questions
        )
    }

    fun answerQuizQuestion(optionId: String) {
        val current = _quizState.value
        if (current.isAnswerSubmitted) return
        val currentQ = current.questions.getOrNull(current.currentQuestionIndex) ?: return
        val isCorrect = currentQ.options.find { it.id == optionId }?.isCorrect == true
        val newScore = if (isCorrect) current.score + 1 else current.score

        _quizState.update {
            it.copy(
                selectedOptionId = optionId,
                isAnswerSubmitted = true,
                score = newScore
            )
        }
    }

    fun nextQuizQuestion() {
        val current = _quizState.value
        val nextIndex = current.currentQuestionIndex + 1
        if (nextIndex >= current.questions.size) {
            _quizState.update { it.copy(isFinished = true) }
        } else {
            _quizState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    selectedOptionId = null,
                    isAnswerSubmitted = false
                )
            }
        }
    }

    fun dismissQuiz() {
        _quizState.update { it.copy(isDialogVisible = false) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { current ->
            val filtered = MusthalahCatalog.searchTerms(query, current.selectedCategory)
            current.copy(
                searchQuery = query,
                terms = filtered
            )
        }
    }

    fun selectCategory(category: MusthalahCategory) {
        _uiState.update { current ->
            val filtered = MusthalahCatalog.searchTerms(current.searchQuery, category)
            current.copy(
                selectedCategory = category,
                terms = filtered
            )
        }
    }

    fun selectTerm(term: MusthalahTerm?) {
        _uiState.update { it.copy(selectedTermForDetail = term) }
    }

    fun selectTermById(termId: String) {
        val term = MusthalahCatalog.getTermById(termId)
        _uiState.update { it.copy(selectedTermForDetail = term) }
    }

    fun clearDetail() {
        _uiState.update { it.copy(selectedTermForDetail = null) }
    }
}
