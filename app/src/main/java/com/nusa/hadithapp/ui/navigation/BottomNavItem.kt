package com.nusa.hadithapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.nusa.hadithapp.R

/**
 * Sealed class yang merepresentasikan item navigasi bottom bar
 * @property route Path navigasi unik
 * @property titleResId String resource ID untuk title (localization)
 * @property iconSelected Icon ketika item aktif
 * @property iconUnselected Icon ketika item tidak aktif
 * @property index Urutan item di bottom bar
 */
sealed class BottomNavItem(
    val route: String,
    val titleResId: Int,        // Resource ID string, bukan hardcoded
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val index: Int
) {
    companion object {
        /**
         * List semua bottom nav items dengan urutan yang sudah ditentukan
         */
        val items: List<BottomNavItem> = listOf(
            Home,
            Search,
            Explore,
            Saved
        )

        /**
         * Get item berdasarkan route
         */
        fun getItemByRoute(route: String): BottomNavItem? {
            return items.find { it.route == route }
        }

        /**
         * Validasi apakah route adalah valid bottom nav route
         */
        fun isValidRoute(route: String): Boolean {
            return items.any { it.route == route }
        }
    }

    /**
     * Home - Layar beranda dengan hadis hari ini & kategori
     */
    object Home : BottomNavItem(
        route = "home",
        titleResId = R.string.nav_home,  // "Beranda"
        iconSelected = Icons.Default.Home,
        iconUnselected = Icons.Outlined.Home,
        index = 0
    )

    /**
     * Search - Pencarian hadis
     */
    object Search : BottomNavItem(
        route = "search",
        titleResId = R.string.nav_search,  // "Cari"
        iconSelected = Icons.Default.Search,
        iconUnselected = Icons.Outlined.Search,
        index = 1
    )

    /**
     * Explore - Jelajahi tema dan kategori hadis
     */
    object Explore : BottomNavItem(
        route = "explore",
        titleResId = R.string.nav_explore,  // "Eksplorasi"
        iconSelected = Icons.Default.GridView,
        iconUnselected = Icons.Outlined.GridView,
        index = 2
    )

    /**
     * Saved - Daftar hadis yang disimpan/favorit
     */
    object Saved : BottomNavItem(
        route = "saved",
        titleResId = R.string.nav_saved,  // "Simpan"
        iconSelected = Icons.Default.Bookmarks,
        iconUnselected = Icons.Outlined.Bookmarks,
        index = 3
    )
}