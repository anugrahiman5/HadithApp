package com.nusa.hadithapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nusa.hadithapp.ui.theme.AppAccentColor
import com.nusa.hadithapp.ui.theme.AppThemeMode
import com.nusa.hadithapp.util.FontSettingsState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * State lengkap untuk pengaturan preferensi baca dan tampilan aplikasi.
 */
data class SettingsState(
    val arabicFontName: String = "Amiri Naskh",
    val arabicFontSize: Float = 26f,
    val showFullHarakat: Boolean = true,
    val arabicLineSpacing: Float = 2.0f,
    val indonesianFontName: String = "Roboto",
    val indonesianFontSize: Float = 15f,
    val highlightSanadInTranslation: Boolean = true,
    val showScholarNotes: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.WARM,
    val accentColor: AppAccentColor = AppAccentColor.TEAL,
    val isDailyReminderEnabled: Boolean = false,
    val reminderHour: Int = 5,
    val reminderMinute: Int = 30,
    val geminiApiKey: String = "",
    val geminiModel: String = "gemini-1.5-flash"
) {
    fun toFontSettingsState(): FontSettingsState {
        return FontSettingsState(
            arabicFontName = arabicFontName,
            arabicFontSize = arabicFontSize,
            indonesianFontName = indonesianFontName,
            indonesianFontSize = indonesianFontSize,
            showFullHarakat = showFullHarakat,
            arabicLineSpacing = arabicLineSpacing
        )
    }
}

private val Context.settingsDataStore by preferencesDataStore(name = "hadith_app_settings_prefs")

/**
 * Repository terpusat untuk menyimpan seluruh preferensi baca & tampilan aplikasi
 * menggunakan AndroidX DataStore Preferences.
 */
@Singleton
class SettingsPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val ARABIC_FONT_NAME = stringPreferencesKey("arabic_font_name")
        val ARABIC_FONT_SIZE = floatPreferencesKey("arabic_font_size")
        val SHOW_FULL_HARAKAT = booleanPreferencesKey("show_full_harakat")
        val ARABIC_LINE_SPACING = floatPreferencesKey("arabic_line_spacing")
        val INDONESIAN_FONT_NAME = stringPreferencesKey("indonesian_font_name")
        val INDONESIAN_FONT_SIZE = floatPreferencesKey("indonesian_font_size")
        val HIGHLIGHT_SANAD = booleanPreferencesKey("highlight_sanad_translation")
        val SHOW_SCHOLAR_NOTES = booleanPreferencesKey("show_scholar_notes")
        val THEME_MODE = stringPreferencesKey("app_theme_mode")
        val ACCENT_COLOR = stringPreferencesKey("app_accent_color")
        val FAVORITE_IMAM_IDS = stringSetPreferencesKey("favorite_imam_ids")
        val IS_DAILY_REMINDER_ENABLED = booleanPreferencesKey("is_daily_reminder_enabled")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
        val GEMINI_API_KEY = stringPreferencesKey("gemini_api_key")
        val GEMINI_MODEL = stringPreferencesKey("gemini_model")
    }

    val favoriteImamsFlow: Flow<Set<String>> = context.settingsDataStore.data.map { prefs ->
        prefs[Keys.FAVORITE_IMAM_IDS] ?: emptySet()
    }

    suspend fun toggleFavoriteImam(imamId: String): Boolean {
        var isNowFavorite = false
        context.settingsDataStore.edit { prefs ->
            val current = prefs[Keys.FAVORITE_IMAM_IDS] ?: emptySet()
            if (current.contains(imamId)) {
                prefs[Keys.FAVORITE_IMAM_IDS] = current - imamId
                isNowFavorite = false
            } else {
                prefs[Keys.FAVORITE_IMAM_IDS] = current + imamId
                isNowFavorite = true
            }
        }
        return isNowFavorite
    }

    val settingsFlow: Flow<SettingsState> = context.settingsDataStore.data.map { prefs ->
        SettingsState(
            arabicFontName = prefs[Keys.ARABIC_FONT_NAME] ?: "Amiri Naskh",
            arabicFontSize = prefs[Keys.ARABIC_FONT_SIZE] ?: 26f,
            showFullHarakat = prefs[Keys.SHOW_FULL_HARAKAT] ?: true,
            arabicLineSpacing = prefs[Keys.ARABIC_LINE_SPACING] ?: 2.0f,
            indonesianFontName = prefs[Keys.INDONESIAN_FONT_NAME] ?: "Roboto",
            indonesianFontSize = prefs[Keys.INDONESIAN_FONT_SIZE] ?: 15f,
            highlightSanadInTranslation = prefs[Keys.HIGHLIGHT_SANAD] ?: true,
            showScholarNotes = prefs[Keys.SHOW_SCHOLAR_NOTES] ?: true,
            themeMode = AppThemeMode.fromId(prefs[Keys.THEME_MODE] ?: AppThemeMode.WARM.id),
            accentColor = AppAccentColor.fromId(prefs[Keys.ACCENT_COLOR] ?: AppAccentColor.TEAL.id),
            isDailyReminderEnabled = prefs[Keys.IS_DAILY_REMINDER_ENABLED] ?: false,
            reminderHour = prefs[Keys.REMINDER_HOUR] ?: 5,
            reminderMinute = prefs[Keys.REMINDER_MINUTE] ?: 30,
            geminiApiKey = prefs[Keys.GEMINI_API_KEY] ?: "",
            geminiModel = prefs[Keys.GEMINI_MODEL] ?: "gemini-1.5-flash"
        )
    }

    val fontSettingsFlow: Flow<FontSettingsState> = settingsFlow.map { it.toFontSettingsState() }

    suspend fun saveSettings(settings: SettingsState) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.ARABIC_FONT_NAME] = settings.arabicFontName
            prefs[Keys.ARABIC_FONT_SIZE] = settings.arabicFontSize
            prefs[Keys.SHOW_FULL_HARAKAT] = settings.showFullHarakat
            prefs[Keys.ARABIC_LINE_SPACING] = settings.arabicLineSpacing
            prefs[Keys.INDONESIAN_FONT_NAME] = settings.indonesianFontName
            prefs[Keys.INDONESIAN_FONT_SIZE] = settings.indonesianFontSize
            prefs[Keys.HIGHLIGHT_SANAD] = settings.highlightSanadInTranslation
            prefs[Keys.SHOW_SCHOLAR_NOTES] = settings.showScholarNotes
            prefs[Keys.THEME_MODE] = settings.themeMode.id
            prefs[Keys.ACCENT_COLOR] = settings.accentColor.id
            prefs[Keys.IS_DAILY_REMINDER_ENABLED] = settings.isDailyReminderEnabled
            prefs[Keys.REMINDER_HOUR] = settings.reminderHour
            prefs[Keys.REMINDER_MINUTE] = settings.reminderMinute
            prefs[Keys.GEMINI_API_KEY] = settings.geminiApiKey
            prefs[Keys.GEMINI_MODEL] = settings.geminiModel
        }
    }

    suspend fun resetToDefaults() {
        saveSettings(SettingsState())
    }
}
