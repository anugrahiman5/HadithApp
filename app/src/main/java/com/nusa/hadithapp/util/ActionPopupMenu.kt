package com.nusa.hadithapp.util

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

private val CYAN_COLOR = Color(0xFF00A3E0)
private val BG_COLOR = Color(0xFF222222)

/**
 * Popup menu untuk aksi hadith (biography, bandingkan hadis, info)
 * @param expanded Menu visible state
 * @param onDismiss Callback saat menu dismiss
 * @param onBiographyClick Callback klik Biography
 * @param onCompareClick Callback klik Bandingkan Hadis
 * @param onInfoClick Callback klik Info
 */
@Composable
fun ActionPopupMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
//    onNoteClick: () -> Unit,
    onBiographyClick: () -> Unit,
    onCompareClick: (() -> Unit)? = null,
    onAiClick: (() -> Unit)? = null,
//    onAccountClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(BG_COLOR)
    ) {
        if (onAiClick != null) {
            PopupMenuItem(icon = Icons.Default.AutoAwesome, title = "Tanya Syarah AI", cyanColor = CYAN_COLOR) {
                onDismiss()
                onAiClick()
            }
        }
//        PopupMenuItem(icon = Icons.Default.Chat, title = "Catatan", cyanColor = cyanColor) {
//            onDismiss()
//            onNoteClick()
//        }
        PopupMenuItem(icon = Icons.Default.Badge, title = "Biografi", cyanColor = CYAN_COLOR) {
            onDismiss()
            onBiographyClick()
        }
        if (onCompareClick != null) {
            PopupMenuItem(icon = Icons.AutoMirrored.Filled.CompareArrows, title = "Bandingkan Hadis", cyanColor = CYAN_COLOR) {
                onDismiss()
                onCompareClick()
            }
        }
//        PopupMenuItem(icon = Icons.Default.AccountCircle, title = "Akun", cyanColor = cyanColor) {
//            onDismiss()
//            onAccountClick()
//        }
        PopupMenuItem(icon = Icons.Default.Info, title = "Info", cyanColor = CYAN_COLOR) {
            onDismiss()
            onInfoClick()
        }
    }
}

@Composable
private fun PopupMenuItem(
    icon: ImageVector,
    title: String,
    cyanColor: Color,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = title, color = Color.White) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = title, tint = cyanColor) },
        onClick = onClick
    )
}
