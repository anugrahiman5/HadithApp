package com.nusa.hadithapp.data.preferences

import com.nusa.hadithapp.util.FontSettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Adapter untuk backward-compatibility dengan kode lama.
 * Mendelegasikan seluruh operasi ke [SettingsPreferencesRepository].
 */
@Singleton
class FontPreferencesRepository @Inject constructor(
    private val settingsRepository: SettingsPreferencesRepository
) {

    val fontSettingsFlow: Flow<FontSettingsState> = settingsRepository.fontSettingsFlow

    suspend fun saveFontSettings(settings: FontSettingsState) {
        val current = settingsRepository.settingsFlow.first()
        settingsRepository.saveSettings(
            current.copy(
                arabicFontName = settings.arabicFontName,
                arabicFontSize = settings.arabicFontSize,
                indonesianFontName = settings.indonesianFontName,
                indonesianFontSize = settings.indonesianFontSize,
                showFullHarakat = settings.showFullHarakat,
                arabicLineSpacing = settings.arabicLineSpacing
            )
        )
    }
}
