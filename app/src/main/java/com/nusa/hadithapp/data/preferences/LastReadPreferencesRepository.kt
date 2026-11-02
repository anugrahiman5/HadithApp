package com.nusa.hadithapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nusa.hadithapp.ui.home.LastReadHadith
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.lastReadPreferencesStore by preferencesDataStore(name = "last_read_prefs")

@Singleton
class LastReadPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val BOOK_SLUG = stringPreferencesKey("last_read_book_slug")
        val BOOK_NAME = stringPreferencesKey("last_read_book_name")
        val NUMBER = intPreferencesKey("last_read_number")
        val TITLE = stringPreferencesKey("last_read_title")
        val KITAB = stringPreferencesKey("last_read_kitab")
        val PROGRESS_PERCENT = floatPreferencesKey("last_read_progress_percent")
        val TIMESTAMP = longPreferencesKey("last_read_timestamp")
    }

    val lastReadFlow: Flow<LastReadHadith?> = context.lastReadPreferencesStore.data.map { preferences ->
        val slug = preferences[PreferencesKeys.BOOK_SLUG] ?: return@map null
        val bookName = preferences[PreferencesKeys.BOOK_NAME] ?: "Kitab Hadits"
        val number = preferences[PreferencesKeys.NUMBER] ?: 1
        val title = preferences[PreferencesKeys.TITLE] ?: "Hadits Pilihan"
        val kitab = preferences[PreferencesKeys.KITAB] ?: ""
        val progress = preferences[PreferencesKeys.PROGRESS_PERCENT] ?: 0f
        val timestamp = preferences[PreferencesKeys.TIMESTAMP] ?: System.currentTimeMillis()

        LastReadHadith(
            bookSlug = slug,
            bookName = bookName,
            number = number,
            title = title,
            progressPercent = progress.coerceIn(0f, 1f),
            timestamp = timestamp,
            kitab = kitab
        )
    }

    suspend fun saveLastRead(lastRead: LastReadHadith) {
        context.lastReadPreferencesStore.edit { preferences ->
            preferences[PreferencesKeys.BOOK_SLUG] = lastRead.bookSlug
            preferences[PreferencesKeys.BOOK_NAME] = lastRead.bookName
            preferences[PreferencesKeys.NUMBER] = lastRead.number
            preferences[PreferencesKeys.TITLE] = lastRead.title
            preferences[PreferencesKeys.KITAB] = lastRead.kitab
            preferences[PreferencesKeys.PROGRESS_PERCENT] = lastRead.progressPercent
            preferences[PreferencesKeys.TIMESTAMP] = if (lastRead.timestamp > 0L) lastRead.timestamp else System.currentTimeMillis()
        }
    }
}
