package com.nusa.hadithapp.domain.model.musthalah

data class MusthalahQuizOption(
    val id: String,
    val text: String,
    val isCorrect: Boolean
)

data class MusthalahQuizQuestion(
    val id: String,
    val questionPrompt: String,
    val categoryBadge: String,
    val options: List<MusthalahQuizOption>,
    val explanation: String
)

data class MusthalahQuizState(
    val isDialogVisible: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val score: Int = 0,
    val selectedOptionId: String? = null,
    val isAnswerSubmitted: Boolean = false,
    val isFinished: Boolean = false,
    val questions: List<MusthalahQuizQuestion> = emptyList()
)
