package com.nusa.hadithapp.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private const val TAG = "HadithAppTheme"

fun buildWarmColorScheme(accent: AppAccentColor) = lightColorScheme(
    primary = accent.color,
    onPrimary = accent.onColor,
    primaryContainer = accent.containerColor,
    onPrimaryContainer = accent.onContainerColor,
    secondary = GoldSecondaryLight,
    onSecondary = GoldOnSecondaryLight,
    secondaryContainer = GoldContainerLight,
    onSecondaryContainer = OnGoldContainerLight,
    tertiary = AccentCyanLight,
    onTertiary = Color.White,
    background = AppPalette.WarmBackground,
    onBackground = AppPalette.WarmOnBackground,
    surface = AppPalette.WarmSurface,
    onSurface = AppPalette.WarmOnSurface,
    surfaceVariant = AppPalette.WarmSurfaceVariant,
    onSurfaceVariant = AppPalette.WarmOnSurfaceVariant,
    outline = AppPalette.WarmOutline,
    outlineVariant = AppPalette.WarmOutlineVariant,
    error = StatusMaudhu,
    onError = Color.White
)

fun buildDarkColorScheme(accent: AppAccentColor) = darkColorScheme(
    primary = accent.darkColor,
    onPrimary = Color(0xFF003823),
    primaryContainer = accent.darkContainerColor,
    onPrimaryContainer = accent.darkOnContainerColor,
    secondary = GoldSecondaryDark,
    onSecondary = GoldOnSecondaryDark,
    secondaryContainer = GoldContainerDark,
    onSecondaryContainer = OnGoldContainerDark,
    tertiary = AccentCyanDark,
    onTertiary = Color.White,
    background = AppPalette.DarkBackground,
    onBackground = AppPalette.DarkOnBackground,
    surface = AppPalette.DarkSurface,
    onSurface = AppPalette.DarkOnSurface,
    surfaceVariant = AppPalette.DarkSurfaceVariant,
    onSurfaceVariant = AppPalette.DarkOnSurfaceVariant,
    outline = AppPalette.DarkOutline,
    outlineVariant = AppPalette.DarkOutlineVariant,
    error = StatusMaudhu,
    onError = Color.White
)

fun buildLightColorScheme(accent: AppAccentColor) = lightColorScheme(
    primary = accent.color,
    onPrimary = accent.onColor,
    primaryContainer = accent.containerColor,
    onPrimaryContainer = accent.onContainerColor,
    secondary = GoldSecondaryLight,
    onSecondary = GoldOnSecondaryLight,
    secondaryContainer = GoldContainerLight,
    onSecondaryContainer = OnGoldContainerLight,
    tertiary = AccentCyanLight,
    onTertiary = Color.White,
    background = AppPalette.LightBackground,
    onBackground = AppPalette.LightOnBackground,
    surface = AppPalette.LightSurface,
    onSurface = AppPalette.LightOnSurface,
    surfaceVariant = AppPalette.LightSurfaceVariant,
    onSurfaceVariant = AppPalette.LightOnSurfaceVariant,
    outline = AppPalette.LightOutline,
    outlineVariant = AppPalette.LightOutlineVariant,
    error = StatusMaudhu,
    onError = Color.White
)

private val HadithShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(16.dp)
)

/**
 * Main theme composable untuk Hadith App.
 * Mendukung tema dinamis (Kertas Hangat, Malam Khusyuk, Ikuti Sistem)
 * serta 4 aksen warna ilmiah tanpa hardcoded colors.
 */
@Composable
fun HadithAppTheme(
    themeMode: AppThemeMode = AppThemeMode.WARM,
    accentColor: AppAccentColor = AppAccentColor.TEAL,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isEffectiveDark = when (themeMode) {
        AppThemeMode.WARM -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> darkTheme
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isEffectiveDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeMode == AppThemeMode.WARM -> buildWarmColorScheme(accentColor)
        themeMode == AppThemeMode.DARK -> buildDarkColorScheme(accentColor)
        isEffectiveDark -> buildDarkColorScheme(accentColor)
        else -> buildLightColorScheme(accentColor)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            try {
                val activity = view.context as? Activity ?: return@SideEffect
                val window = activity.window ?: return@SideEffect

                window.statusBarColor = colorScheme.background.toArgb()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.navigationBarColor = colorScheme.background.toArgb()
                }
                WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars =
                    !isEffectiveDark
            } catch (e: Exception) {
                Log.e(TAG, "Error applying theme to window", e)
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = HadithShapes,
        content = content
    )
}
