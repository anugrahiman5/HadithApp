package com.nusa.hadithapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class UserProfile(
    val name: String = "Thalibul Ilmi",
    val roleTitle: String = "Penuntut Ilmu (Thalibul Ilmi)",
    val pinCode: String = "",
    val isPinEnabled: Boolean = false
)

private val Context.userProfileDataStore by preferencesDataStore(name = "user_profile_prefs")

@Singleton
class UserProfilePreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val NAME = stringPreferencesKey("user_name")
        val ROLE_TITLE = stringPreferencesKey("user_role_title")
        val PIN_CODE = stringPreferencesKey("user_pin_code")
        val IS_PIN_ENABLED = booleanPreferencesKey("user_is_pin_enabled")
    }

    val profileFlow: Flow<UserProfile> = context.userProfileDataStore.data.map { prefs ->
        UserProfile(
            name = prefs[Keys.NAME] ?: "Thalibul Ilmi",
            roleTitle = prefs[Keys.ROLE_TITLE] ?: "Penuntut Ilmu (Thalibul Ilmi)",
            pinCode = prefs[Keys.PIN_CODE] ?: "",
            isPinEnabled = prefs[Keys.IS_PIN_ENABLED] ?: false
        )
    }

    suspend fun updateProfile(name: String, roleTitle: String) {
        context.userProfileDataStore.edit { prefs ->
            prefs[Keys.NAME] = name
            prefs[Keys.ROLE_TITLE] = roleTitle
        }
    }

    suspend fun updatePin(pin: String, enabled: Boolean) {
        context.userProfileDataStore.edit { prefs ->
            prefs[Keys.PIN_CODE] = pin
            prefs[Keys.IS_PIN_ENABLED] = enabled
        }
    }

    suspend fun resetProfile() {
        context.userProfileDataStore.edit { prefs ->
            prefs[Keys.NAME] = "Thalibul Ilmi"
            prefs[Keys.ROLE_TITLE] = "Penuntut Ilmu (Thalibul Ilmi)"
            prefs[Keys.PIN_CODE] = ""
            prefs[Keys.IS_PIN_ENABLED] = false
        }
    }
}
