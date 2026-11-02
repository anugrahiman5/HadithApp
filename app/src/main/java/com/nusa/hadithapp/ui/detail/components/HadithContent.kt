package com.nusa.hadithapp.ui.detail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.util.FontSettingsState
import com.nusa.hadithapp.util.HadithTextDefaults
import com.nusa.hadithapp.util.KitabPublisher
import com.nusa.hadithapp.util.formatHadithText
import com.nusa.hadithapp.util.getArabicFontFamily
import com.nusa.hadithapp.util.getIndonesianFontFamily

@Composable
fun HadithContent(
    hadith: Hadith,
    fontSettings: FontSettingsState = FontSettingsState(),
    similarCount: Int? = null,
    onGradeClick: () -> Unit,
    onOpenSanadSheet: () -> Unit,
    onOpenSimilarHadith: () -> Unit,
    onOpenAiSheet: () -> Unit = {},
    onRawiClick: (String) -> Unit,
    isArabicAudioPlaying: Boolean = false,
    onPlayArabicAudio: () -> Unit = {},
    isIndonesianAudioPlaying: Boolean = false,
    onPlayIndonesianAudio: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Baris Badge Derajat & Rujukan Kitab (Scroll Horizontal Rapi Satu Baris)
        HadithBadgesRow(
            hadith = hadith,
            onGradeClick = onGradeClick
        )

        // 2. Card Teks Hadits Bahasa Arab (Tipografi Elegan & Seragam + Audio TTS)
        ArabicHadithCard(
            hadith = hadith,
            fontSettings = fontSettings,
            isAudioPlaying = isArabicAudioPlaying,
            onPlayAudio = onPlayArabicAudio
        )

        // 3. Card Terjemahan Indonesia (Alur Paragraf Natural + Audio TTS)
        IndonesianTranslationCard(
            translationText = hadith.translation,
            fontSettings = fontSettings,
            onRawiClick = onRawiClick,
            isAudioPlaying = isIndonesianAudioPlaying,
            onPlayAudio = onPlayIndonesianAudio
        )

        // 4. Tombol Aksi Utama: Lihat Sanad & Hadits Serupa
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onOpenSanadSheet,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountTree,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val sanadLabel = if (hadith.sanadJalurList.isNotEmpty()) {
                    "Sanad (${hadith.sanadJalurList.size} jalur)"
                } else {
                    "Lihat Sanad"
                }
                Text(
                    text = sanadLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onOpenSimilarHadith,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val similarLabel = if (similarCount != null && similarCount > 0) {
                    "Serupa ($similarCount)"
                } else {
                    "Hadits Serupa"
                }
                Text(
                    text = similarLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 5. Tombol Aksi Tanya Syarah AI (Mode Ulama)
        Surface(
            onClick = onOpenAiSheet,
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Tanya Syarah AI (Mode Ulama)",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tadabbur, faedah fiqih, & kosakata hadits",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Tanya",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Baris Badge Derajat Hadits & Rujukan Penomoran Standar Kitab
 * Disusun dalam horizontal scroll agar tidak memakan ruang vertikal dan muat satu baris
 */
@Composable
private fun HadithBadgesRow(
    hadith: Hadith,
    onGradeClick: () -> Unit
) {
    val rawPublisher = KitabPublisher.getPublisherName(hadith.bookName, isCompact = true)
    val cleanPublisher = rawPublisher.trimEnd(':').trim()
    val hasValidPublisher = cleanPublisher.isNotBlank() && cleanPublisher != "-"
    val hasOtherNumber = hadith.otherNumber.isNotBlank() && hadith.otherNumber != "0" && hadith.otherNumber != "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Badge Derajat Keshahihan Hadits
        if (hadith.bookName.contains("Bukhari", ignoreCase = true) || hadith.bookName.contains("Muslim", ignoreCase = true)) {
            Surface(
                onClick = onGradeClick,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Text(
                    text = "🔰 Ijma' Ulama: Shahih",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        } else if (hadith.researcherList.isNotEmpty()) {
            hadith.researcherList.forEach { researcher ->
                val shortName = when {
                    researcher.researcher.contains("Albani", ignoreCase = true) -> "Al-Albani"
                    researcher.researcher.contains("Zubair", ignoreCase = true) -> "Zubair 'Ali Zai"
                    researcher.researcher.contains("Arnauth", ignoreCase = true) -> "Al-Arnauth"
                    researcher.researcher.contains("Syakir", ignoreCase = true) -> "Ahmad Syakir"
                    researcher.researcher.contains("Dzahabi", ignoreCase = true) -> "Adz-Dzahabi"
                    researcher.researcher.contains("Ijma", ignoreCase = true) -> "Ijma' Ulama"
                    else -> researcher.researcher
                }
                val cleanGrade = researcher.grade
                    .replace("\n", " • ")
                    .replace(Regex("\\s+"), " ")
                    .trim()

                Surface(
                    onClick = onGradeClick,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = "🔰 $shortName: $cleanGrade",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        } else if (hadith.grade.isNotBlank()) {
            val cleanGrade = hadith.grade.replace("\n", " • ").trim()
            Surface(
                onClick = onGradeClick,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Text(
                    text = "🔰 $cleanGrade",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        // Badge Rujukan Penomoran Standar Kitab (Fathul Bari / Baitul Afkar dsb)
        if (hasValidPublisher && hasOtherNumber) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f))
            ) {
                Text(
                    text = "📖 $cleanPublisher: No. ${hadith.otherNumber}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

/**
 * Card Teks Hadits Bahasa Arab
 * Ditata dengan tipografi Scheherazade yang konsisten, megah, dan proporsional
 */
@Composable
private fun ArabicHadithCard(
    hadith: Hadith,
    fontSettings: FontSettingsState,
    isAudioPlaying: Boolean = false,
    onPlayAudio: () -> Unit = {}
) {
    val arabicFamily = remember(fontSettings.arabicFontName) {
        getArabicFontFamily(fontSettings.arabicFontName)
    }

    val arabicParagraphs = remember(hadith.arabicText, fontSettings.showFullHarakat) {
        val processedText = if (fontSettings.showFullHarakat) {
            hadith.arabicText
        } else {
            hadith.arabicText.replace(Regex("""[\u064B-\u0652\u0670\u06D6-\u06ED]"""), "")
        }
        processedText.split("\n").map { it.trim() }.filter { it.isNotBlank() }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Header kartu arab: label & tombol pelafalan audio TTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TEKS ARAB",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                    letterSpacing = 0.8.sp
                )
                HadithAudioActionButton(
                    isPlaying = isAudioPlaying,
                    label = "Lafalkan Arab",
                    onClick = onPlayAudio
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Teks Arab: Jika ada baris baru, ditampilkan berurutan dengan ukuran dan font seragam
            arabicParagraphs.forEachIndexed { index, paragraph ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        lineHeight = (fontSettings.arabicFontSize * fontSettings.arabicLineSpacing).sp,
                        fontSize = fontSettings.arabicFontSize.sp,
                        fontFamily = arabicFamily,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
            Spacer(modifier = Modifier.height(10.dp))

            // Footer metadata kartu arab
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${hadith.bookName} • Hadits ${hadith.number}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                if (hadith.kitab.isNotBlank()) {
                    Text(
                        text = hadith.kitab,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private enum class ParagraphKind {
    SANAD,
    MATAN,
    COMMENTARY
}

/**
 * Mengklasifikasikan paragraf secara alami tanpa asumsi rapuh
 */
private fun classifyParagraph(paragraph: String, index: Int): ParagraphKind {
    val clean = paragraph.trim()

    // 1. Cek Komentar / Catatan Imam atau Sahabat
    val isCommentary = clean.startsWith("Abu Dawud", ignoreCase = true) ||
            clean.startsWith("Abu Daud", ignoreCase = true) ||
            clean.startsWith("Abu Isa", ignoreCase = true) ||
            clean.startsWith("Abu 'Isa", ignoreCase = true) ||
            clean.startsWith("Al-Bukhari", ignoreCase = true) ||
            clean.startsWith("Imam Muslim", ignoreCase = true) ||
            clean.startsWith("Anas berkata:", ignoreCase = true) ||
            clean.startsWith("Catatan:", ignoreCase = true) ||
            clean.startsWith("Keterangan:", ignoreCase = true)

    if (isCommentary) return ParagraphKind.COMMENTARY

    // 2. Cek Jalur Sanad (diawali frasa sanad atau mengandung perawi siku pada awal hadits)
    val startsWithSanad = clean.startsWith("Telah menceritakan", ignoreCase = true) ||
            clean.startsWith("Telah mengabarkan", ignoreCase = true) ||
            clean.startsWith("Diriwayatkan dari", ignoreCase = true) ||
            clean.startsWith("Menceritakan kepada kami", ignoreCase = true)

    val hasPerawiBrackets = clean.contains("[") && clean.contains("]")

    if (startsWithSanad || (index == 0 && hasPerawiBrackets)) {
        return ParagraphKind.SANAD
    }

    // 3. Paragraf Sabda / Narasi Matan Inti
    return ParagraphKind.MATAN
}

/**
 * Card Terjemahan Indonesia
 * Merender setiap paragraf secara berurutan dan cerdas:
 * - Sanad: Ditata rapi dengan highlight perawi semi-bold & interaktif
 * - Matan: Ditata dalam bingkai aksen lembut tanpa label palsu
 * - Komentar: Ditata dalam kartu catatan penjelas kitab
 */
@Composable
private fun IndonesianTranslationCard(
    translationText: String,
    fontSettings: FontSettingsState,
    searchKeywords: List<String> = emptyList(),
    isSearchMode: Boolean = false,
    onRawiClick: (String) -> Unit,
    isAudioPlaying: Boolean = false,
    onPlayAudio: () -> Unit = {}
) {
    val textColors = HadithTextDefaults.colors()
    val currentIndonesianFont = remember(fontSettings.indonesianFontName) {
        getIndonesianFontFamily(fontSettings.indonesianFontName)
    }

    val paragraphBlocks = remember(translationText) {
        translationText.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Terjemahan: Judul & tombol audio pembacaan terjemahan
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Terjemahan Indonesia",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HadithAudioActionButton(
                    isPlaying = isAudioPlaying,
                    label = "Bacakan",
                    onClick = onPlayAudio
                )
            }

            if (paragraphBlocks.isEmpty()) {
                val formattedText = remember(translationText, searchKeywords, isSearchMode, textColors) {
                    formatHadithText(
                        text = translationText,
                        keywords = searchKeywords,
                        isSearchMode = isSearchMode,
                        colors = textColors
                    )
                }
                ClickableText(
                    text = formattedText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = fontSettings.indonesianFontSize.sp,
                        lineHeight = (fontSettings.indonesianFontSize * 1.58f).sp,
                        fontFamily = currentIndonesianFont,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { offset ->
                        formattedText.getStringAnnotations("PERAWI_NAME", offset, offset)
                            .firstOrNull()?.let { annotation ->
                                onRawiClick(annotation.item)
                            }
                    }
                )
            } else {
                paragraphBlocks.forEachIndexed { index, paragraph ->
                    val kind = classifyParagraph(paragraph, index)
                    val formattedBlock = remember(paragraph, searchKeywords, isSearchMode, textColors) {
                        formatHadithText(
                            text = paragraph,
                            keywords = searchKeywords,
                            isSearchMode = isSearchMode,
                            colors = textColors
                        )
                    }

                    when (kind) {
                        ParagraphKind.SANAD -> {
                            // Blok Jalur Sanad
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = if (index == 0) "🔗 Jalur Sanad Utama:" else "🔗 Jalur Sanad Penguat:",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                                )

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ClickableText(
                                        text = formattedBlock,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontStyle = FontStyle.Normal,
                                            fontSize = (fontSettings.indonesianFontSize * 0.95f).sp,
                                            lineHeight = (fontSettings.indonesianFontSize * 1.52f).sp,
                                            fontFamily = currentIndonesianFont,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.padding(12.dp),
                                        onClick = { offset ->
                                            formattedBlock.getStringAnnotations("PERAWI_NAME", offset, offset)
                                                .firstOrNull()?.let { annotation ->
                                                    onRawiClick(annotation.item)
                                                }
                                        }
                                    )
                                }
                            }
                        }

                        ParagraphKind.MATAN -> {
                            // Blok Sabda / Narasi Matan Inti (Kotak Aksen Lembut)
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(14.dp)
                                        .height(IntrinsicSize.Min)
                                ) {
                                    // Aksen Garis Vertikal Halus di Sisi Kiri
                                    Box(
                                        modifier = Modifier
                                            .width(3.5.dp)
                                            .fillMaxHeight()
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(2.dp)
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))

                                    ClickableText(
                                        text = formattedBlock,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = fontSettings.indonesianFontSize.sp,
                                            lineHeight = (fontSettings.indonesianFontSize * 1.58f).sp,
                                            fontFamily = currentIndonesianFont,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { offset ->
                                            formattedBlock.getStringAnnotations("PERAWI_NAME", offset, offset)
                                                .firstOrNull()?.let { annotation ->
                                                    onRawiClick(annotation.item)
                                                }
                                        }
                                    )
                                }
                            }
                        }

                        ParagraphKind.COMMENTARY -> {
                            // Blok Catatan / Ta'liq Imam / Riwayat Tambahan
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "📝 Keterangan / Ta'liq Imam:",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                    ClickableText(
                                        text = formattedBlock,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = fontSettings.indonesianFontSize.sp,
                                            lineHeight = (fontSettings.indonesianFontSize * 1.55f).sp,
                                            fontFamily = currentIndonesianFont,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { offset ->
                                            formattedBlock.getStringAnnotations("PERAWI_NAME", offset, offset)
                                                .firstOrNull()?.let { annotation ->
                                                    onRawiClick(annotation.item)
                                                }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
