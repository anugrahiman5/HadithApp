package com.nusa.hadithapp.ui.home

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.Hadith
import com.nusa.hadithapp.domain.model.HadithBook
import com.nusa.hadithapp.domain.model.HadithCategory
import com.nusa.hadithapp.domain.model.HadithTheme
import com.nusa.hadithapp.ui.classification.category.AllCategoriesBottomSheet
import com.nusa.hadithapp.ui.classification.theme.AllThemesBottomSheet
import com.nusa.hadithapp.ui.detail.components.QuoteCardData
import com.nusa.hadithapp.ui.detail.components.QuoteCardDialog
import com.nusa.hadithapp.ui.home.components.BookGridSection
import com.nusa.hadithapp.ui.home.components.ExploreThemeSection
import com.nusa.hadithapp.ui.home.components.HadithOfTheDayCard
import com.nusa.hadithapp.ui.home.components.HomeHeader
import com.nusa.hadithapp.ui.home.components.HomeSearchBar
import com.nusa.hadithapp.ui.home.components.ImamBiographyCard
import com.nusa.hadithapp.ui.home.components.LastReadCard
import com.nusa.hadithapp.ui.home.components.SanadInspectorCard
import com.nusa.hadithapp.ui.home.components.TerminologySection
import com.nusa.hadithapp.ui.search.components.MultiBookFilterBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onBookClick: (HadithBook) -> Unit,
    onContinueReadClick: (LastReadHadith) -> Unit,
    onHadithOfTheDayClick: (bookSlug: String, number: Int) -> Unit = { _, _ -> },
    onThemeClick: (HadithTheme) -> Unit = {},
    onCategoryClick: (HadithCategory) -> Unit = {},
    onSeeAllThemeClick: () -> Unit = {},
    onSeeAllCategoriesClick: () -> Unit = {},
    onSearchTriggered: (String) -> Unit,
    onRawiSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBiographyClick: (String) -> Unit = {},
    onMusthalahClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val targetSearchBooks by viewModel.targetSearchBooks.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var showAllThemeSheet by remember { mutableStateOf(false) }
    var showAllCategoriesSheet by remember { mutableStateOf(false) }
    var showMultiBookFilterSheet by remember { mutableStateOf(false) }
    var showHomeShareSheet by remember { mutableStateOf(false) }
    var showHomeQuoteDialog by remember { mutableStateOf(false) }

    val shareDailyHadithAsText = {
        uiState.hadithOfTheDay?.let { hadithOfTheDay ->
            val shareText = "✨ Hadits Hari Ini\n\n${hadithOfTheDay.arabicText}\n\n\"${hadithOfTheDay.translation}\"\n(${hadithOfTheDay.bookName} no. ${hadithOfTheDay.number} • ${hadithOfTheDay.grade})"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Bagikan Hadits")
            context.startActivity(shareIntent)
        }
    }

    val voiceSearchLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                searchQuery = spokenText
                onSearchTriggered(spokenText)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Bar & Salam Ramah "Assalamu'alaikum" + Tanggal Hijriah
        item {
            HomeHeader(
                userName = uiState.userName,
                hijriDate = uiState.hijriDate,
                onSettingsClick = onSettingsClick,
                onProfileClick = onProfileClick
            )
        }

        // 2. Search Bar: Input teks, Mic & Filter Tune
        item {
            HomeSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearchTriggered = {
                    if (searchQuery.isNotBlank()) {
                        onSearchTriggered(searchQuery)
                    }
                },
                onFilterClick = {
                    showMultiBookFilterSheet = true
                },
                onMicClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID")
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Ucapkan kata kunci hadis atau perawi...")
                    }
                    try {
                        voiceSearchLauncher.launch(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Fitur input suara tidak tersedia pada perangkat ini", Toast.LENGTH_SHORT).show()
                    }
                },
                selectedBookCount = targetSearchBooks.size
            )
        }

        // 3. Hadits Pilihan Hari Ini (Inspirasi harian di atas)
        item {
            uiState.hadithOfTheDay?.let { hadithOfTheDay ->
                HadithOfTheDayCard(
                    hadith = hadithOfTheDay,
                    isRefreshing = uiState.isDailyHadithLoading,
                    onClick = {
                        onHadithOfTheDayClick(hadithOfTheDay.bookSlug, hadithOfTheDay.number)
                    },
                    onShareClick = {
                        showHomeShareSheet = true
                    },
                    onRefreshClick = {
                        viewModel.refreshDailyHadith()
                    }
                )
            }
        }

        // 4. Last Read / Progres Bacaan Card
        item {
            val fallbackLastRead = uiState.lastRead ?: LastReadHadith(
                bookSlug = "bukhari",
                bookName = "Shahih Bukhari",
                number = 3,
                title = "Bagaimana permulaan wahyu diturunkan",
                progressPercent = 0.04f,
                kitab = "Kitab Permulaan Wahyu"
            )

            LastReadCard(
                lastRead = fallbackLastRead,
                onClick = { onContinueReadClick(fallbackLastRead) }
            )
        }

        // 5. Jelajah Tema: Filter Topik Amalan (Pill Chips)
        item {
            ExploreThemeSection(
                onThemeClick = { theme ->
                    onThemeClick(theme)
                },
                onSeeAllClick = { showAllThemeSheet = true }
            )
        }

        // 6. Terminologi Hadits: Filter Status Keshahihan (Card Chips)
        item {
            TerminologySection(
                onCategoryClick = { category ->
                    onCategoryClick(category)
                },
                onSeeAllClick = { showAllCategoriesSheet = true },
                onMusthalahClick = onMusthalahClick
            )
        }

        // 7. Pemeriksa Sanad & Rawi Banner
        item {
            SanadInspectorCard(
                onClick = onRawiSearchClick
            )
        }

        // 7.5 Biografi 10 Imam Hadis Banner
        item {
            ImamBiographyCard(
                onClick = { onBiographyClick("Bukhari") }
            )
        }

        // 8. 14 Kitab Hadis: Katalog Pustaka Utama (2-Column Grid)
        item {
            BookGridSection(
                books = uiState.books,
                onBookClick = onBookClick
            )
        }
    }

    // Bottom Sheet: Semua Tema
    if (showAllThemeSheet) {
        AllThemesBottomSheet(
            onThemeClick = { theme ->
                onThemeClick(theme)
            },
            onDismiss = { showAllThemeSheet = false }
        )
    }

    // Bottom Sheet: Semua Kategori / Terminologi
    if (showAllCategoriesSheet) {
        AllCategoriesBottomSheet(
            onCategoryClick = { category ->
                onCategoryClick(category)
            },
            onDismiss = { showAllCategoriesSheet = false }
        )
    }

    // Bottom Sheet: Filter Multi-Kitab
    if (showMultiBookFilterSheet) {
        MultiBookFilterBottomSheet(
            selectedCanonicalSlugs = targetSearchBooks,
            onApplyFilter = { newSelected ->
                viewModel.setTargetSearchBooks(newSelected)
                if (searchQuery.isNotBlank()) {
                    onSearchTriggered(searchQuery)
                }
            },
            onDismiss = { showMultiBookFilterSheet = false }
        )
    }

    // Bottom Sheet: Opsi Bagikan Hadis Hari Ini
    if (showHomeShareSheet && uiState.hadithOfTheDay != null) {
        val daily = uiState.hadithOfTheDay!!
        ModalBottomSheet(
            onDismissRequest = { showHomeShareSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Bagikan Hadits Hari Ini",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pilih format pembagian hadits yang Anda inginkan:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Opsi 1: Kartu Gambar Quote
                Card(
                    onClick = {
                        showHomeShareSheet = false
                        showHomeQuoteDialog = true
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Kartu Gambar Quote",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "WA Status 9:16",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Format vertikal 9:16 atau 1:1 Feed dengan tema warna estetis untuk dibagikan ke media sosial",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Opsi 2: Teks Lengkap
                Card(
                    onClick = {
                        showHomeShareSheet = false
                        shareDailyHadithAsText()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Teks Lengkap Hadits",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Salin & bagikan teks Arab, terjemahan Indonesia, nama kitab, dan nomor hadis",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog Quote Card Hadits Hari Ini
    if (showHomeQuoteDialog && uiState.hadithOfTheDay != null) {
        val daily = uiState.hadithOfTheDay!!
        QuoteCardDialog(
            data = QuoteCardData(
                bookName = daily.bookName,
                number = daily.number,
                arabicText = daily.arabicText,
                translation = daily.translation,
                grade = daily.grade
            ),
            onDismiss = { showHomeQuoteDialog = false }
        )
    }
}
