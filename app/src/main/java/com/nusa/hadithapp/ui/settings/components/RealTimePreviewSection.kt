package com.nusa.hadithapp.ui.settings.components

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

@Composable
fun RealTimePreviewSection(
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "PRATINJAU LANGSUNG",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp
                )
            }
            Text(
                text = "Real-time",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Chip Bukhari #1 & Status Shahih
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "Bukhari #1",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AppPalette.StatusShahihContainer
                        ) {
                            Text(
                                text = "✓ Ijma': Shahih",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = AppPalette.StatusShahih,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Teks Arab Matan
                val arabicRawText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى..."
                val displayedArabic = remember(arabicRawText, settings.showFullHarakat) {
                    if (settings.showFullHarakat) {
                        arabicRawText
                    } else {
                        // Hilangkan harakat untuk kitab gundul
                        arabicRawText.replace(Regex("""[\u064B-\u0652\u0670\u06D6-\u06ED]"""), "")
                    }
                }

                val arabicFontFamily = remember(settings.arabicFontName) {
                    getArabicFontFamily(settings.arabicFontName)
                }

                Text(
                    text = displayedArabic,
                    fontSize = settings.arabicFontSize.sp,
                    lineHeight = (settings.arabicFontSize * settings.arabicLineSpacing).sp,
                    fontFamily = arabicFontFamily,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                // Ornamen Pemisah Simbolik
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✧ ─── ✧",
                        color = MaterialTheme.colorScheme.outlineVariant,
                        fontSize = 12.sp
                    )
                }

                // Terjemahan Indonesia dengan Sanad Highlight
                val indonesianRaw = "\"[Al-Humaidi] menceritakan kepada kami, ia berkata: Sesungguhnya segala amalan tergantung pada niatnya, dan sesungguhnya setiap orang akan mendapatkan apa yang ia niatkan...\""
                val textColors = HadithTextColor(
                    squareBracketColor = MaterialTheme.colorScheme.primary,
                    curlyBracketColor = AppPalette.StatusShahih,
                    searchHighlightBg = Color.Transparent,
                    searchHighlightText = MaterialTheme.colorScheme.onSurface
                )

                val formattedTranslation = remember(indonesianRaw, settings.highlightSanadInTranslation, textColors) {
                    formatHadithText(
                        text = indonesianRaw,
                        highlightSanad = settings.highlightSanadInTranslation,
                        colors = textColors
                    )
                }

                val indonesianFont = remember(settings.indonesianFontName) {
                    getIndonesianFontFamily(settings.indonesianFontName)
                }

                Text(
                    text = formattedTranslation,
                    fontSize = settings.indonesianFontSize.sp,
                    lineHeight = (settings.indonesianFontSize * 1.55f).sp,
                    fontFamily = indonesianFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Info footer kecil di kartu pratinjau
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FormatColorText,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Pratinjau pembaruan ukuran teks dan keterbacaan secara instan.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * 2. Bagian Tipografi & Teks Arab
 */
