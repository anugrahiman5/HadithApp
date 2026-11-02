package com.nusa.hadithapp.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.HadithBook
import com.nusa.hadithapp.ui.explore.components.ChapterItemCard
import com.nusa.hadithapp.ui.explore.components.HeroBookOverviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterBrowserScreen(
    viewModel: ChapterBrowserViewModel,
    onBackClick: () -> Unit,
    onNavigateToHadith: (bookSlug: String, hadithNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBookPickerSheet by remember { mutableStateOf(false) }
    var showJumpDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Reset posisi scroll ke atas saat kitab berganti
    LaunchedEffect(uiState.selectedBook) {
        listState.scrollToItem(0)
    }

    val filterChips = listOf("Semua Bab", "Paling Sering Dibaca", "Ibadah", "Muamalah", "Adab & Akhlaq")

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Column {
                        Text("Daftar Kitab & Bab", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${uiState.selectedBook.shortName} • ${uiState.chapters.size} Kitab Bab",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Tombol Dropdown Ganti Kitab
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(0.6f),
                        modifier = Modifier.clickable { showBookPickerSheet = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(15.dp), tint = MaterialTheme.colorScheme.primary)
                            Text("Ganti Kitab", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            if (uiState.chapters.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { showJumpDialog = true },
                    icon = { Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null) },
                    text = { Text("Lompat ke No. Bab (1-${uiState.chapters.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(24.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 1. HORIZONTAL BOOK TABS (Khusus Kitab)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(HadithBook.entries) { book ->
                    val isSelected = uiState.selectedBook == book
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectBook(book) },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(book.initial, fontWeight = FontWeight.Bold)
                                Text(book.shortName)
                                if (isSelected) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onPrimary))
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // 2. DAFTAR KONTEN SCROLLABLE
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // HERO BOOK OVERVIEW CARD
                item {
                    HeroBookOverviewCard(
                        book = uiState.selectedBook,
                        totalChapters = uiState.chapters.size,
                        lastRead = uiState.lastRead,
                        onResumeReadClick = { hadithNumber ->
                            onNavigateToHadith(uiState.selectedBook.slug, hadithNumber)
                        }
                    )
                }

                // SEARCH BAR BAB
                item {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Cari nama bab atau nomor hadis (e.g. Iman, 50)...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // FILTER CHIP BAB
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filterChips) { chip ->
                            val isSelected = uiState.activeFilterChip == chip
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectFilterChip(chip) },
                                label = {
                                    val countText = if (chip == "Semua Bab") " (${uiState.chapters.size})" else ""
                                    Text("$chip$countText", fontSize = 12.sp)
                                },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }
                }

                // DAFTAR KARTU BAB
                if (uiState.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (uiState.filteredChapters.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text("Tidak ada bab yang ditemukan.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    items(uiState.filteredChapters, key = { "${uiState.selectedBook.slug}_${it.chapterNumber}" }) { chapter ->
                        ChapterItemCard(
                            item = chapter,
                            onClick = {
                                // 🟢 Klik bab -> Langsung buka nomor hadits awal bab tersebut!
                                onNavigateToHadith(uiState.selectedBook.slug, chapter.startHadithNumber)
                            }
                        )
                    }
                }
            }
        }
    }

    // 🟢 DIALOG LOMPAT KE NOMOR BAB
    if (showJumpDialog) {
        var inputNumber by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = { Text("Lompat ke Nomor Bab", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = inputNumber,
                    onValueChange = { inputNumber = it },
                    label = { Text("Nomor Bab (1 - ${uiState.chapters.size})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val babNum = inputNumber.toIntOrNull()
                        val target = uiState.chapters.find { it.chapterNumber == babNum }
                        if (target != null) {
                            onNavigateToHadith(uiState.selectedBook.slug, target.startHadithNumber)
                            showJumpDialog = false
                        }
                    }
                ) {
                    Text("Lompat")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) { Text("Batal") }
            }
        )
    }

    // 🟢 BOTTOM SHEET GANTI KITAB
    if (showBookPickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBookPickerSheet = false },
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pilih Kitab Hadits", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(modifier = Modifier.height(400.dp)) {
                    items(HadithBook.entries) { book ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectBook(book)
                                    showBookPickerSheet = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = uiState.selectedBook == book, onClick = null)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(book.fullName, fontSize = 15.sp, fontWeight = if (uiState.selectedBook == book) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }
    }
}