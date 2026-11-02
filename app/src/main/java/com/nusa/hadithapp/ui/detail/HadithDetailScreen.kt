package com.nusa.hadithapp.ui.detail

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.data.data_source.remote.config.HadithClassificationMetadata
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.ui.components.dialogs.GradeDefinitionDialog
import com.nusa.hadithapp.ui.components.dialogs.JumpToHadithDialog
import com.nusa.hadithapp.ui.components.dialogs.PerawiDetailDialog
import com.nusa.hadithapp.ui.components.dialogs.QualityLegendDialog
import com.nusa.hadithapp.ui.components.dialogs.SanadTreeBercabangDialog
import com.nusa.hadithapp.ui.detail.components.HadithBottomActionBar
import com.nusa.hadithapp.ui.detail.components.HadithContent
import com.nusa.hadithapp.ui.detail.components.HadithHeaderControls
import com.nusa.hadithapp.ui.detail.components.HadithInfoDialog
import com.nusa.hadithapp.ui.detail.components.KitabBabSelectionBottomSheet
import com.nusa.hadithapp.ui.detail.components.NoteBottomSheet
import com.nusa.hadithapp.ui.detail.components.QuoteCardDialog
import com.nusa.hadithapp.ui.detail.components.HadithAiBottomSheet
import com.nusa.hadithapp.ui.detail.components.HadithAudioPlayerBar
import com.nusa.hadithapp.ui.detail.components.SanadBottomSheet
import com.nusa.hadithapp.ui.detail.components.SelectFolderBottomSheet
import com.nusa.hadithapp.util.ActionPopupMenu
import com.nusa.hadithapp.util.FontSettingsBottomSheet
import com.nusa.hadithapp.util.HadithTtsManager
import com.nusa.hadithapp.util.TtsLanguage
import com.nusa.hadithapp.util.TtsPlaybackState
import com.nusa.hadithapp.util.formatHadithForSharing
import com.nusa.hadithapp.util.rememberHadithTtsController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithDetailScreen(
    bookSlug: String,
    number: Int,
    vMember: Int = -1,
    themeName: String? = null,
    categoryName: String? = null,
    searchIndex: Int = -1,
    perawiKode: Int = -1,
    perawiName: String? = null,
    viewModel: HadithViewModel,
    onBackClick: () -> Unit,
    onNavigateToHadith: ((bookSlug: String, hadithNumber: Int, kodeRawi: Int, rawiName: String) -> Unit)? = null,
    onNavigateToBiography: ((String) -> Unit)? = null,
    onNavigateToMusthalah: ((String) -> Unit)? = null,
    onNavigateToCompare: ((bookSlug: String, number: Int) -> Unit)? = null,
    onNavigateToSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val uiState by viewModel.uiState.collectAsState()
    val perawiProfileState by viewModel.perawiProfileState.collectAsState()
    val scholarCommentState by viewModel.scholarCommentState.collectAsState()
    val similarHadithState by viewModel.similarHadithState.collectAsState()
    val kitabList by viewModel.kitabListState.collectAsState()
    val babList by viewModel.babListState.collectAsState()
    val fontSettings by viewModel.fontSettings.collectAsState()
    val isSearchMode by viewModel.isFromSearch.collectAsState()
    val searchCurrentIndex by viewModel.searchCurrentIndex.collectAsState()
    val isPerawiMode by viewModel.isPerawiMode.collectAsState()
    val perawiSession by viewModel.perawiSession.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()
    val currentNote by viewModel.currentNote.collectAsState()
    val availableFolders by viewModel.availableFolders.collectAsState()
    val currentBookmarkFolder by viewModel.currentBookmarkFolders.collectAsState()
    val aiResponseState by viewModel.aiResponseState.collectAsState()
    val settingsState by viewModel.settingsState.collectAsState()
    var showAiBottomSheet by remember { mutableStateOf(false) }

    val ttsController = rememberHadithTtsController()
    val ttsPlaybackState by ttsController.playbackState.collectAsState()

    val onCopyClick: () -> Unit = {
        (uiState as? HadithUiState.Success)?.hadith?.let { hadith ->
            val textToCopy = formatHadithForSharing(hadith)
            clipboardManager.setText(AnnotatedString(textToCopy))
            Toast.makeText(context, "Teks hadits berhasil disalin", Toast.LENGTH_SHORT).show()
        }
    }
    val shareAsPlainText: () -> Unit = {
        (uiState as? HadithUiState.Success)?.hadith?.let { hadith ->
            val textToShare = formatHadithForSharing(hadith)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Bagikan Hadits")
            context.startActivity(shareIntent)
        }
    }
    var showShareOptionsSheet by remember { mutableStateOf(false) }
    val onShareClick: () -> Unit = {
        showShareOptionsSheet = true
    }

    var showMenuPopup by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showFontBottomSheet by remember { mutableStateOf(false) }
    var perawiTerpilih by remember { mutableStateOf<SanadPerawi?>(null) }
    var tampilkanKeteranganWarna by remember { mutableStateOf(false) }
    var tampilkanDefinisiGrade by remember { mutableStateOf(false) }
    var activeMusthalahTerm by remember { mutableStateOf<com.nusa.hadithapp.domain.model.musthalah.MusthalahTerm?>(null) }
    var tampilkanBaganPopUpBercabang by remember { mutableStateOf(false) }
    var showKitabSheet by remember { mutableStateOf(false) }
    var showBabSheet by remember { mutableStateOf(false) }
    var showSanadBottomSheet by remember { mutableStateOf(false) }
    var sanadSheetTabIndex by remember { mutableIntStateOf(0) }
    var showQuoteCardDialog by remember { mutableStateOf(false) }
    var showJumpDialog by remember { mutableStateOf(false) }
    var activeVMember by remember { mutableStateOf(vMember) }
    var showNoteBottomSheet by remember { mutableStateOf(false) }
    var showSelectFolderSheet by remember { mutableStateOf(false) }
    var isBottomBarVisible by remember { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -15f) {
                    isBottomBarVisible = false
                } else if (available.y > 15f) {
                    isBottomBarVisible = true
                }
                return Offset.Zero
            }
        }
    }

    val isThemeMode = !themeName.isNullOrBlank() && vMember != -1
    val isCategoryMode = !categoryName.isNullOrBlank() && vMember != -1
    val currentHadith = (uiState as? HadithUiState.Success)?.hadith

    LaunchedEffect(currentHadith?.number) {
        ttsController.stop()
        isBottomBarVisible = true
    }

    LaunchedEffect(bookSlug, number, activeVMember, perawiKode) {
        viewModel.observeBookmarkFolders(bookSlug, number)
        viewModel.observeBookmarkAndNote(bookSlug, number)
        when {
            isThemeMode && themeName != null -> {
                viewModel.loadHadithByTheme(themeName, activeVMember)
            }

            isCategoryMode && categoryName != null -> {
                viewModel.loadHadithByCategory(categoryName, activeVMember)
            }

            perawiKode != -1 && !perawiName.isNullOrBlank() -> {
                viewModel.loadHadithWithPerawiMode(bookSlug, number, perawiKode, perawiName)
            }

            searchIndex != -1 -> {
                viewModel.loadHadithWithSearchIndex(bookSlug, number, searchIndex)
            }

            else -> {
                viewModel.loadHadith(bookSlug, number)
            }
        }
    }

    LaunchedEffect(showKitabSheet, bookSlug) {
        if (showKitabSheet && kitabList.isEmpty()) viewModel.loadAllBooks(bookSlug)
    }

    LaunchedEffect(showBabSheet, bookSlug) {
        if (showBabSheet) {
            if (currentHadith != null) {
                val currentKitabIndex = kitabList.indexOfFirst {
                    it.nKitab.equals(currentHadith.kitab, ignoreCase = true)
                }

                if (currentKitabIndex != -1) {
                    val currentKitab = kitabList[currentKitabIndex]
                    val nextKitab = kitabList.getOrNull(currentKitabIndex + 1)

                    val startHadith = currentKitab.awalan?.toIntOrNull() ?: 1
                    val endHadith =
                        nextKitab?.awalan?.toIntOrNull() ?: currentHadith.totalHadith

                    viewModel.loadAllChapters(bookSlug, startHadith, endHadith)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        val titleText = when {
                            isThemeMode -> "Tema: $themeName"
                            isCategoryMode -> "Kategori: $categoryName"
                            isPerawiMode -> "Riwayat: ${perawiSession?.rawiName ?: perawiName ?: "Perawi"}"
                            else -> currentHadith?.bookName ?: "Memuat..."
                        }
                        Text(
                            text = titleText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (currentHadith != null) {
                            val subTitleText = when {
                                isThemeMode || isCategoryMode -> "Hadits No. $activeVMember (${currentHadith.bookName} No. ${currentHadith.number})"
                                isPerawiMode -> "${currentHadith.bookName} No. ${currentHadith.number}"
                                else -> "Hadits No. ${currentHadith.number}"
                            }
                            Text(
                                text = subTitleText,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAiBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = "Tanya Syarah AI",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { showFontBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Outlined.TextFields,
                            contentDescription = "Pengaturan Font",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenuPopup = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        ActionPopupMenu(
                            expanded = showMenuPopup,
                            onDismiss = { showMenuPopup = false },
                            onAiClick = {
                                showMenuPopup = false
                                showAiBottomSheet = true
                            },
                            onBiographyClick = {
                                showMenuPopup = false
                                onNavigateToBiography?.invoke(bookSlug)
                            },
                            onCompareClick = {
                                showMenuPopup = false
                                currentHadith?.let {
                                    onNavigateToCompare?.invoke(bookSlug, it.number)
                                }
                            },
                            onInfoClick = {
                                showMenuPopup = false
                                showInfoDialog = true
                            }
                        )
                    }
                },
                windowInsets = WindowInsets(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                HadithBottomActionBar(
                    isBookmarked = isBookmarked,
                    onBookmarkClick = {
                        showSelectFolderSheet = true
                    },
                    onNoteClick = {
                        showNoteBottomSheet = true
                    },
                    onCopyClick = onCopyClick,
                    onShareClick = onShareClick,
                    onGenerateImageClick = {
                        showQuoteCardDialog = true
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is HadithUiState.Loading -> CircularProgressIndicator()
                is HadithUiState.Success -> {
                    val hadith = state.hadith

                    val displayTotal = when {
                        isThemeMode && themeName != null -> HadithClassificationMetadata.getTotalCount(themeName)
                        isCategoryMode && categoryName != null -> HadithClassificationMetadata.getTotalCount(categoryName)
                        isPerawiMode -> perawiSession?.hadithNumbers?.size ?: 0
                        isSearchMode -> 0
                        else -> hadith.totalHadith
                    }

                    val displayCurrentNumber = when {
                        isThemeMode || isCategoryMode -> activeVMember
                        isPerawiMode -> (perawiSession?.currentIndex ?: 0) + 1
                        isSearchMode -> searchCurrentIndex + 1
                        else -> hadith.number
                    }

                    val onNavigatePrevious: () -> Unit = {
                        when {
                            isThemeMode || isCategoryMode -> {
                                if (activeVMember > 1) activeVMember -= 1
                            }
                            isPerawiMode -> {
                                viewModel.loadPreviousPerawiItem()
                            }
                            isSearchMode -> {
                                viewModel.loadPreviousSearchItem()
                            }
                            else -> {
                                if (hadith.number > 1) {
                                    viewModel.loadHadith(bookSlug, hadith.number - 1)
                                }
                            }
                        }
                    }

                    val onNavigateNext: () -> Unit = {
                        when {
                            isThemeMode || isCategoryMode -> {
                                if (activeVMember < displayTotal) activeVMember += 1
                            }
                            isPerawiMode -> {
                                viewModel.loadNextPerawiItem()
                            }
                            isSearchMode -> {
                                viewModel.loadNextSearchItem()
                            }
                            else -> {
                                if (hadith.number < displayTotal) {
                                    viewModel.loadHadith(bookSlug, hadith.number + 1)
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        HadithHeaderControls(
                            kitabName = hadith.kitab,
                            babName = hadith.chapter,
                            currentNumber = displayCurrentNumber,
                            totalHadith = displayTotal,
                            onKitabDropdownClick = { showKitabSheet = true },
                            onBabDropdownClick = { showBabSheet = true },
                            onPreviousClick = onNavigatePrevious,
                            onNextClick = onNavigateNext,
                            onJumpClick = { showJumpDialog = true }
                        )

                        if (isSearchMode) {
                            Text(
                                text = viewModel.getSearchPositionText(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        if (isPerawiMode) {
                            Text(
                                text = viewModel.getPerawiPositionText(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        var totalSwipeDistance by remember { mutableFloatStateOf(0f) }

                        AnimatedContent(
                            targetState = hadith.number,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                                            slideOutHorizontally { width -> -width } + fadeOut()
                                } else {
                                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                            slideOutHorizontally { width -> width } + fadeOut()
                                }
                            },
                            label = "hadith_scroll_transition",
                            modifier = Modifier
                                .weight(1f)
                                .pointerInput(hadith.number) {
                                    detectHorizontalDragGestures(
                                        onDragStart = { totalSwipeDistance = 0f },
                                        onHorizontalDrag = { _, dragAmount ->
                                            totalSwipeDistance += dragAmount
                                        },
                                        onDragEnd = {
                                            val threshold = 65.dp.toPx()
                                            if (totalSwipeDistance < -threshold) {
                                                onNavigateNext()
                                            } else if (totalSwipeDistance > threshold) {
                                                onNavigatePrevious()
                                            }
                                            totalSwipeDistance = 0f
                                        },
                                        onDragCancel = {
                                            totalSwipeDistance = 0f
                                        }
                                    )
                                }
                        ) { _ ->
                            val isArabicPlaying = ttsPlaybackState is TtsPlaybackState.Playing && (ttsPlaybackState as TtsPlaybackState.Playing).language == TtsLanguage.ARABIC
                            val isIndonesianPlaying = ttsPlaybackState is TtsPlaybackState.Playing && (ttsPlaybackState as TtsPlaybackState.Playing).language == TtsLanguage.INDONESIAN

                            HadithContent(
                                hadith = hadith,
                                fontSettings = fontSettings,
                                similarCount = (similarHadithState as? SimilarHadithUiState.Success)?.items?.size,
                                isArabicAudioPlaying = isArabicPlaying,
                                onPlayArabicAudio = {
                                    if (isArabicPlaying) {
                                        ttsController.stop()
                                    } else {
                                        ttsController.speak(
                                            text = hadith.arabicText,
                                            language = TtsLanguage.ARABIC,
                                            onError = { err ->
                                                Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    }
                                },
                                isIndonesianAudioPlaying = isIndonesianPlaying,
                                onPlayIndonesianAudio = {
                                    if (isIndonesianPlaying) {
                                        ttsController.stop()
                                    } else {
                                        ttsController.speak(
                                            text = hadith.translation,
                                            language = TtsLanguage.INDONESIAN,
                                            onError = { err ->
                                                Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                            }
                                        )
                                    }
                                },
                                onGradeClick = { tampilkanDefinisiGrade = true },
                                onOpenSanadSheet = {
                                    sanadSheetTabIndex = 0
                                    showSanadBottomSheet = true
                                },
                                onOpenSimilarHadith = {
                                    sanadSheetTabIndex = 1
                                    viewModel.loadSimilarHadith(bookSlug, hadith.number)
                                    showSanadBottomSheet = true
                                },
                                onOpenAiSheet = {
                                    showAiBottomSheet = true
                                },
                                onRawiClick = { namaRawiTeks ->
                                    val cleanClicked = namaRawiTeks.trim()
                                    val allPerawis = hadith.sanadJalurList.flatten()
                                    val perawiDitemukan = allPerawis.find { perawi ->
                                        perawi.name.equals(cleanClicked, ignoreCase = true)
                                    } ?: allPerawis.find { perawi ->
                                        perawi.name.contains(cleanClicked, ignoreCase = true) ||
                                        cleanClicked.contains(perawi.name, ignoreCase = true)
                                    }
                                    perawiDitemukan?.let { perawi ->
                                        perawiTerpilih = perawi
                                        viewModel.loadPerawiProfile(perawi.kodeRawi)
                                        viewModel.loadScholarComments(perawi.kodeRawi)
                                    }
                                }
                            )
                        }
                    }

                    perawiTerpilih?.let { perawi ->
                        PerawiDetailDialog(
                            perawi = perawi,
                            perawiProfileState = perawiProfileState,
                            scholarCommentState = scholarCommentState,
                            onFetchBookHadithNumbers = { slug ->
                                viewModel.loadPerawiHadithNumbers(slug, perawi.kodeRawi)
                            },
                            onNavigateToHadith = { targetSlug, targetNumber, kodeRawi, rawiName, numbers ->
                                perawiTerpilih = null
                                viewModel.resetPerawiProfile()
                                viewModel.resetScholarCommentState()
                                viewModel.savePerawiSession(targetSlug, kodeRawi, rawiName, numbers, targetNumber)
                                if (onNavigateToHadith != null) {
                                    onNavigateToHadith(targetSlug, targetNumber, kodeRawi, rawiName)
                                } else {
                                    viewModel.loadHadithWithPerawiMode(targetSlug, targetNumber, kodeRawi, rawiName)
                                }
                            },
                            onDismiss = {
                                perawiTerpilih = null
                                viewModel.resetPerawiProfile()
                                viewModel.resetScholarCommentState()
                            }
                        )
                    }

                    if (tampilkanBaganPopUpBercabang) {
                        SanadTreeBercabangDialog(
                            sanadJalurList = hadith.sanadJalurList,
                            bookTitle = hadith.bookName,
                            hadithNumber = hadith.number,
                            onDismiss = { tampilkanBaganPopUpBercabang = false },
                            onSelectPerawi = { rawi ->
                                tampilkanBaganPopUpBercabang = false
                                perawiTerpilih = rawi
                                viewModel.loadPerawiProfile(rawi.kodeRawi)
                                viewModel.loadScholarComments(rawi.kodeRawi)
                            }
                        )
                    }

                    if (tampilkanKeteranganWarna) {
                        QualityLegendDialog(onDismiss = { tampilkanKeteranganWarna = false })
                    }

                    if (tampilkanDefinisiGrade) {
                        GradeDefinitionDialog(
                            bookName = hadith.bookName,
                            grade = hadith.grade,
                            onDismiss = { tampilkanDefinisiGrade = false },
                            onOpenMusthalah = { termId ->
                                tampilkanDefinisiGrade = false
                                activeMusthalahTerm = com.nusa.hadithapp.data.data_source.local.catalog.MusthalahCatalog.getTermById(termId)
                            }
                        )
                    }

                    activeMusthalahTerm?.let { term ->
                        com.nusa.hadithapp.ui.musthalah.components.MusthalahDetailBottomSheet(
                            term = term,
                            onDismiss = { activeMusthalahTerm = null },
                            onOpenFullKamus = {
                                activeMusthalahTerm = null
                                onNavigateToMusthalah?.invoke(term.id)
                            }
                        )
                    }

                    if (showSanadBottomSheet) {
                        SanadBottomSheet(
                            bookSlug = bookSlug,
                            sanadJalurList = hadith.sanadJalurList,
                            similarHadithState = similarHadithState,
                            initialTabIndex = sanadSheetTabIndex,
                            onRequestSimilarHadith = {
                                viewModel.loadSimilarHadith(bookSlug, hadith.number)
                            },
                            onSimilarHadithClick = { targetSlug, targetNumber ->
                                showSanadBottomSheet = false
                                viewModel.loadHadith(targetSlug, targetNumber)
                            },
                            onPerawiClick = { rawi ->
                                perawiTerpilih = rawi
                                viewModel.loadPerawiProfile(rawi.kodeRawi)
                                viewModel.loadScholarComments(rawi.kodeRawi)
                            },
                            onOpenTreeDialog = { tampilkanBaganPopUpBercabang = true },
                            onOpenQualityDialog = { tampilkanKeteranganWarna = true },
                            onOpenCompare = {
                                showSanadBottomSheet = false
                                onNavigateToCompare?.invoke(bookSlug, hadith.number)
                            },
                            onDismiss = { showSanadBottomSheet = false }
                        )
                    }
                }

                is HadithUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.88f)
                            .padding(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "📡", fontSize = 42.sp)
                            Text(
                                text = "Gagal Memuat Hadits",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    when {
                                        isThemeMode && themeName != null -> viewModel.loadHadithByTheme(themeName, activeVMember)
                                        isCategoryMode && categoryName != null -> viewModel.loadHadithByCategory(categoryName, activeVMember)
                                        isPerawiMode && perawiKode != -1 && !perawiName.isNullOrBlank() -> viewModel.loadHadithWithPerawiMode(bookSlug, number, perawiKode, perawiName)
                                        else -> viewModel.loadHadith(bookSlug, number)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(8.dp))
                                Text("Coba Lagi")
                            }
                        }
                    }
                }

                HadithUiState.Empty -> {
                    Text("Tidak ada data.")
                }
            }

            if (showFontBottomSheet) {
                FontSettingsBottomSheet(
                    settings = fontSettings,
                    onSettingsChange = { updateSettings ->
                        viewModel.updateFontSettings(updateSettings)
                    },
                    onDismiss = { showFontBottomSheet = false }
                )
            }

            // Floating TTS Audio Control Bar
            HadithAudioPlayerBar(
                playbackState = ttsPlaybackState,
                onStopClick = { ttsController.stop() },
                onSpeedChange = { newSpeed ->
                    ttsController.setSpeedAndReplay(newSpeed)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
        }
    }

    if (showKitabSheet) {
        KitabBabSelectionBottomSheet(
            title = "Pilih Kitab",
            items = kitabList.map { it.nKitab ?: "Tanpa Nama" },
            selectedItem = (uiState as? HadithUiState.Success)?.hadith?.kitab ?: "",
            onItemSelected = { index, _ ->
                val selectedKitab = kitabList.getOrNull(index)
                val nomorAwal = selectedKitab?.awalan?.toIntOrNull() ?: 1
                viewModel.loadHadith(bookSlug, nomorAwal)
            },
            onDismiss = { showKitabSheet = false }
        )
    }

    if (showBabSheet) {
        KitabBabSelectionBottomSheet(
            title = "Pilih Bab",
            items = babList.map { it.nBab ?: "Tanpa Nama" },
            selectedItem = (uiState as? HadithUiState.Success)?.hadith?.chapter ?: "",
            onItemSelected = { index, _ ->
                val selectedBab = babList.getOrNull(index)
                val nomorAwal = selectedBab?.awalanBab?.toIntOrNull() ?: 1
                viewModel.loadHadith(bookSlug, nomorAwal)
            },
            onDismiss = { showBabSheet = false }
        )
    }

    if (showNoteBottomSheet && currentHadith != null) {
        NoteBottomSheet(
            initialNote = currentNote,
            bookTitle = "${currentHadith.bookName} No. ${currentHadith.number}",
            onSaveNote = { content ->
                viewModel.saveNote(bookSlug, currentHadith.number, content)
                Toast.makeText(context, "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show()
            },
            onDeleteNote = {
                viewModel.saveNote(bookSlug, currentHadith.number, "")
                Toast.makeText(context, "Catatan dihapus", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showNoteBottomSheet = false }
        )
    }

    if (showSelectFolderSheet && currentHadith != null) {
        SelectFolderBottomSheet(
            hadithTitle = "${currentHadith.bookName} No. ${currentHadith.number}",
            availableFolders = availableFolders,
            currentFolders = currentBookmarkFolder,
            isAlreadyBookmarked = isBookmarked,
            onSaveToFolders = { chosenFolders ->
                viewModel.saveBookmarkToFolder(currentHadith, bookSlug, chosenFolders)
                Toast.makeText(context, "Disimpan ke ${chosenFolders.size} koleksi", Toast.LENGTH_SHORT).show()
            },
            onRemoveBookmark = {
                viewModel.removeBookmark(bookSlug, currentHadith.number)
                Toast.makeText(context, "Dihapus dari Bookmark", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showSelectFolderSheet = false }
        )
    }

    if (showShareOptionsSheet && currentHadith != null) {
        ModalBottomSheet(
            onDismissRequest = { showShareOptionsSheet = false },
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
                    text = "Bagikan Hadits",
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
                        showShareOptionsSheet = false
                        showQuoteCardDialog = true
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
                        showShareOptionsSheet = false
                        shareAsPlainText()
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

    if (showQuoteCardDialog && currentHadith != null) {
        QuoteCardDialog(
            hadith = currentHadith,
            onDismiss = { showQuoteCardDialog = false }
        )
    }

    if (showInfoDialog && currentHadith != null) {
        HadithInfoDialog(
            hadith = currentHadith,
            onDismiss = { showInfoDialog = false }
        )
    }

    if (showAiBottomSheet && currentHadith != null) {
        HadithAiBottomSheet(
            hadith = currentHadith,
            aiState = aiResponseState,
            hasApiKey = settingsState.geminiApiKey.isNotBlank(),
            onAskAi = { prompt ->
                viewModel.askAi(currentHadith, prompt)
            },
            onResetAi = {
                viewModel.resetAiState()
            },
            onNavigateToSettings = {
                showAiBottomSheet = false
                onNavigateToSettings?.invoke()
            },
            onDismiss = {
                showAiBottomSheet = false
            }
        )
    }

    if (showJumpDialog) {
        val displayTotal = when {
            isThemeMode && themeName != null -> HadithClassificationMetadata.getTotalCount(themeName)
            isCategoryMode && categoryName != null -> HadithClassificationMetadata.getTotalCount(categoryName)
            isPerawiMode -> perawiSession?.hadithNumbers?.size ?: 0
            isSearchMode -> 0
            else -> currentHadith?.totalHadith ?: 0
        }
        val displayCurrentNumber = when {
            isThemeMode || isCategoryMode -> activeVMember
            isPerawiMode -> (perawiSession?.currentIndex ?: 0) + 1
            isSearchMode -> searchCurrentIndex + 1
            else -> currentHadith?.number ?: 1
        }
        JumpToHadithDialog(
            currentNumber = displayCurrentNumber,
            totalHadith = displayTotal,
            onConfirm = { targetNum ->
                showJumpDialog = false
                when {
                    isThemeMode || isCategoryMode -> {
                        activeVMember = targetNum
                    }
                    isPerawiMode -> {
                        viewModel.loadPerawiItemAt(targetNum - 1)
                    }
                    isSearchMode -> {
                        // Tidak berpindah lompat di search mode
                    }
                    else -> {
                        viewModel.loadHadith(bookSlug, targetNum)
                    }
                }
            },
            onDismiss = { showJumpDialog = false }
        )
    }
}
