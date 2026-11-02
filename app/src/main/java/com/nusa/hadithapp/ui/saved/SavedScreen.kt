package com.nusa.hadithapp.ui.saved

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.ui.saved.components.BookmarkItemCard
import com.nusa.hadithapp.ui.saved.components.NoteStudyCard
import com.nusa.hadithapp.util.FontSettingsBottomSheet
import com.nusa.hadithapp.util.PdfExporter
import com.nusa.hadithapp.util.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onNavigateToDetail: (bookSlug: String, number: Int) -> Unit,
    onNavigateToEditor: (noteId: String?, bookSlug: String?, number: Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val fontSettings by viewModel.fontSettings.collectAsState()
    val bookmarkListState = rememberLazyListState()
    val noteListState = rememberLazyListState()
    var showSortDialog by remember { mutableStateOf(false) }
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var showFontBottomSheet by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var selectedBookmarkForMove by remember { mutableStateOf<Bookmark?>(null) }

    val currentSortLabel = remember(uiState.sortOrder, uiState.selectedTab) {
        when (uiState.sortOrder) {
            SortOrder.NEWEST -> if (uiState.selectedTab == 0) "Terakhir Disimpan" else "Terakhir Diedit"
            SortOrder.OLDEST -> if (uiState.selectedTab == 0) "Terlama Disimpan" else "Terlama Diedit"
            SortOrder.NAME_TITLE -> if (uiState.selectedTab == 0) "Nama Kitab (A-Z)" else "Judul Catatan (A-Z)"
            SortOrder.HADITH_NUMBER -> "Nomor Hadits (1-N)"
        }
    }
    Scaffold(
        floatingActionButton = {
            if (uiState.selectedTab == 1) {
                ExtendedFloatingActionButton(
                    onClick = { onNavigateToEditor(null, null, null) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Tulis Kajian", fontWeight = FontWeight.Bold) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 1. TOP HEADER & ICONS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Simpan",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = "Katalog Pribadi",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { showFontBottomSheet = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Outlined.TextFields,
                                contentDescription = "Font",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { showCreateFolderDialog = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.FolderSpecial,
                                contentDescription = "Folder",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { viewModel.toggleSearchBar() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Cari",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Text(
                    text = "Koleksi Bookmark & Catatan Kajian",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (uiState.isSearchBarVisible) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Cari dalam hadits/catatan tersimpan...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Hapus")
                                }
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            // 2. SEGMENTED DUAL-TAB SWITCHER (Bookmark vs Catatan)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val isTab0 = uiState.selectedTab == 0
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isTab0) MaterialTheme.colorScheme.surface else Color.Transparent,
                        shadowElevation = if (isTab0) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.selectTab(0) }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = if (isTab0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bookmark Berjudul",
                                fontSize = 12.sp,
                                fontWeight = if (isTab0) FontWeight.Bold else FontWeight.Medium,
                                color = if (isTab0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Badge(containerColor = if (isTab0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant) {
                                Text(
                                    "${uiState.totalBookmarksCount}",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    val isTab1 = uiState.selectedTab == 1
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isTab1) MaterialTheme.colorScheme.surface else Color.Transparent,
                        shadowElevation = if (isTab1) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.selectTab(1) }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = if (isTab1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Catatan Kajian",
                                fontSize = 12.sp,
                                fontWeight = if (isTab1) FontWeight.Bold else FontWeight.Medium,
                                color = if (isTab1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Badge(containerColor = if (isTab1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant) {
                                Text(
                                    "${uiState.totalNotesCount}",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // 3. HORIZONTAL FILTER CHIPS (Folder Koleksi)
            val chips =
                if (uiState.selectedTab == 0) uiState.availableFolderTags else uiState.availableNoteCategories
            val currentTag =
                if (uiState.selectedTab == 0) uiState.bookmarkFilterTag else uiState.noteFilterTag
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(chips) { tag ->
                    val isSelected = currentTag.equals(tag, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectFilterTag(tag) },
                        label = { Text(tag, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. ACTION BAR (Urutan & Kelola Koleksi)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { showSortDialog = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Urutan: $currentSortLabel",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = if (uiState.selectedTab == 0) "🗂 Kelola Koleksi" else "🗂 Kelola Binder",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showCreateFolderDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                state = if (uiState.selectedTab == 0) bookmarkListState
                else noteListState,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 5. LIST KONTEN KARTU
                if (uiState.selectedTab == 0) {
                    if (uiState.bookmarks.isEmpty()) {
                        item {
                            EmptySavedState(
                                title = "Belum Ada Hadits Tersimpan",
                                description = if (uiState.searchQuery.isNotBlank()) "Tidak ada hadits yang cocok dengan kata kunci \"${uiState.searchQuery}\"" else "Saat membaca hadits, tekan tombol Bookmark di bawah untuk menyimpannya ke koleksi ini."
                            )
                        }
                    } else {
                        items(uiState.bookmarks, key = { it.id }) { bookmark ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                BookmarkItemCard(
                                    bookmark = bookmark,
                                    fontSettings = fontSettings,
                                    onClick = {
                                        onNavigateToDetail(
                                            bookmark.bookSlug,
                                            bookmark.number
                                        )
                                    },
                                    onDeleteClick = { viewModel.removeBookmark(bookmark) },
                                    onMoveFolderClick = { selectedBookmarkForMove = bookmark }
                                )
                            }
                        }
                    }

                    if (uiState.notes.isNotEmpty()) {
                        item {
                            val latestNote = uiState.notes.first()
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                        Text("SEKSI TERHUBUNG", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Text(
                                        text = "Faedah: ${latestNote.title}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Tersedia ${uiState.notes.size} catatan tafsir & referensi sanad dalam binder kajian Anda.",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Button(
                                        onClick = { viewModel.selectTab(1) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Buka Catatan Kajian", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (uiState.notes.isEmpty()) {
                        item {
                            EmptySavedState(
                                title = "Belum Ada Catatan Kajian",
                                description = if (uiState.searchQuery.isNotBlank()) "Tidak ada catatan yang cocok dengan kata kunci \"${uiState.searchQuery}\"" else "Tulis faidah, tadabbur, atau catatan dari hadits yang Anda pelajari."
                            )
                        }
                    } else {
                        items(uiState.notes, key = { it.id }) { note ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                NoteStudyCard(
                                    note = note,
                                    fontSettings = fontSettings,
                                    onClick = { onNavigateToEditor(note.id, null, null) },
                                    onEditClick = { onNavigateToEditor(note.id, null, null) },
                                    onDeleteClick = { viewModel.deleteNote(note.id) },
                                    onOpenHadithClick = {
                                        onNavigateToDetail(
                                            note.bookSlug,
                                            note.number
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // BANNER EKSPOR KAJIAN
                    if (uiState.notes.isNotEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Print,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(8.dp).size(22.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "Ekspor Binder Kajian ke PDF / Teks",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Kompilasi ringkasan dan sanad untuk materi khutbah atau halaqah santri.",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            showExportDialog = true
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Default.Download,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Ekspor Sekarang",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 6. BANNER INDIKATOR PENYIMPANAN OFFLINE (Bawah)
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Tersimpan di Penyimpanan Lokal",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${uiState.bookmarks.size + uiState.notes.size} hadits & catatan siap dibaca tanpa jaringan internet (Offline).",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // 🟢 DIALOG SORTIR HADITS
    if (showSortDialog) {
        val sortOptions = remember(uiState.selectedTab) {
            if (uiState.selectedTab == 0) {
                listOf(
                    SortOrder.NEWEST to "Terakhir Disimpan",
                    SortOrder.OLDEST to "Terlama Disimpan",
                    SortOrder.NAME_TITLE to "Nama Kitab (A-Z)",
                    SortOrder.HADITH_NUMBER to "Nomor Hadits (1-N)"
                )
            } else {
                listOf(
                    SortOrder.NEWEST to "Terakhir Diedit",
                    SortOrder.OLDEST to "Terlama Diedit",
                    SortOrder.NAME_TITLE to "Judul Catatan (A-Z)"
                )
            }
        }
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = {
                Text(
                    if (uiState.selectedTab == 0) "Urutkan Bookmark" else "Urutkan Catatan Kajian",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    sortOptions.forEach { (order, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSortOrder(order)
                                    showSortDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.sortOrder == order,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSortDialog = false }) { Text("Tutup") }
            }
        )
    }

    // 🟢 DIALOG BUAT FOLDER KOLEKSI BARU
    if (showCreateFolderDialog) {
        var newFolderName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateFolderDialog = false },
            title = { Text("Buat Folder Koleksi Baru", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    placeholder = { Text("Contoh: Kajian Tazkiyatun Nufs") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createNewFolder(newFolderName)
                        showCreateFolderDialog = false
                    }
                ) {
                    Text("Buat Folder")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateFolderDialog = false }) { Text("Batal") }
            }
        )
    }

    // 🟢 DIALOG PINDAHKAN HADITS KE FOLDER LAIN
    selectedBookmarkForMove?.let { bookmark ->
        AlertDialog(
            onDismissRequest = { selectedBookmarkForMove = null },
            title = { Text("Pindahkan ke Folder...", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    uiState.availableFolderTags.forEach { folder ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.moveBookmarkToFolder(bookmark, folder)
                                    selectedBookmarkForMove = null
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Folder,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                folder,
                                fontSize = 14.sp,
                                fontWeight = if (bookmark.folderTags.contains(folder)) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedBookmarkForMove = null }) { Text("Batal") }
            }
        )
    }

    // 🟢 DIALOG PILIHAN FORMAT EKSPOR (PDF vs Teks)
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text(
                    text = "Ekspor Binder Kajian",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Pilih format dokumen untuk mengekspor ${uiState.notes.size} catatan kajian Anda:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showExportDialog = false
                                PdfExporter.exportAndShareNotesPdf(context, uiState.notes)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Dokumen PDF (.pdf)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Format rapi siap cetak untuk materi khutbah & halaqah",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showExportDialog = false
                                val compiledText = buildString {
                                    appendLine("📚 KOMPILASI BINDER CATATAN KAJIAN")
                                    appendLine("Total: ${uiState.notes.size} Catatan")
                                    appendLine("═════════════════════════════════════\n")
                                    uiState.notes.forEachIndexed { idx, n ->
                                        appendLine("${idx + 1}. ${n.title.uppercase()}")
                                        appendLine("📁 Binder: ${n.categoryTag}")
                                        if (n.tags.isNotEmpty()) appendLine(
                                            "🏷️ ${n.tags.joinToString(" ") { "#$it" }}"
                                        )
                                        appendLine("─────────────────────────────────────")
                                        appendLine(n.content)
                                        if (n.bookName.isNotBlank()) {
                                            appendLine("\n📖 Rujukan: ${n.bookName} No. ${n.number}")
                                        }
                                        appendLine("\n═════════════════════════════════════\n")
                                    }
                                }

                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, compiledText)
                                    type = "text/plain"
                                }
                                context.startActivity(
                                    Intent.createChooser(sendIntent, "Ekspor Binder Kajian")
                                )
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Teks Lengkap (.txt)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Format teks untuk disalin ke WhatsApp atau catatan HP",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) { Text("Batal") }
            }
        )
    }

    // 🟢 BOTTOM SHEET UKURAN FONT
    if (showFontBottomSheet) {
        FontSettingsBottomSheet(
            settings = fontSettings,
            onSettingsChange = { newSettings ->
                viewModel.updateFontSettings(newSettings)
            },
            onDismiss = { showFontBottomSheet = false }
        )
    }
}

@Composable
private fun EmptySavedState(title: String, description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 40.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "📂", fontSize = 42.sp)
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
