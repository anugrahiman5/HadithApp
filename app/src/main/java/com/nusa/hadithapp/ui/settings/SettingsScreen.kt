package com.nusa.hadithapp.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FormatColorText
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.nusa.hadithapp.data.preferences.SettingsState
import com.nusa.hadithapp.ui.theme.AppAccentColor
import com.nusa.hadithapp.ui.theme.AppPalette
import com.nusa.hadithapp.ui.theme.AppThemeMode
import com.nusa.hadithapp.util.HadithTextColor
import com.nusa.hadithapp.util.formatHadithText
import com.nusa.hadithapp.util.getArabicFontFamily
import com.nusa.hadithapp.util.getIndonesianFontFamily
import com.nusa.hadithapp.ui.settings.components.*

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val cacheSize by viewModel.cacheSizeText.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Column {
                        Text(
                            text = "Pengaturan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tampilan & Preferensi Baca",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.resetToDefaults {
                                Toast.makeText(context, "Pengaturan diatur ulang ke bawaan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Atur Ulang",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. KARTU PRATINJAU LANGSUNG (WYSIWYG REAL-TIME PREVIEW)
            RealTimePreviewSection(settings = settings)

            // 2. TIPOGRAFI & TEKS ARAB
            ArabicTypographySection(
                settings = settings,
                onFontChange = { viewModel.updateArabicFont(it) },
                onFontSizeChange = { viewModel.updateArabicFontSize(it) },
                onHarakatChange = { viewModel.updateFullHarakat(it) },
                onLineSpacingChange = { viewModel.updateArabicLineSpacing(it) }
            )

            // 3. TEKS TERJEMAHAN INDONESIA
            IndonesianTranslationSection(
                settings = settings,
                onFontSizeChange = { viewModel.updateIndonesianFontSize(it) },
                onSanadHighlightChange = { viewModel.updateSanadHighlight(it) },
                onScholarNotesChange = { viewModel.updateScholarNotes(it) }
            )

            // 4. TEMA & TAMPILAN LAYAR
            ThemeAppearanceSection(
                settings = settings,
                onThemeModeChange = { viewModel.updateThemeMode(it) },
                onAccentColorChange = { viewModel.updateAccentColor(it) }
            )

            // 5. PENGINGAT HADITS HARIAN
            DailyReminderSection(
                settings = settings,
                onReminderEnabledChange = { viewModel.updateDailyReminderEnabled(it) },
                onReminderTimeChange = { h, m -> viewModel.updateReminderTime(h, m) },
                onTestNotification = { viewModel.testDailyReminderNotification() }
            )

            // 6. PENYIMPANAN & CACHE LOKAL
            StorageCacheSection(
                cacheSize = cacheSize,
                onClearCache = {
                    viewModel.clearCache {
                        Toast.makeText(context, "Cache aplikasi berhasil dibersihkan", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            // 7. ASISTEN AI HADITS (OPSIONAL / BYOK)
            AiAssistantSection(
                settings = settings,
                onApiKeyChange = { viewModel.updateGeminiApiKey(it) },
                onModelChange = { viewModel.updateGeminiModel(it) }
            )

            // 8. FOOTER BRANDING & INFORMASI VERSI
            SettingsFooter()
        }
    }
}

/**
 * 1. Bagian Pratinjau Langsung (WYSIWYG Live Preview)
 */
