package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.data.data_source.remote.GeminiApiService
import com.nusa.hadithapp.data.preferences.SettingsPreferencesRepository
import com.nusa.hadithapp.domain.model.Hadith
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AskHadithAiUseCase @Inject constructor(
    private val geminiApiService: GeminiApiService,
    private val settingsPreferencesRepository: SettingsPreferencesRepository
) {
    suspend operator fun invoke(
        hadith: Hadith,
        userQuery: String,
        apiKeyOverride: String? = null,
        modelOverride: String? = null
    ): Result<String> {
        val currentSettings = settingsPreferencesRepository.settingsFlow.first()
        val apiKey = apiKeyOverride ?: currentSettings.geminiApiKey
        val model = modelOverride ?: currentSettings.geminiModel

        if (apiKey.isBlank()) {
            return Result.failure(
                IllegalStateException("Kunci API Gemini belum diatur. Silakan atur di menu Pengaturan.")
            )
        }

        return geminiApiService.askHadithExplanation(
            apiKey = apiKey,
            model = model,
            hadith = hadith,
            userQuery = userQuery
        )
    }
}
