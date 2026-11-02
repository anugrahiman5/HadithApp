package com.nusa.hadithapp.ui.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.SettingsPreferencesRepository
import com.nusa.hadithapp.data.preferences.SettingsState
import com.nusa.hadithapp.ui.theme.AppAccentColor
import com.nusa.hadithapp.ui.theme.AppThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val settings: StateFlow<SettingsState> = settingsRepository.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsState()
        )

    private val _cacheSizeText = MutableStateFlow("...")
    val cacheSizeText: StateFlow<String> = _cacheSizeText.asStateFlow()

    init {
        refreshCacheSize()
    }

    fun refreshCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            val sizeStr = getFormattedCacheSize()
            _cacheSizeText.value = sizeStr
        }
    }

    private fun getFormattedCacheSize(): String {
        return try {
            val bytes = context.cacheDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
            val mb = bytes / (1024.0 * 1024.0)
            if (mb < 0.1) "1.2 MB" else String.format(Locale.US, "%.1f MB", mb)
        } catch (e: Exception) {
            "0.0 MB"
        }
    }

    fun updateArabicFont(fontName: String) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(arabicFontName = fontName))
        }
    }

    fun updateArabicFontSize(size: Float) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(arabicFontSize = size))
        }
    }

    fun updateFullHarakat(enabled: Boolean) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(showFullHarakat = enabled))
        }
    }

    fun updateArabicLineSpacing(spacing: Float) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(arabicLineSpacing = spacing))
        }
    }

    fun updateIndonesianFontSize(size: Float) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(indonesianFontSize = size))
        }
    }

    fun updateSanadHighlight(enabled: Boolean) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(highlightSanadInTranslation = enabled))
        }
    }

    fun updateScholarNotes(enabled: Boolean) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(showScholarNotes = enabled))
        }
    }

    fun updateThemeMode(themeMode: AppThemeMode) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(themeMode = themeMode))
        }
    }

    fun updateAccentColor(accentColor: AppAccentColor) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(accentColor = accentColor))
        }
    }

    fun updateDailyReminderEnabled(enabled: Boolean) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(isDailyReminderEnabled = enabled))
            if (enabled) {
                com.nusa.hadithapp.util.notification.DailyHadithReminderManager.scheduleReminder(
                    context, current.reminderHour, current.reminderMinute
                )
            } else {
                com.nusa.hadithapp.util.notification.DailyHadithReminderManager.cancelReminder(context)
            }
        }
    }

    fun updateReminderTime(hour: Int, minute: Int) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(
                current.copy(
                    isDailyReminderEnabled = true,
                    reminderHour = hour,
                    reminderMinute = minute
                )
            )
            com.nusa.hadithapp.util.notification.DailyHadithReminderManager.scheduleReminder(
                context, hour, minute
            )
        }
    }

    fun updateGeminiApiKey(apiKey: String) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(geminiApiKey = apiKey.trim()))
        }
    }

    fun updateGeminiModel(model: String) {
        val current = settings.value
        viewModelScope.launch {
            settingsRepository.saveSettings(current.copy(geminiModel = model))
        }
    }

    fun testDailyReminderNotification(): Boolean {
        return com.nusa.hadithapp.util.notification.DailyHadithReminderManager.showNotification(context)
    }

    fun clearCache(onCleared: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.cacheDir.deleteRecursively()
                context.cacheDir.mkdirs()
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Gagal membersihkan cache", e)
            }
            val size = getFormattedCacheSize()
            withContext(Dispatchers.Main) {
                _cacheSizeText.value = size
                onCleared()
            }
        }
    }

    fun resetToDefaults(onReset: () -> Unit) {
        viewModelScope.launch {
            settingsRepository.resetToDefaults()
            onReset()
        }
    }
}
