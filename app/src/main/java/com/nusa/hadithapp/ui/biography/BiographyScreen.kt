package com.nusa.hadithapp.ui.biography

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nusa.hadithapp.domain.model.biography.BiographyTab
import com.nusa.hadithapp.ui.biography.components.ApiNarrativeSection
import com.nusa.hadithapp.ui.biography.components.BiographyTopBar
import com.nusa.hadithapp.ui.biography.components.BottomActionCta
import com.nusa.hadithapp.ui.biography.components.HeroHeaderCard
import com.nusa.hadithapp.ui.biography.components.ImamSelectorBar
import com.nusa.hadithapp.ui.biography.components.MajorWorksSection
import com.nusa.hadithapp.ui.biography.components.MethodologySection
import com.nusa.hadithapp.ui.biography.components.MetricsSection
import com.nusa.hadithapp.ui.biography.components.ScholarQuotesSection
import com.nusa.hadithapp.ui.biography.components.SubTabSwitcher
import com.nusa.hadithapp.ui.biography.components.TimelineSection
import com.nusa.hadithapp.util.FontSettingsBottomSheet
import kotlinx.coroutines.launch

@Composable
fun BiographyScreen(
    viewModel: BiographyViewModel,
    onBackClick: () -> Unit,
    onNavigateToHadith: (bookSlug: String, number: Int) -> Unit,
    onOpenChapters: (bookSlug: String) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val fontSettings by viewModel.fontSettingsFlow.collectAsState()
    val favoriteImams by viewModel.favoriteImamsFlow.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showFontBottomSheet by remember { mutableStateOf(false) }

    val bio = uiState.currentBiography
    val isCurrentBookmarked = remember(bio, favoriteImams) {
        bio != null && favoriteImams.contains(bio.id)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                BiographyTopBar(
                    isBookmarked = isCurrentBookmarked,
                    onBackClick = onBackClick,
                    onFormatClick = { showFontBottomSheet = true },
                    onShareClick = {
                        bio?.let {
                            val shareText = """
                                📜 Biografi ${it.shortName}
                                ${it.fullNameLatin} (${it.periodHijri})
                                
                                ✨ ${it.titles.firstOrNull() ?: ""}
                                📍 Lahir & Wafat: ${it.birthAndDeathPlace}
                                📚 Karya Utama: ${it.majorWorks.firstOrNull()?.titleLatin ?: ""} (${it.metrics.totalHadithsDisplay} Hadis)
                                
                                Disarikan dari Ensiklopedia Hadits
                            """.trimIndent()
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Bagikan Biografi Imam"))
                        }
                    },
                    onBookmarkClick = {
                        bio?.let { currentBio ->
                            viewModel.toggleFavoriteCurrentImam { isFav ->
                                val msg = if (isFav) {
                                    "Biografi ${currentBio.shortName} disimpan ke favorit"
                                } else {
                                    "Biografi ${currentBio.shortName} dihapus dari favorit"
                                }
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )

                ImamSelectorBar(
                    imams = uiState.allImams,
                    selectedImamId = uiState.selectedImamId,
                    onImamSelect = { imamId ->
                        viewModel.selectImam(imamId)
                        scope.launch { listState.animateScrollToItem(0) }
                    }
                )
            }
        },
        bottomBar = {
            bio?.let { currentBio ->
                BottomActionCta(
                    bio = currentBio,
                    onOpenBookClick = { slug ->
                        onNavigateToHadith(slug, 1)
                    },
                    onOpenChaptersClick = { slug ->
                        onOpenChapters(slug)
                    },
                    onOpenTeachersClick = {
                        scope.launch {
                            // Scroll ke seksi guru dan murid di bagian bawah
                            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (bio == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Hero Card
                item {
                    HeroHeaderCard(bio = bio)
                }

                // 2. Sub-tab Selector
                item {
                    SubTabSwitcher(
                        selectedTab = uiState.selectedTab,
                        onTabSelect = { tab ->
                            viewModel.selectTab(tab)
                        }
                    )
                }

                // Dynamic content based on Tab
                when (uiState.selectedTab) {
                    BiographyTab.SUMMARY -> {
                        item { MetricsSection(metrics = bio.metrics) }
                        item { TimelineSection(timelineNodes = bio.timeline) }
                        item { MethodologySection(items = bio.methodology) }
                        item {
                            MajorWorksSection(
                                imamShortName = bio.shortName,
                                works = bio.majorWorks,
                                onOpenBookClick = { onNavigateToHadith(bio.targetBookSlug, 1) }
                            )
                        }
                        item { ScholarQuotesSection(quotes = bio.scholarTestimonials) }
                        item {
                            ApiNarrativeSection(
                                teachers = bio.parsedTeachers,
                                students = bio.parsedStudents,
                                rawApiText = bio.apiRawText
                            )
                        }
                    }

                    BiographyTab.TIMELINE -> {
                        item { TimelineSection(timelineNodes = bio.timeline) }
                        item {
                            ApiNarrativeSection(
                                teachers = bio.parsedTeachers,
                                students = bio.parsedStudents,
                                rawApiText = bio.apiRawText
                            )
                        }
                    }

                    BiographyTab.WORKS -> {
                        item { MetricsSection(metrics = bio.metrics) }
                        item {
                            MajorWorksSection(
                                imamShortName = bio.shortName,
                                works = bio.majorWorks,
                                onOpenBookClick = { onNavigateToHadith(bio.targetBookSlug, 1) }
                            )
                        }
                    }

                    BiographyTab.METHODOLOGY -> {
                        item { MethodologySection(items = bio.methodology) }
                        item { ScholarQuotesSection(quotes = bio.scholarTestimonials) }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

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
}
