package com.nusa.hadithapp.ui.saved.editor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.domain.model.ReadingHistoryItem
import com.nusa.hadithapp.domain.model.ReferencedHadith
import com.nusa.hadithapp.ui.home.LastReadHadith
import com.nusa.hadithapp.util.BookSlugUtil
import com.nusa.hadithapp.util.extractArabicMatan
import com.nusa.hadithapp.util.extractMatanOnly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachHadithBottomSheet(
    bookmarks: List<Bookmark>,
    readingHistory: List<ReadingHistoryItem> = emptyList(),
    lastRead: LastReadHadith? = null,
    quickSearchResult: ReferencedHadith?,
    targetHadithToSwap: ReferencedHadith? = null,
    onSearchHadith: (bookSlug: String, number: Int) -> Unit,
    onHadithConfirmed: (ReferencedHadith) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusManager = LocalFocusManager.current

    var selectedTab by remember { mutableIntStateOf(1) }
    var selectedHadith by remember(targetHadithToSwap, bookmarks) {
        mutableStateOf<ReferencedHadith?>(
            if (targetHadithToSwap != null) {
                targetHadithToSwap
            } else {
                bookmarks.firstOrNull()?.let {
                    ReferencedHadith(it.bookSlug, it.number, it.bookName, it.grade, it.arabicText, it.translation, it.sanadSnippet)
                }
            }
        )
    }

    val popularBooks = BookSlugUtil.getAllSlugsList()
    var selectedBookSlug by remember { mutableStateOf("bukhari") }
    var hadithNumberInput by remember { mutableStateOf("1") }
    var bookmarkSearchQuery by remember { mutableStateOf("") }
    var historySearchQuery by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedBookmark by remember { mutableStateOf<Bookmark?>(bookmarks.firstOrNull()) }

    LaunchedEffect(quickSearchResult) {
        val result = quickSearchResult ?: return@LaunchedEffect
        if (selectedTab == 0 || (selectedHadith?.bookSlug.equals(result.bookSlug, ignoreCase = true) && selectedHadith?.number == result.number)) {
            selectedHadith = result
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 16.dp)
        ) {
            // 1. Header: Judul & Subtitle & Tombol Tutup
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Sematkan Rujukan Hadis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup")
                }
            }

            Text(
                text = "Cari hadis dari 14 kitab atau pilih dari bookmark & riwayat bacaan Anda",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Tiga Tab Sumber Hadits
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Tab 0: Pencarian Cepat
                    SourceTabItem(
                        title = "Pencarian\nCepat",
                        icon = Icons.Default.Search,
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    // Tab 1: Bookmark Tersimpan
                    SourceTabItem(
                        title = if (bookmarks.isNotEmpty()) "Bookmark\nTersimpan (${bookmarks.size})" else "Bookmark\nTersimpan",
                        icon = Icons.Default.Bookmark,
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1.1f)
                    )
                    // Tab 2: Riwayat Bacaan
                    SourceTabItem(
                        title = if (readingHistory.isNotEmpty()) "Riwayat\nBacaan (${readingHistory.size})" else "Riwayat\nBacaan",
                        icon = Icons.Default.History,
                        isSelected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. KONTEN SESUAI TAB
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (selectedTab) {
                    // 🟢 TAB 0: PENCARIAN CEPAT (PILIH KITAB & NOMOR)
                    0 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Pilih Kitab:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(popularBooks) { slug ->
                                    val isSelected = selectedBookSlug == slug
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedBookSlug = slug },
                                        label = { Text(BookSlugUtil.getOfficialBookName(slug), fontSize = 11.sp) }
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = hadithNumberInput,
                                    onValueChange = { hadithNumberInput = it },
                                    label = { Text("Nomor Hadits") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Button(
                                    onClick = {
                                        val num = hadithNumberInput.toIntOrNull() ?: 1
                                        onSearchHadith(selectedBookSlug, num)
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Cari")
                                }
                            }

                            // Tampilkan hasil pencarian cepat jika ditemukan
                            quickSearchResult?.let { result ->
                                val matanArabic = remember(result.arabicText) {
                                    extractArabicMatan(result.arabicText)
                                }
                                val matanTranslation = remember(result.translation) {
                                    extractMatanOnly(result.translation)
                                }
                                val isSelected = selectedHadith?.bookSlug.equals(result.bookSlug, ignoreCase = true) && selectedHadith?.number == result.number
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Hadits Ditemukan:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                HadithSelectableCard(
                                    bookName = result.bookName,
                                    number = result.number,
                                    grade = result.grade,
                                    arabicText = matanArabic,
                                    translation = matanTranslation,
                                    isSelected = isSelected,
                                    onClick = { selectedHadith = result }
                                )
                            }
                        }
                    }

                    // 🟢 TAB 1: BOOKMARK TERSIMPAN
                    1 -> {
                        val filteredBookmarks = remember(bookmarkSearchQuery, bookmarks) {
                            if (bookmarkSearchQuery.isBlank()) bookmarks
                            else bookmarks.filter {
                                it.bookName.contains(bookmarkSearchQuery, ignoreCase = true) ||
                                        it.number.toString().contains(bookmarkSearchQuery) ||
                                        it.translation.contains(bookmarkSearchQuery, ignoreCase = true)
                            }
                        }

                        Column {
                            OutlinedTextField(
                                value = bookmarkSearchQuery,
                                onValueChange = { bookmarkSearchQuery = it },
                                placeholder = { Text("Cari dalam bookmark...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {focusManager.clearFocus()}),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(filteredBookmarks, key = { it.id }) { b ->
                                    val isSelected = selectedHadith?.bookSlug.equals(b.bookSlug, ignoreCase = true) && selectedHadith?.number == b.number
                                    val matanTranslation = remember(b.translation) {
                                        extractMatanOnly(b.translation)
                                    }
                                    HadithSelectableCard(
                                        bookName = b.bookName,
                                        number = b.number,
                                        grade = b.grade,
                                        arabicText = b.arabicText,
                                        translation = matanTranslation,
                                        subtitle = if (b.babTitle.isNotBlank()) b.babTitle else b.kitabTitle,
                                        isSelected = isSelected,
                                        onClick = {
                                            focusManager.clearFocus()
                                            selectedHadith = ReferencedHadith(
                                                bookSlug = b.bookSlug,
                                                number = b.number,
                                                bookName = b.bookName,
                                                grade = b.grade,
                                                arabicText = b.arabicText,
                                                translation = matanTranslation,
                                                firstSanad = b.sanadSnippet
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 🟢 TAB 2: RIWAYAT BACAAN
                    2 -> {
                        val filteredHistory = remember(historySearchQuery, readingHistory) {
                            if (historySearchQuery.isBlank()) readingHistory
                            else readingHistory.filter {
                                it.bookName.contains(historySearchQuery, ignoreCase = true) ||
                                        it.number.toString().contains(historySearchQuery) ||
                                        it.chapter.contains(historySearchQuery, ignoreCase = true) ||
                                        it.translationSnippet.contains(historySearchQuery, ignoreCase = true)
                            }
                        }

                        Column {
                            OutlinedTextField(
                                value = historySearchQuery,
                                onValueChange = { historySearchQuery = it },
                                placeholder = { Text("Cari dalam riwayat bacaan...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            if (filteredHistory.isNotEmpty()) {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    items(filteredHistory, key = { "${it.bookSlug}_${it.number}" }) { item ->
                                        val isSelected = selectedHadith?.bookSlug.equals(item.bookSlug, ignoreCase = true) && selectedHadith?.number == item.number
                                        val matanTranslation = remember(item.translationSnippet) {
                                            extractMatanOnly(item.translationSnippet)
                                        }
                                        val matanArabic = remember(item.arabicSnippet) {
                                            extractArabicMatan(item.arabicSnippet)
                                        }

                                        HadithSelectableCard(
                                            bookName = item.bookName,
                                            number = item.number,
                                            grade = "Dibaca",
                                            arabicText = if (matanArabic.isNotBlank()) matanArabic else "...",
                                            translation = if (matanTranslation.isNotBlank()) matanTranslation else item.chapter,
                                            subtitle = if (item.chapter.isNotBlank()) item.chapter else item.kitab,
                                            isSelected = isSelected,
                                            onClick = {
                                                focusManager.clearFocus()
                                                selectedHadith = ReferencedHadith(
                                                    bookSlug = item.bookSlug,
                                                    number = item.number,
                                                    bookName = item.bookName,
                                                    grade = "Shahih",
                                                    arabicText = item.arabicSnippet,
                                                    translation = matanTranslation,
                                                    firstSanad = ""
                                                )
                                                onSearchHadith(item.bookSlug, item.number)
                                            }
                                        )
                                    }
                                }
                            } else if (readingHistory.isEmpty() && lastRead != null) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Hadits Terakhir Dibaca:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    val isSelected = selectedHadith?.bookSlug.equals(lastRead.bookSlug, ignoreCase = true) && selectedHadith?.number == lastRead.number

                                    HadithSelectableCard(
                                        bookName = lastRead.bookName,
                                        number = lastRead.number,
                                        grade = "Terakhir Dibaca",
                                        arabicText = "Hadits Terakhir Dibaca",
                                        translation = lastRead.title,
                                        isSelected = isSelected,
                                        onClick = {
                                            focusManager.clearFocus()
                                            selectedHadith = ReferencedHadith(
                                                bookSlug = lastRead.bookSlug,
                                                number = lastRead.number,
                                                bookName = lastRead.bookName,
                                                grade = "Shahih",
                                                arabicText = "",
                                                translation = lastRead.title,
                                                firstSanad = ""
                                            )
                                            onSearchHadith(lastRead.bookSlug, lastRead.number)
                                        }
                                    )
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (historySearchQuery.isNotBlank()) "Tidak ada riwayat yang cocok." else "Belum ada riwayat bacaan hadits.",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. FOOTER
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedHadith?.let { chosen ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(16.dp)
                                )
                            }
                            Column {
                                Text(text = "1 Hadis Terpilih", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(text = "${chosen.bookName} #${chosen.number}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                        Button(
                            onClick = {
                                selectedHadith?.let {
                                    onHadithConfirmed(it)
                                    onDismiss()
                                }
                            },
                            enabled = selectedHadith != null,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth().size(48.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (targetHadithToSwap != null) "Ganti Hadits Rujukan" else "Sematkan ke Catatan",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
private fun SourceTabItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}