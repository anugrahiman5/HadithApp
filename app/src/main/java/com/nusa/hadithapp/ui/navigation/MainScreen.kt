package com.nusa.hadithapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nusa.hadithapp.ui.classification.category.CategoryResultScreen
import com.nusa.hadithapp.ui.detail.HadithDetailScreen
import com.nusa.hadithapp.ui.classification.theme.ThemeResultScreen
import com.nusa.hadithapp.ui.home.HomeScreen
import com.nusa.hadithapp.ui.search.SearchScreen
import com.nusa.hadithapp.ui.classification.category.CategoryResultViewModel
import com.nusa.hadithapp.ui.detail.HadithViewModel
import com.nusa.hadithapp.ui.classification.theme.ThemeResultViewModel
import com.nusa.hadithapp.ui.explore.ChapterBrowserScreen
import com.nusa.hadithapp.ui.explore.ChapterBrowserViewModel
import com.nusa.hadithapp.ui.home.HomeViewModel
import com.nusa.hadithapp.ui.saved.SavedScreen
import com.nusa.hadithapp.ui.saved.SavedViewModel
import com.nusa.hadithapp.ui.saved.editor.EditStudyNoteScreen
import com.nusa.hadithapp.ui.saved.editor.StudyNoteEditorViewModel
import com.nusa.hadithapp.ui.search.SearchViewModel

@Composable
fun MainScreen(
    navTargetBookSlug: String? = null,
    navTargetHadithNumber: Int = -1,
    onNavTargetConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()

    LaunchedEffect(navTargetBookSlug, navTargetHadithNumber) {
        if (!navTargetBookSlug.isNullOrBlank() && navTargetHadithNumber > 0) {
            navController.navigate("hadith_detail/$navTargetBookSlug/$navTargetHadithNumber")
            onNavTargetConsumed()
        }
    }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Explore,
        BottomNavItem.Saved
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationRoute = navBackStackEntry?.destination?.route

    val isBottomBarVisible = items.any { item ->
        currentDestinationRoute?.startsWith(item.route) == true
    }

    Scaffold(bottomBar = {
        if (isBottomBarVisible) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                items.forEach { item ->
                    val labelText = stringResource(id = item.titleResId)
                    val isSelected = currentDestinationRoute?.startsWith(item.route) == true

                    NavigationBarItem(
                        icon = { Icon(item.iconSelected, contentDescription = labelText) },
                        label = { Text(text = labelText) },
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    onBookClick = { book ->
                        navController.navigate("hadith_detail/${book.slug}/1")
                    },
                    onContinueReadClick = { lastRead ->
                        val slug = lastRead.bookSlug
                        navController.navigate("hadith_detail/$slug/${lastRead.number}")
                    },
                    onHadithOfTheDayClick = { slug, number ->
                        navController.navigate("hadith_detail/$slug/$number")
                    },
                    onThemeClick = { theme ->
                        navController.navigate("theme_result/${theme.className}")
                    },
                    onCategoryClick = { category ->
                        navController.navigate("category_result/${category.className}")
                    },
                    onSearchTriggered = { queryText ->
                        val cleanquery = queryText.trim()
                        val encodeQuery = android.net.Uri.encode(cleanquery)
                        val searchRoute = if (encodeQuery.isNotBlank()){
                            "${BottomNavItem.Search.route}?query=$encodeQuery&tab=0"
                        } else {
                            BottomNavItem.Search.route
                        }
                        navController.navigate(searchRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = encodeQuery.isBlank()
                        }
                    },
                    onRawiSearchClick = {
                        navController.navigate("${BottomNavItem.Search.route}?tab=1") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onBiographyClick = { imamKey ->
                        navController.navigate("biography?imamKey=$imamKey")
                    },
                    onMusthalahClick = {
                        navController.navigate("musthalah")
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    }
                )
            }
            composable(
                route = "${BottomNavItem.Search.route}?query={query}&tab={tab}",
                arguments = listOf(
                    navArgument("query") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("tab") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                val rawQuery = backStackEntry.arguments?.getString("query")
                val tab = backStackEntry.arguments?.getInt("tab") ?: 0
                val initialQuery = rawQuery?.let { android.net.Uri.decode(it) }

                val searchViewModel: SearchViewModel = hiltViewModel()

                LaunchedEffect(initialQuery, tab) {
                    searchViewModel.setTab(tab)
                    if (!initialQuery.isNullOrBlank()){
                        searchViewModel.updateQuery(initialQuery)
                        if (tab == 1) {
                            searchViewModel.searchRawi(initialQuery)
                        } else {
                            searchViewModel.search(initialQuery)
                        }
                    } else if (tab == 1) {
                        searchViewModel.updateQuery("")
                    }
                }
                SearchScreen(
                    viewModel = searchViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onHadithClick = { bookSlug, number, searchIndex ->
                        navController.navigate("hadith_detail/$bookSlug/$number?searchIndex=$searchIndex")
                    },
                    onPerawiHadithClick = { bookSlug, number, kodeRawi, rawiName ->
                        val enc = android.net.Uri.encode(rawiName)
                        navController.navigate("hadith_detail/$bookSlug/$number?perawiKode=$kodeRawi&perawiName=$enc")
                    }
                )
            }
            
            composable(BottomNavItem.Explore.route) {
                val chapterViewModel: ChapterBrowserViewModel = hiltViewModel()
                ChapterBrowserScreen(
                    viewModel = chapterViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHadith = { bookSlug, hadithNumber ->
                        navController.navigate("hadith_detail/$bookSlug/$hadithNumber")
                    }
                )
            }

            composable(BottomNavItem.Saved.route) {
                val savedViewModel: SavedViewModel = hiltViewModel()
                SavedScreen(
                    viewModel = savedViewModel,
                    onNavigateToDetail = { bookSlug, number ->
                        navController.navigate("hadith_detail/$bookSlug/$number")
                    },
                    onNavigateToEditor = { noteId, bookSlug, number ->
                        val route = when {
                            !noteId.isNullOrBlank() -> "edit_study_note?noteId=$noteId"
                            !bookSlug.isNullOrBlank() && number != null -> "edit_study_note?bookSlug=$bookSlug&number=$number"
                            else -> "edit_study_note"
                        }
                        navController.navigate(route)
                    }
                )
            }

            //Rute Detail Hadits
            composable(
                route = "hadith_detail/{bookSlug}/{number}?vMember={vMember}&themeName={themeName}&categoryName={categoryName}&searchIndex={searchIndex}&perawiKode={perawiKode}&perawiName={perawiName}",
                arguments = listOf(
                    navArgument("bookSlug") { type = NavType.StringType },
                    navArgument("number") { type = NavType.IntType },
                    navArgument("vMember") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument("themeName"){
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("categoryName") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("searchIndex"){ type = NavType.IntType; defaultValue = -1 },
                    navArgument("perawiKode"){ type = NavType.IntType; defaultValue = -1 },
                    navArgument("perawiName"){
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val bookSlug = backStackEntry.arguments?.getString("bookSlug") ?: "ShahihBukhari"
                val number = backStackEntry.arguments?.getInt("number") ?: 1
                val vMember = backStackEntry.arguments?.getInt("vMember") ?: -1
                val themeName = backStackEntry.arguments?.getString("themeName")
                val categoryName = backStackEntry.arguments?.getString("categoryName")
                val searchIndex = backStackEntry.arguments?.getInt("searchIndex") ?: -1
                val perawiKode = backStackEntry.arguments?.getInt("perawiKode") ?: -1
                val perawiName = backStackEntry.arguments?.getString("perawiName")?.let { android.net.Uri.decode(it) }
                val viewModel: HadithViewModel = hiltViewModel()

                HadithDetailScreen(
                    bookSlug = bookSlug,
                    number = number,
                    vMember = vMember,
                    themeName = themeName,
                    categoryName = categoryName,
                    searchIndex = searchIndex,
                    perawiKode = perawiKode,
                    perawiName = perawiName,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHadith = { targetSlug, targetNumber, targetKode, targetName ->
                        val enc = android.net.Uri.encode(targetName)
                        navController.navigate("hadith_detail/$targetSlug/$targetNumber?perawiKode=$targetKode&perawiName=$enc")
                    },
                    onNavigateToBiography = { slug ->
                        navController.navigate("biography?imamKey=$slug")
                    },
                    onNavigateToMusthalah = { termId ->
                        navController.navigate("musthalah?termId=$termId")
                    },
                    onNavigateToCompare = { slug, num ->
                        navController.navigate("hadith_compare/$slug/$num")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
            }

            //Rute Hasil Tema
            composable(
                route = "theme_result/{themeName}",
                arguments = listOf(
                    navArgument("themeName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                    val themeName = backStackEntry.arguments?.getString("themeName") ?: ""
                    val viewModel: ThemeResultViewModel = hiltViewModel()

                    ThemeResultScreen(
                        themeName = themeName,
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onHadithClick = { slug, number, vMember, currentTheme ->
                            navController.navigate("hadith_detail/$slug/$number?vMember=$vMember&themeName=$currentTheme")
                        }
                    )
                }

            //Rute Hasil Terminology
            composable("category_result/{categoryName}") { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                val viewModel: CategoryResultViewModel = hiltViewModel()

                CategoryResultScreen(
                    categoryName = categoryName,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onHadithClick = { bookSlug, number, vMember, categoryName ->
                        navController.navigate("hadith_detail/$bookSlug/$number?vMember=$vMember&categoryName=$categoryName")
                    }
                )
            }

            // 🟢 Rute Layar Editor Kajian
            composable(
                route = "edit_study_note?noteId={noteId}&bookSlug={bookSlug}&number={number}",
                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("bookSlug") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("number") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                val editorViewModel: StudyNoteEditorViewModel = hiltViewModel()
                EditStudyNoteScreen(
                    viewModel = editorViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("settings") {
                val settingsViewModel: com.nusa.hadithapp.ui.settings.SettingsViewModel = hiltViewModel()
                com.nusa.hadithapp.ui.settings.SettingsScreen(
                    viewModel = settingsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("profile") {
                val profileViewModel: com.nusa.hadithapp.ui.profile.ProfileViewModel = hiltViewModel()
                com.nusa.hadithapp.ui.profile.ProfileScreen(
                    viewModel = profileViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHadith = { bookSlug, number ->
                        navController.navigate("hadith_detail/$bookSlug/$number")
                    }
                )
            }

            composable(
                route = "biography?imamKey={imamKey}",
                arguments = listOf(
                    navArgument("imamKey") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = "Bukhari"
                    }
                )
            ) { backStackEntry ->
                val imamKey = backStackEntry.arguments?.getString("imamKey") ?: "Bukhari"
                val biographyViewModel: com.nusa.hadithapp.ui.biography.BiographyViewModel = hiltViewModel()

                LaunchedEffect(imamKey) {
                    if (imamKey.isNotBlank()) {
                        biographyViewModel.selectImam(imamKey)
                    }
                }

                com.nusa.hadithapp.ui.biography.BiographyScreen(
                    viewModel = biographyViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHadith = { bookSlug, number ->
                        navController.navigate("hadith_detail/$bookSlug/$number")
                    },
                    onOpenChapters = {
                        navController.navigate(BottomNavItem.Explore.route)
                    }
                )
            }

            composable(
                route = "musthalah?termId={termId}",
                arguments = listOf(
                    navArgument("termId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val termId = backStackEntry.arguments?.getString("termId")
                val musthalahViewModel: com.nusa.hadithapp.ui.musthalah.MusthalahViewModel = hiltViewModel()

                LaunchedEffect(termId) {
                    if (!termId.isNullOrBlank()) {
                        musthalahViewModel.selectTermById(termId)
                    }
                }

                com.nusa.hadithapp.ui.musthalah.MusthalahScreen(
                    viewModel = musthalahViewModel,
                    initialTermId = termId,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToCategory = { categoryClassName ->
                        navController.navigate("category_result/$categoryClassName")
                    }
                )
            }

            // Rute Banding Hadits (Rujukan Paralel & Komparasi Matan)
            composable(
                route = "hadith_compare/{bookSlug}/{number}",
                arguments = listOf(
                    navArgument("bookSlug") { type = NavType.StringType },
                    navArgument("number") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val bookSlug = backStackEntry.arguments?.getString("bookSlug") ?: "ShahihBukhari"
                val number = backStackEntry.arguments?.getInt("number") ?: 1
                val compareViewModel: com.nusa.hadithapp.ui.compare.HadithCompareViewModel = hiltViewModel()

                LaunchedEffect(bookSlug, number) {
                    compareViewModel.loadComparison(bookSlug, number)
                }

                com.nusa.hadithapp.ui.compare.HadithCompareScreen(
                    viewModel = compareViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHadithDetail = { targetSlug, targetNumber ->
                        navController.navigate("hadith_detail/$targetSlug/$targetNumber")
                    }
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    }
}
