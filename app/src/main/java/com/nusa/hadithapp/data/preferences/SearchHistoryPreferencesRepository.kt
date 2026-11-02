package com.nusa.hadithapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.searchHistoryDataStore by preferencesDataStore(name = "search_history_prefs")

@Singleton
class SearchHistoryPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val RECENT_SEARCHES = stringPreferencesKey("recent_search_queries")
    }

    // Menggunakan pemisah '|||' agar urutan kronologis pencarian pengguna tetap terjaga
    val recentSearchesFlow: Flow<List<String>> = context.searchHistoryDataStore.data.map { prefs ->
        val raw = prefs[Keys.RECENT_SEARCHES] ?: ""
        if (raw.isBlank()) {
            emptyList()
        } else {
            raw.split("|||").filter { it.isNotBlank() }
        }
    }

    suspend fun addSearchQuery(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return

        context.searchHistoryDataStore.edit { prefs ->
            val currentRaw = prefs[Keys.RECENT_SEARCHES] ?: ""
            val currentList = if (currentRaw.isBlank()) emptyList() else currentRaw.split("|||").filter { it.isNotBlank() }
            val updated = listOf(trimmed) + currentList.filterNot { it.equals(trimmed, ignoreCase = true) }
            val capped = updated.take(15) // Maksimal 15 pencarian terakhir
            prefs[Keys.RECENT_SEARCHES] = capped.joinToString("|||")
        }
    }

    suspend fun removeSearchQuery(query: String) {
        val trimmed = query.trim()
        context.searchHistoryDataStore.edit { prefs ->
            val currentRaw = prefs[Keys.RECENT_SEARCHES] ?: ""
            val currentList = if (currentRaw.isBlank()) emptyList() else currentRaw.split("|||").filter { it.isNotBlank() }
            val updated = currentList.filterNot { it.equals(trimmed, ignoreCase = true) }
            prefs[Keys.RECENT_SEARCHES] = updated.joinToString("|||")
        }
    }

    suspend fun clearAll() {
        context.searchHistoryDataStore.edit { prefs ->
            prefs.remove(Keys.RECENT_SEARCHES)
        }
    }
}
