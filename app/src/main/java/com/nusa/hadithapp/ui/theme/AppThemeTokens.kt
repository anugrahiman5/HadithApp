package com.nusa.hadithapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Enum untuk Mode Tema Tampilan Baca.
 * Mengatur latar belakang (background) dan kontras mata.
 */
enum class AppThemeMode(
    val id: String,
    val title: String,
    val subtitle: String
) {
    WARM("WARM", "Kertas Hangat", "#FFF8F5"),
    DARK("DARK", "Malam Khusyuk", "Charcoal"),
    SYSTEM("SYSTEM", "Ikuti Sistem", "Otomatis");

    companion object {
        fun fromId(id: String): AppThemeMode {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: WARM
        }
    }
}

/**
 * Enum untuk Pilihan Aksen Warna Ilmiah.
 * Berpedoman pada estetika manuskrip dan literatur hadits klasik.
 * Tanpa hardcoded nilai warna di UI.
 */
enum class AppAccentColor(
    val id: String,
    val title: String,
    val color: Color,
    val onColor: Color,
    val containerColor: Color,
    val onContainerColor: Color,
    val darkColor: Color,
    val darkContainerColor: Color,
    val darkOnContainerColor: Color
) {
    TEAL(
        id = "TEAL",
        title = "Teal Tholabul Ilmi (Klasik)",
        color = Color(0xFF0D5C52),
        onColor = Color.White,
        containerColor = Color(0xFFD2F4EE),
        onContainerColor = Color(0xFF00201C),
        darkColor = Color(0xFF65D8C7),
        darkContainerColor = Color(0xFF005047),
        darkOnContainerColor = Color(0xFF84F5E3)
    ),
    BROWN(
        id = "BROWN",
        title = "Terakota / Cokelat Kitab Klasik",
        color = Color(0xFF8D4925),
        onColor = Color.White,
        containerColor = Color(0xFFFFDBCF),
        onContainerColor = Color(0xFF380D00),
        darkColor = Color(0xFFFFB59A),
        darkContainerColor = Color(0xFF703211),
        darkOnContainerColor = Color(0xFFFFDBCF)
    ),
    BLUE(
        id = "BLUE",
        title = "Biru Safir Nabawi",
        color = Color(0xFF1B4F8A),
        onColor = Color.White,
        containerColor = Color(0xFFD5E3FF),
        onContainerColor = Color(0xFF001B3C),
        darkColor = Color(0xFFA6C8FF),
        darkContainerColor = Color(0xFF00325B),
        darkOnContainerColor = Color(0xFFD5E3FF)
    ),
    MAROON(
        id = "MAROON",
        title = "Marun Granada (Merah Tinta Manuskrip)",
        color = Color(0xFF85222B),
        onColor = Color.White,
        containerColor = Color(0xFFFFDAD9),
        onContainerColor = Color(0xFF3B0810),
        darkColor = Color(0xFFFFB3B4),
        darkContainerColor = Color(0xFF690A17),
        darkOnContainerColor = Color(0xFFFFDAD9)
    );

    companion object {
        fun fromId(id: String): AppAccentColor {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: TEAL
        }
    }
}

/**
 * Token Warna Netral Terpusat untuk Seluruh Aplikasi.
 * Menghindari penulisan Color(0x...) langsung di UI composable.
 */
object AppPalette {
    // Kertas Hangat (Sepia / Warm Paper)
    val WarmBackground = Color(0xFFFFF8F5)
    val WarmOnBackground = Color(0xFF2C2523)
    val WarmSurface = Color(0xFFFFFFFF)
    val WarmOnSurface = Color(0xFF2C2523)
    val WarmSurfaceVariant = Color(0xFFF7EFE9)
    val WarmOnSurfaceVariant = Color(0xFF5D524D)
    val WarmOutline = Color(0xFFE2D6CE)
    val WarmOutlineVariant = Color(0xFFEFE5DE)

    // Malam Khusyuk (Charcoal Dark)
    val DarkBackground = Color(0xFF14171A)
    val DarkOnBackground = Color(0xFFF1F5F9)
    val DarkSurface = Color(0xFF1E2226)
    val DarkOnSurface = Color(0xFFF1F5F9)
    val DarkSurfaceVariant = Color(0xFF282D33)
    val DarkOnSurfaceVariant = Color(0xFF94A3B8)
    val DarkOutline = Color(0xFF3E4651)
    val DarkOutlineVariant = Color(0xFF2F3640)

    // Sistem Terang Default (Clean White Light)
    val LightBackground = Color(0xFFF8FAFC)
    val LightOnBackground = Color(0xFF0F172A)
    val LightSurface = Color(0xFFFFFFFF)
    val LightOnSurface = Color(0xFF0F172A)
    val LightSurfaceVariant = Color(0xFFF1F5F9)
    val LightOnSurfaceVariant = Color(0xFF475569)
    val LightOutline = Color(0xFFCBD5E1)
    val LightOutlineVariant = Color(0xFFE2E8F0)

    // Status Derajat Hadits Otentik
    val StatusShahih = Color(0xFF2E7D32)
    val StatusShahihContainer = Color(0xFFE8F5E9)
    val StatusHasan = Color(0xFF0288D1)
    val StatusHasanContainer = Color(0xFFE1F5FE)
    val StatusDhaif = Color(0xFFED6C02)
    val StatusDhaifContainer = Color(0xFFFFF3E0)
    val StatusMaudhu = Color(0xFFD32F2F)
    val StatusMaudhuContainer = Color(0xFFFFEBEE)
}
