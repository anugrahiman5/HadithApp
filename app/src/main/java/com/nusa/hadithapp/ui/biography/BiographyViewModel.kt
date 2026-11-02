package com.nusa.hadithapp.ui.biography

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nusa.hadithapp.data.preferences.SettingsPreferencesRepository
import com.nusa.hadithapp.domain.model.biography.BiographyTab
import com.nusa.hadithapp.domain.repository.BiographyRepository
import com.nusa.hadithapp.util.FontSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiographyViewModel @Inject constructor(
    private val repository: BiographyRepository,
    private val settingsPreferencesRepository: SettingsPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BiographyUiState())
    val uiState: StateFlow<BiographyUiState> = _uiState.asStateFlow()

    val fontSettingsFlow: StateFlow<FontSettingsState> = settingsPreferencesRepository.fontSettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FontSettingsState())

    val favoriteImamsFlow: StateFlow<Set<String>> = settingsPreferencesRepository.favoriteImamsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private var bioJob: Job? = null

    init {
        val allImams = repository.getAllImams()
        _uiState.update { it.copy(allImams = allImams) }
        val defaultId = allImams.firstOrNull()?.id ?: "Bukhari"
        selectImam(defaultId)
    }

    fun selectImam(rawKey: String) {
        val canonicalBio = com.nusa.hadithapp.data.data_source.local.catalog.ImamBioCatalog.getImam(rawKey)
        val canonicalId = canonicalBio.id
        if (_uiState.value.selectedImamId == canonicalId && _uiState.value.currentBiography != null) return
        _uiState.update { it.copy(selectedImamId = canonicalId, currentBiography = canonicalBio) }
        loadBiography(canonicalId)
    }

    fun selectTab(tab: BiographyTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateFontSettings(newSettings: FontSettingsState) {
        viewModelScope.launch {
            val current = settingsPreferencesRepository.settingsFlow.first()
            settingsPreferencesRepository.saveSettings(
                current.copy(
                    arabicFontName = newSettings.arabicFontName,
                    arabicFontSize = newSettings.arabicFontSize,
                    indonesianFontName = newSettings.indonesianFontName,
                    indonesianFontSize = newSettings.indonesianFontSize,
                    showFullHarakat = newSettings.showFullHarakat,
                    arabicLineSpacing = newSettings.arabicLineSpacing
                )
            )
        }
    }

    fun toggleFavoriteCurrentImam(onToggled: (isFavorite: Boolean) -> Unit) {
        val currentId = _uiState.value.selectedImamId
        if (currentId.isNotBlank()) {
            viewModelScope.launch {
                val isFav = settingsPreferencesRepository.toggleFavoriteImam(currentId)
                onToggled(isFav)
            }
        }
    }

    private fun loadBiography(imamId: String) {
        bioJob?.cancel()
        bioJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isApiEnriched = false) }
            repository.getBiography(imamId).collect { bio ->
                _uiState.update { state ->
                    state.copy(
                        currentBiography = bio,
                        isLoading = false,
                        isApiEnriched = !bio.apiRawText.isNullOrBlank()
                    )
                }
            }
        }
    }
}
