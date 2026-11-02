package com.nusa.hadithapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.HadithGradeFilter
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.domain.model.matchesHadithGrade
import com.nusa.hadithapp.ui.components.dialogs.PerawiDetailDialog
import com.nusa.hadithapp.ui.search.components.ArabicInputHelper
import com.nusa.hadithapp.ui.search.components.ArabicVirtualKeyboard
import com.nusa.hadithapp.ui.search.components.BookFilterChips
import com.nusa.hadithapp.ui.search.components.GradeFilterChips
import com.nusa.hadithapp.ui.search.components.MultiBookFilterBottomSheet
import com.nusa.hadithapp.ui.search.components.RawiFilterPanel
import com.nusa.hadithapp.ui.search.components.RawiResultCard
import com.nusa.hadithapp.ui.search.components.SearchDiscoveryView
import com.nusa.hadithapp.ui.search.components.SearchResultCard
import com.nusa.hadithapp.ui.search.components.SearchTopBar
import com.nusa.hadithapp.util.BookSlugUtil
import kotlinx.coroutines.launch
import kotlin.collections.get

private const val TAG = "SearchScreen"

/**
 * Screen untuk pencarian hadith dengan tab selection
 * @param viewModel SearchViewModel untuk state management
 * @param onBackClick Callback saat user klik tombol back
 * @param onHadithClick Callback saat user klik hadith card
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onHadithClick: (bookSlug: String, number: Int, searchIndex: Int) -> Unit,
    onPerawiHadithClick: ((bookSlug: String, number: Int, kodeRawi: Int, rawiName: String) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.currentQuery.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val selectedFilterBook by viewModel.selectedFilterBook.collectAsState()
    val selectedGradeFilter by viewModel.selectedGradeFilter.collectAsState()
    val targetSearchBooks by viewModel.targetSearchBooks.collectAsState()
    var showMultiBookFilterSheet by remember { mutableStateOf(false) }

    val rawiUiState by viewModel.rawiUiState.collectAsState()
    val kuniyahQuery by viewModel.rawiKuniyahQuery.collectAsState()
    val classifyQuery by viewModel.rawiClassifyQuery.collectAsState()
    val levelQuery by viewModel.rawiLevelQuery.collectAsState()

    val selectedRawi by viewModel.selectedRawiForDetail.collectAsState()
    val perawiProfileState by viewModel.perawiProfileState.collectAsState()
    val scholarCommentState by viewModel.scholarCommentState.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val quickJumpSuggestion by viewModel.quickJumpSuggestion.collectAsState()

    var textInput by remember(query) { mutableStateOf(query) }
    var isArabicBarVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val rawiListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SearchTopBar(
                query = textInput,
                onQueryChange = {
                    textInput = it
                    viewModel.updateQuery(it)
                },
                onSearchTriggered = {
                    focusManager.clearFocus()
                    if (selectedTab == 0) {
                        if (textInput.isNotBlank()) {
                            viewModel.search(textInput)
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        }
                    } else {
                        viewModel.updateQuery(textInput)
                        viewModel.searchRawi(textInput)
                        coroutineScope.launch {
                            rawiListState.scrollToItem(0)
                        }
                    }
                },
                onBackClick = onBackClick,
                onFilterClick = { showMultiBookFilterSheet = true },
                selectedBookCount = targetSearchBooks.size,
                isArabicBarVisible = isArabicBarVisible,
                onToggleArabicBar = {
                    val nextState = !isArabicBarVisible
                    isArabicBarVisible = nextState
                    if (nextState) {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setTab(0) },
                    text = { Text("Hadits", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setTab(1) },
                    text = { Text("Perawi / Rawi", fontWeight = FontWeight.Bold) }
                )
            }

            //TAB 1. RAWI
            if (selectedTab == 1) {
                LazyColumn(
                    state = rawiListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Item 1: Panel Filter
                    item {
                        RawiFilterPanel(
                            kuniyahValue = kuniyahQuery,
                            onKuniyahChange = { newKuniyah ->
                                viewModel.updateRawiFilters(newKuniyah, classifyQuery, levelQuery)
                            },
                            classifyValue = classifyQuery,
                            onClassifyChange = { newClassify ->
                                viewModel.updateRawiFilters(kuniyahQuery, newClassify, levelQuery)
                            },
                            levelValue = levelQuery,
                            onLevelChange = { newLevel ->
                                viewModel.updateRawiFilters(kuniyahQuery, classifyQuery, newLevel)
                            },
                            onApplyFilter = {
                                focusManager.clearFocus()
                                viewModel.searchRawi(textInput)
                            },
                            onReset = { viewModel.updateRawiFilters("","","") }
                        )
                    }

                    // Item 2: UI State Handler
                    when (val state = rawiUiState) {
                        is RawiSearchUiState.Idle -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Ketik nama perawi di atas atau terapkan filter untuk mulai mencari.",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        is RawiSearchUiState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                        }

                        is RawiSearchUiState.Empty -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tidak ditemukan perawi yang sesuai dengan kriteria.",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        is RawiSearchUiState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = state.message,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        is RawiSearchUiState.Success -> {
                            item {
                                Text(
                                    text = "Ditemukan ${state.rawiList.size} Perawi",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            items(state.rawiList, key = { it.kodeRawi }) { rawi ->
                                RawiResultCard(
                                    item = rawi,
                                    quality = rawi.quality,
                                    onClick = {
                                        viewModel.showPerawiDetail(rawi)
                                    }
                                )
                            }
                        }
                    }
                }
                return@Column
            }

            // Kartu Loncat Cepat ke Hadis Spesifik (jika terdeteksi nama kitab + nomor)
            if (quickJumpSuggestion != null) {
                val jump = quickJumpSuggestion!!
                Card(
                    onClick = {
                        onHadithClick(jump.bookSlug, jump.hadithNumber, 0)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🎯", fontSize = 20.sp)
                            Column {
                                Text(
                                    text = "Loncat Langsung ke Hadis",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${jump.bookName} No. ${jump.hadithNumber}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Button(
                            onClick = { onHadithClick(jump.bookSlug, jump.hadithNumber, 0) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Baca Sekarang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            when (val state = uiState) {
                is SearchUiState.Idle -> {
                    SearchDiscoveryView(
                        recentSearches = recentSearches,
                        onQueryClick = { clickedQuery ->
                            textInput = clickedQuery
                            focusManager.clearFocus()
                            viewModel.updateQuery(clickedQuery)
                            viewModel.search(clickedQuery)
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        },
                        onRemoveRecentQuery = { viewModel.removeRecentSearch(it) },
                        onClearAllRecent = { viewModel.clearAllRecentSearches() }
                    )
                }

                is SearchUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                    }
                }

                is SearchUiState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Tidak ada hasil ditemukan untuk \"$query\"",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        SearchDiscoveryView(
                            recentSearches = recentSearches,
                            onQueryClick = { clickedQuery ->
                                textInput = clickedQuery
                                focusManager.clearFocus()
                                viewModel.updateQuery(clickedQuery)
                                viewModel.search(clickedQuery)
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            },
                            onRemoveRecentQuery = { viewModel.removeRecentSearch(it) },
                            onClearAllRecent = { viewModel.clearAllRecentSearches() }
                        )
                    }
                }

                is SearchUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is SearchUiState.Success -> {
                    val allBooks =
                        state.resultsPerBook.filter { it.value.isNotEmpty() }.keys.sortedBy { bookSlug ->
                            val index = BookSlugUtil.ALL_BOOK_SLUGS.indexOf(bookSlug)
                            if (index != -1) index else Int.MAX_VALUE
                        }

                    val currentPoolItems = if (selectedFilterBook == null) {
                        state.resultsPerBook.values.flatten()
                    } else {
                        state.resultsPerBook[selectedFilterBook] ?: emptyList()
                    }

                    val allGradeCount = currentPoolItems.size
                    val shahihGradeCount = remember(currentPoolItems) {
                        currentPoolItems.count { matchesHadithGrade(it, HadithGradeFilter.SHAHIH) }
                    }
                    val hasanGradeCount = remember(currentPoolItems) {
                        currentPoolItems.count { matchesHadithGrade(it, HadithGradeFilter.HASAN) }
                    }
                    val dhaifGradeCount = remember(currentPoolItems) {
                        currentPoolItems.count { matchesHadithGrade(it, HadithGradeFilter.DHAIF) }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        BookFilterChips(
                            allBooks = allBooks,
                            totalCount = state.total,
                            resultsPerBook = state.resultsPerBook,
                            selectedFilterBook = selectedFilterBook,
                            onBookSelected = { bookSlug ->
                                viewModel.filterByBook(bookSlug)
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }
                        )

                        GradeFilterChips(
                            selectedGrade = selectedGradeFilter,
                            allCount = allGradeCount,
                            shahihCount = shahihGradeCount,
                            hasanCount = hasanGradeCount,
                            dhaifCount = dhaifGradeCount,
                            onGradeSelected = { newGrade ->
                                viewModel.setGradeFilter(newGrade)
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }
                        )

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (selectedFilterBook == null) {
                                // 🟢 MODE SEMUA KITAB: Tampilkan per kelompok kitab + Tombol penuntun ke filter kitab
                                var totalItemsRendered = 0
                                allBooks.forEach { bookSlug ->
                                    val rawItemsForBook = state.resultsPerBook[bookSlug] ?: emptyList()
                                    val itemsForBook = rawItemsForBook.filter {
                                        matchesHadithGrade(it, selectedGradeFilter)
                                    }
                                    if (itemsForBook.isNotEmpty()) {
                                        totalItemsRendered += itemsForBook.size
                                        items(
                                            itemsForBook,
                                            key = { "${it.bookSlug}_${it.number}" }) { item ->
                                            SearchResultCard(item = item, searchKeyword = query, onClick = {
                                                val index = viewModel.getSearchItemIndex(
                                                    item.bookSlug,
                                                    item.number
                                                )
                                                onHadithClick(item.bookSlug, item.number, index)
                                            })
                                        }

                                        val totalForBook = viewModel.getTotalCountForBook(bookSlug)
                                        val loadedCount = rawItemsForBook.size

                                        // Jika masih ada sisa hadis di kitab ini, tampilkan tombol penuntun
                                        if (loadedCount < totalForBook) {
                                            item(key = "footer_$bookSlug") {
                                                val remaining = totalForBook - loadedCount
                                                val bookName =
                                                    rawItemsForBook.firstOrNull()?.bookName ?: bookSlug
                                                Button(
                                                    onClick = { viewModel.filterByBook(bookSlug) },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                                    )
                                                ) {
                                                    Text(
                                                        "Lihat $remaining hadis lainnya di $bookName ➔",
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (totalItemsRendered == 0 && selectedGradeFilter != HadithGradeFilter.ALL) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 40.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Tidak ada hadis berderajat ${selectedGradeFilter.label} pada hadis yang sudah dimuat.",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                // 🟢 MODE FILTER KITAB: Tampilkan seluruh item yang dimuat + Tombol Muat Lebih Banyak
                                val rawDisplayedItems =
                                    state.resultsPerBook[selectedFilterBook] ?: emptyList()
                                val displayedItems = rawDisplayedItems.filter {
                                    matchesHadithGrade(it, selectedGradeFilter)
                                }

                                if (displayedItems.isEmpty() && selectedGradeFilter != HadithGradeFilter.ALL) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 40.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Tidak ada hadis berderajat ${selectedGradeFilter.label} di kitab ini pada hadis yang sudah dimuat.",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                } else {
                                    items(
                                        displayedItems,
                                        key = { "${it.bookSlug}_${it.number}" }) { item ->
                                        SearchResultCard(item = item, searchKeyword = query, onClick = {
                                            val index =
                                                viewModel.getSearchItemIndex(item.bookSlug, item.number)
                                            onHadithClick(item.bookSlug, item.number, index)
                                        })
                                    }
                                }

                                val currentBookSlug = selectedFilterBook!!
                                if (viewModel.hasMoreItems(currentBookSlug)) {
                                    item {
                                        Button(
                                            onClick = { viewModel.loadMoreForBook(currentBookSlug) },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                                        ) {
                                            Text(
                                                "Muat Lebih Banyak Hadits",
                                                color = MaterialTheme.colorScheme.onTertiary
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

        // Keyboard Arab Virtual Lengkap (Hijaiyah + Harakat)
        ArabicVirtualKeyboard(
                visible = isArabicBarVisible,
                onKeyClick = { key ->
                    val updated = ArabicInputHelper.appendKey(textInput, key)
                    textInput = updated
                    viewModel.updateQuery(updated)
                },
                onDeleteClick = {
                    if (textInput.isNotEmpty()) {
                        val updated = textInput.dropLast(1)
                        textInput = updated
                        viewModel.updateQuery(updated)
                    }
                },
                onClearClick = {
                    val harakatRegex = "[\u064B-\u065F\u0670]".toRegex()
                    val cleaned = textInput.replace(harakatRegex, "")
                    textInput = cleaned
                    viewModel.updateQuery(cleaned)
                },
                onSearchClick = {
                    isArabicBarVisible = false
                    focusManager.clearFocus()
                    if (selectedTab == 0) {
                        if (textInput.isNotBlank()) {
                            viewModel.search(textInput)
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        }
                    } else {
                        viewModel.updateQuery(textInput)
                        viewModel.searchRawi(textInput)
                        coroutineScope.launch {
                            rawiListState.scrollToItem(0)
                        }
                    }
                },
                onCloseClick = {
                    isArabicBarVisible = false
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        selectedRawi?.let { rawi ->
            val sanadPerawi = SanadPerawi(
                kodeRawi = rawi.kodeRawi,
                name = rawi.name,
                quality = rawi.quality,
                kalangan = rawi.kalangan ?: ""
            )

            PerawiDetailDialog(
                perawi = sanadPerawi,
                perawiProfileState = perawiProfileState,
                scholarCommentState = scholarCommentState,
                onFetchBookHadithNumbers = { bookSlug ->
                    viewModel.loadPerawiHadithNumbers(bookSlug, rawi.kodeRawi)
                },
                onNavigateToHadith = { targetSlug, targetNumber, kodeRawi, rawiName, numbers ->
                    viewModel.savePerawiSession(targetSlug, kodeRawi, rawiName, numbers, targetNumber)
                    viewModel.dismissPerawiDetail()
                    if (onPerawiHadithClick != null) {
                        onPerawiHadithClick(targetSlug, targetNumber, kodeRawi, rawiName)
                    } else {
                        onHadithClick(targetSlug, targetNumber, -1)
                    }
                },
                onDismiss = { viewModel.dismissPerawiDetail() }
            )
        }

        if (showMultiBookFilterSheet) {
            MultiBookFilterBottomSheet(
                selectedCanonicalSlugs = targetSearchBooks,
                onApplyFilter = { newSelected ->
                    viewModel.setTargetSearchBooks(newSelected)
                },
                onDismiss = { showMultiBookFilterSheet = false }
            )
        }
    }
}