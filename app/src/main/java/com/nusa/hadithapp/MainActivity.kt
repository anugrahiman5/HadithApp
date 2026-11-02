package com.nusa.hadithapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nusa.hadithapp.data.preferences.SettingsPreferencesRepository
import com.nusa.hadithapp.data.preferences.SettingsState
import com.nusa.hadithapp.ui.navigation.MainScreen
import com.nusa.hadithapp.ui.theme.HadithAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity - Entry point aplikasi Hadith
 * Menggunakan Jetpack Compose untuk UI dan Hilt untuk dependency injection
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    @Inject
    lateinit var settingsRepository: SettingsPreferencesRepository

    private var navTargetBookSlug by mutableStateOf<String?>(null)
    private var navTargetHadithNumber by mutableIntStateOf(-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate - savedInstanceState: ${savedInstanceState?.size()} bytes")
        handleNotificationIntent(intent)

        try {
            // Enable edge-to-edge display
            enableEdgeToEdge()

            setContent {
                val settings by settingsRepository.settingsFlow.collectAsState(initial = SettingsState())

                HadithAppTheme(
                    themeMode = settings.themeMode,
                    accentColor = settings.accentColor
                ) {
                    Surface(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        MainScreen(
                            navTargetBookSlug = navTargetBookSlug,
                            navTargetHadithNumber = navTargetHadithNumber,
                            onNavTargetConsumed = {
                                navTargetBookSlug = null
                                navTargetHadithNumber = -1
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting content", e)
            throw e  // Re-throw untuk crash properly
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val slug = intent?.getStringExtra("target_book_slug")
        val num = intent?.getIntExtra("target_hadith_number", -1) ?: -1
        if (!slug.isNullOrBlank() && num > 0) {
            navTargetBookSlug = slug
            navTargetHadithNumber = num
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}
