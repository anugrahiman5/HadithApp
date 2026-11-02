package com.nusa.hadithapp.ui.components.dialogs

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nusa.hadithapp.domain.model.PerawiProfile
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.domain.model.ScholarComment
import com.nusa.hadithapp.domain.model.getQualityZoneInfo
import com.nusa.hadithapp.ui.detail.PerawiUiState
import com.nusa.hadithapp.ui.detail.ScholarCommentUiState
import com.nusa.hadithapp.util.BookSlugUtil
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private data class PerawiHadithSelection(
    val bookSlug: String,
    val bookName: String,
    val numbers: List<Int>
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PerawiDetailDialog(
    perawi: SanadPerawi,
    perawiProfileState: PerawiUiState,
    scholarCommentState: ScholarCommentUiState,
    onDismiss: () -> Unit,
    onFetchBookHadithNumbers: (suspend (bookSlug: String) -> List<Int>)? = null,
    onNavigateToHadith: ((bookSlug: String, hadithNumber: Int, kodeRawi: Int, rawiName: String, numbers: List<Int>) -> Unit)? = null,
    onBrowseHadiths: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showAllBooks by remember { mutableStateOf(false) }
    var loadingBookSlug by remember { mutableStateOf<String?>(null) }
    var hadithSelection by remember { mutableStateOf<PerawiHadithSelection?>(null) }

    val rawiName = when (perawiProfileState) {
        is PerawiUiState.Success -> perawiProfileState.profile.nama ?: perawi.name
        else -> perawi.name
    }

    val onShareClick: () -> Unit = {
        val prof = (perawiProfileState as? PerawiUiState.Success)?.profile
        val textToShare = buildString {
            appendLine("Profil Perawi Hadits: $rawiName")
            appendLine("Kalangan: ${prof?.kalangan ?: perawi.kalangan}")
            if (!prof?.nasab.isNullOrBlank()) appendLine("Nasab: ${prof.nasab}")
            if (!prof?.kuniyah.isNullOrBlank()) appendLine("Kunyah: ${prof.kuniyah}")
            if (!prof?.tahunWafat.isNullOrBlank()) appendLine("Wafat: ${prof.tahunWafat}")
            appendLine("Derajat Kredibilitas: Level ${prof?.quality ?: perawi.quality}")
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Bagikan Profil Perawi"))
    }

    val handleBookClick: (String, String, Int) -> Unit = { bookSlug, bookName, count ->
        if (count <= 0) {
            Toast.makeText(context, "Tidak ada riwayat perawi dalam $bookName", Toast.LENGTH_SHORT).show()
        } else if (onFetchBookHadithNumbers != null && onNavigateToHadith != null) {
            scope.launch {
                loadingBookSlug = bookSlug
                try {
                    val numbers = onFetchBookHadithNumbers(bookSlug)
                    loadingBookSlug = null
                    if (numbers.isEmpty()) {
                        Toast.makeText(context, "Tidak dapat memuat hadits dari $bookName", Toast.LENGTH_SHORT).show()
                    } else if (numbers.size == 1) {
                        onNavigateToHadith(bookSlug, numbers.first(), perawi.kodeRawi, rawiName, numbers)
                        onDismiss()
                    } else {
                        hadithSelection = PerawiHadithSelection(bookSlug, bookName, numbers)
                    }
                } catch (e: Exception) {
                    loadingBookSlug = null
                    Toast.makeText(context, "Gagal memuat data hadits: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "$count hadits dalam $bookName", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profil Perawi Hadis",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Profil perawi disimpan ke catatan", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkBorder,
                                contentDescription = "Simpan",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = onShareClick) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Bagikan",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (onBrowseHadiths != null) {
                                    onBrowseHadiths(rawiName)
                                    onDismiss()
                                } else {
                                    val prof = (perawiProfileState as? PerawiUiState.Success)?.profile
                                    val firstBook = listOf(
                                        "ShahihBukhari" to (prof?.rBukhari ?: 0),
                                        "ShahihMuslim" to (prof?.rMuslim ?: 0),
                                        "SunanAbuDaud" to (prof?.rAbuDaud ?: 0),
                                        "SunanTirmidzi" to (prof?.rTirmidzi ?: 0),
                                        "SunanNasai" to (prof?.rNasai ?: 0),
                                        "SunanIbnuMajah" to (prof?.rIbnuMajah ?: 0),
                                        "MusnadAhmad" to (prof?.rAhmad ?: 0),
                                        "MuwathaMalik" to (prof?.rMalik ?: 0),
                                        "SunanDarimi" to (prof?.rDarimi ?: 0)
                                    ).find { it.second > 0 }

                                    if (firstBook != null) {
                                        handleBookClick(firstBook.first, BookSlugUtil.getOfficialBookName(firstBook.first), firstBook.second)
                                    } else {
                                        Toast.makeText(context, "Tidak ada riwayat hadits yang terindeks", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Jelajahi Hadis Perawi Ini",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Surface(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Tutup",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (perawiProfileState) {
                    is PerawiUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is PerawiUiState.Success -> {
                        val prof = perawiProfileState.profile
                        val qualityLevel = prof.quality ?: perawi.quality

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 1. Kartu Identitas & Biografi Utama
                            PerawiIdentityCard(
                                profile = prof,
                                fallbackName = perawi.name,
                                fallbackKalangan = perawi.kalangan,
                                quality = qualityLevel
                            )

                            // 2. Kartu Kuantitas Riwayat per Kitab (Kutubus Sittah & 9 Kitab)
                            HadithQuantitySection(
                                profile = prof,
                                showAllBooks = showAllBooks,
                                loadingBookSlug = loadingBookSlug,
                                onToggleShowAll = { showAllBooks = !showAllBooks },
                                onBookClick = handleBookClick
                            )

                            // 3. Kartu Komentar Ulama (Jarh wa Ta'dil)
                            ScholarCommentsSection(scholarCommentState = scholarCommentState)

                            // Footer Branding
                            Text(
                                text = "✧ Ensiklopedia Rijalul Hadits ✧",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 12.dp)
                            )
                        }
                    }

                    is PerawiUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("⚠️", fontSize = 42.sp)
                                Text(
                                    text = "Gagal Memuat Profil Perawi",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = perawiProfileState.message,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                                Button(onClick = onDismiss) {
                                    Text("Tutup")
                                }
                            }
                        }
                    }

                    PerawiUiState.Empty -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet untuk Pemilihan Nomor Hadits Jika Perawi Memiliki > 1 Riwayat
    hadithSelection?.let { selection ->
        ModalBottomSheet(
            onDismissRequest = { hadithSelection = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .heightIn(max = 480.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Sheet
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hadits di ${selection.bookName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${rawiName} meriwayatkan ${selection.numbers.size} hadits dalam kitab ini",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Tombol Pintas Buka Hadits Pertama
                Button(
                    onClick = {
                        val targetNumber = selection.numbers.first()
                        val targetSlug = selection.bookSlug
                        hadithSelection = null
                        onNavigateToHadith?.invoke(targetSlug, targetNumber, perawi.kodeRawi, rawiName, selection.numbers)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Buka Hadits Pertama (#${selection.numbers.first()})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Daftar Seluruh Nomor Hadits
                Text(
                    text = "Atau pilih nomor hadits spesifik:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    selection.numbers.forEach { num ->
                        SuggestionChip(
                            onClick = {
                                val targetSlug = selection.bookSlug
                                hadithSelection = null
                                onNavigateToHadith?.invoke(targetSlug, num, perawi.kodeRawi, rawiName, selection.numbers)
                                onDismiss()
                            },
                            label = {
                                Text(
                                    text = "#$num",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Kartu Identitas Utama: Nama, Quality Level Pill, Nasab Lengkap, & Grid Info 2x2
 */
@Composable
private fun PerawiIdentityCard(
    profile: PerawiProfile,
    fallbackName: String,
    fallbackKalangan: String,
    quality: Int
) {
    val zone = getQualityZoneInfo(quality)
    val displayName = profile.nama ?: fallbackName
    val displayKalangan = profile.kalangan?.takeIf { it.isNotBlank() } ?: fallbackKalangan.takeIf { it.isNotBlank() } ?: "-"

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nama Latin Perawi
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Badge Tingkat Kredibilitas
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = zone.color.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, zone.color.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = zone.color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LEVEL $quality: ${zone.groupName.uppercase()}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = zone.color
                    )
                }
            }

            // Kotak Nasab Lengkap (Ditampilkan jika tersedia di data API)
            if (!profile.nasab.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "NASAB LENGKAP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = profile.nasab,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Grid Informasi Pokok 2x2
            val tempatHidup = when {
                !profile.negeriHidup.isNullOrBlank() && !profile.negeriWafat.isNullOrBlank() && profile.negeriHidup != profile.negeriWafat ->
                    "${profile.negeriHidup} & ${profile.negeriWafat}"
                !profile.negeriHidup.isNullOrBlank() -> profile.negeriHidup
                !profile.negeriWafat.isNullOrBlank() -> profile.negeriWafat
                else -> "-"
            }

            val tahunWafat = profile.tahunWafat?.takeIf { it.isNotBlank() }?.let {
                if (it.endsWith("H", ignoreCase = true)) it else "$it H"
            } ?: "-"

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoGridCell(
                        label = "Kunyah",
                        value = profile.kuniyah?.takeIf { it.isNotBlank() } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    InfoGridCell(
                        label = "Kalangan",
                        value = displayKalangan,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoGridCell(
                        label = "Tempat Hidup",
                        value = tempatHidup,
                        modifier = Modifier.weight(1f)
                    )
                    InfoGridCell(
                        label = "Tahun Wafat",
                        value = tahunWafat,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoGridCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Section Kuantitas Riwayat per Kitab (Grid 2x2 untuk Kitab Utama + Ekspansi 5 Kitab)
 */
@Composable
private fun HadithQuantitySection(
    profile: PerawiProfile,
    showAllBooks: Boolean,
    loadingBookSlug: String?,
    onToggleShowAll: () -> Unit,
    onBookClick: (bookSlug: String, bookName: String, count: Int) -> Unit
) {
    val numberFormatter = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")) }

    val totalRiwayat = (profile.rBukhari ?: 0) +
            (profile.rMuslim ?: 0) +
            (profile.rAbuDaud ?: 0) +
            (profile.rTirmidzi ?: 0) +
            (profile.rNasai ?: 0) +
            (profile.rIbnuMajah ?: 0) +
            (profile.rAhmad ?: 0) +
            (profile.rMalik ?: 0) +
            (profile.rDarimi ?: 0)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Judul Section
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
                    imageVector = Icons.Outlined.AutoStories,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Kuantitas Riwayat per Kitab",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "Kutubus Sittah",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        // Grid 2x2 Kitab Utama
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BookCountCard(
                letter = "خ",
                count = numberFormatter.format(profile.rBukhari ?: 0),
                rawCount = profile.rBukhari ?: 0,
                bookName = "Shahih Bukhari",
                isLoading = loadingBookSlug == "ShahihBukhari",
                onClick = { onBookClick("ShahihBukhari", "Shahih Bukhari", profile.rBukhari ?: 0) },
                modifier = Modifier.weight(1f)
            )
            BookCountCard(
                letter = "م",
                count = numberFormatter.format(profile.rMuslim ?: 0),
                rawCount = profile.rMuslim ?: 0,
                bookName = "Shahih Muslim",
                isLoading = loadingBookSlug == "ShahihMuslim",
                onClick = { onBookClick("ShahihMuslim", "Shahih Muslim", profile.rMuslim ?: 0) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BookCountCard(
                letter = "د",
                count = numberFormatter.format(profile.rAbuDaud ?: 0),
                rawCount = profile.rAbuDaud ?: 0,
                bookName = "Sunan Abu Daud",
                isLoading = loadingBookSlug == "SunanAbuDaud",
                onClick = { onBookClick("SunanAbuDaud", "Sunan Abu Daud", profile.rAbuDaud ?: 0) },
                modifier = Modifier.weight(1f)
            )
            BookCountCard(
                letter = "ت",
                count = numberFormatter.format(profile.rTirmidzi ?: 0),
                rawCount = profile.rTirmidzi ?: 0,
                bookName = "Sunan Tirmidzi",
                isLoading = loadingBookSlug == "SunanTirmidzi",
                onClick = { onBookClick("SunanTirmidzi", "Sunan Tirmidzi", profile.rTirmidzi ?: 0) },
                modifier = Modifier.weight(1f)
            )
        }

        // Ekspansi 5 Kitab Tambahan
        AnimatedVisibility(
            visible = showAllBooks,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BookCountCard(
                        letter = "ن",
                        count = numberFormatter.format(profile.rNasai ?: 0),
                        rawCount = profile.rNasai ?: 0,
                        bookName = "Sunan An-Nasa'i",
                        isLoading = loadingBookSlug == "SunanNasai",
                        onClick = { onBookClick("SunanNasai", "Sunan An-Nasa'i", profile.rNasai ?: 0) },
                        modifier = Modifier.weight(1f)
                    )
                    BookCountCard(
                        letter = "ق",
                        count = numberFormatter.format(profile.rIbnuMajah ?: 0),
                        rawCount = profile.rIbnuMajah ?: 0,
                        bookName = "Sunan Ibnu Majah",
                        isLoading = loadingBookSlug == "SunanIbnuMajah",
                        onClick = { onBookClick("SunanIbnuMajah", "Sunan Ibnu Majah", profile.rIbnuMajah ?: 0) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BookCountCard(
                        letter = "حم",
                        count = numberFormatter.format(profile.rAhmad ?: 0),
                        rawCount = profile.rAhmad ?: 0,
                        bookName = "Musnad Ahmad",
                        isLoading = loadingBookSlug == "MusnadAhmad",
                        onClick = { onBookClick("MusnadAhmad", "Musnad Ahmad", profile.rAhmad ?: 0) },
                        modifier = Modifier.weight(1f)
                    )
                    BookCountCard(
                        letter = "ط",
                        count = numberFormatter.format(profile.rMalik ?: 0),
                        rawCount = profile.rMalik ?: 0,
                        bookName = "Muwatha' Malik",
                        isLoading = loadingBookSlug == "MuwathaMalik",
                        onClick = { onBookClick("MuwathaMalik", "Muwatha' Malik", profile.rMalik ?: 0) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BookCountCard(
                        letter = "مي",
                        count = numberFormatter.format(profile.rDarimi ?: 0),
                        rawCount = profile.rDarimi ?: 0,
                        bookName = "Sunan Ad-Darimi",
                        isLoading = loadingBookSlug == "SunanDarimi",
                        onClick = { onBookClick("SunanDarimi", "Sunan Ad-Darimi", profile.rDarimi ?: 0) },
                        modifier = Modifier.weight(0.5f)
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                }
            }
        }

        // Tombol Lihat Seluruh Hadis yang Diriwayatkan
        OutlinedButton(
            onClick = onToggleShowAll,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Text(
                text = if (showAllBooks) {
                    "Sembunyikan 5 Kitab Lainnya"
                } else {
                    "Lihat Seluruh Hadis yang Diriwayatkan (${numberFormatter.format(totalRiwayat)} hadis)"
                },
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun BookCountCard(
    letter: String,
    count: String,
    rawCount: Int,
    bookName: String,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (rawCount > 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
        ),
        border = BorderStroke(
            1.dp,
            if (rawCount > 0) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (rawCount > 0) 1.5.dp else 0.dp),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !isLoading) { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Header baris atas: Badge Inisial Kitab & Panah / Loading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (rawCount > 0) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.size(26.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = letter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (rawCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (rawCount > 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Jumlah Hadits
            Text(
                text = count,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (rawCount > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )

            // Nama Kitab
            Text(
                text = bookName,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = if (rawCount > 0) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Section Komentar Ulama (Jarh wa Ta'dil)
 */
@Composable
private fun ScholarCommentsSection(scholarCommentState: ScholarCommentUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Judul Section
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
                    imageVector = Icons.Outlined.RateReview,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Komentar Ulama (Jarh wa Ta'dil)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "Konsensus",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        when (scholarCommentState) {
            is ScholarCommentUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }

            is ScholarCommentUiState.Success -> {
                val comments = scholarCommentState.comments
                if (comments.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Belum ada catatan komentar ulama (jarh wa ta'dil) yang terindeks untuk perawi ini.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    comments.forEach { item ->
                        ScholarCommentCard(item = item)
                    }
                }
            }

            is ScholarCommentUiState.Error -> {
                Text(
                    text = scholarCommentState.message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            ScholarCommentUiState.Empty -> {
                Text(
                    text = "Data komentar tidak tersedia.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun ScholarCommentCard(item: ScholarComment) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Baris Atas: Nama Ulama & Tag Singkat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = item.ulama.ifBlank { "Ulama Hadits" },
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Tag Komentar (Label Penilaian Singkat)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = item.komentar.take(18).trim(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Isi Komentar (Kutipan dengan Aksen Garis Kiri)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight()
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(1.5.dp)
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "“${item.komentar}”",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
