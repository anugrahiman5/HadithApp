package com.nusa.hadithapp.ui.saved.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.Bookmark
import com.nusa.hadithapp.util.BookSlugUtil
import com.nusa.hadithapp.util.FontSettingsState
import com.nusa.hadithapp.util.extractArabicMatan
import com.nusa.hadithapp.util.extractMatanOnly
import com.nusa.hadithapp.util.extractPerawiNames
import com.nusa.hadithapp.util.formatBookmarkForSharing
import com.nusa.hadithapp.util.formatMarkdownContent
import com.nusa.hadithapp.util.getArabicFontFamily
import com.nusa.hadithapp.util.getIndonesianFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookmarkItemCard(
    bookmark: Bookmark,
    fontSettings: FontSettingsState = FontSettingsState(),
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoveFolderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormatter = SimpleDateFormat("d MMM yyyy", Locale("id", "ID"))
    val formattedDate = dateFormatter.format(Date(bookmark.createdAt))

    // Warna Badge Derajat Hadits
    val isShahih = bookmark.grade.contains("Shahih", ignoreCase = true)
    val badgeBgColor = if (isShahih) Color(0xFF2E7D32).copy(0.2f) else Color(0xFF00897B).copy(0.2f)
    val badgeTextColor = if (isShahih) Color(0xFF81C784) else Color(0xFF80CBC4)
    val officialBookName = remember(bookmark.bookSlug, bookmark.bookName) {
        BookSlugUtil.getOfficialBookName(
            if (bookmark.bookSlug.isNotBlank()) bookmark.bookSlug
            else bookmark.bookName
        )
    }

    var bookmarkMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Header: Folder Tag + Badge Derajat + Ikon Bookmark & Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tag Folder (Koleksi)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        val primaryFolder = bookmark.folderTags.firstOrNull() ?: "Umum"
                        val extractFolderCount = if (bookmark.folderTags.size > 1) " (+${bookmark.folderTags.size - 1})" else ""
                        Text(
                            text = "📁 $primaryFolder$extractFolderCount",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Badge Derajat Ulama
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = badgeBgColor
                    ) {
                        Text(
                            text = bookmark.grade.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Tersimpan",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Box {

                        IconButton(
                            onClick = { bookmarkMenuExpanded = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Opsi",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = bookmarkMenuExpanded,
                            onDismissRequest = { bookmarkMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Pindahkan ke Folder...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Folder,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    bookmarkMenuExpanded = false
                                    onMoveFolderClick()
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Salin Hadits") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    bookmarkMenuExpanded = false
                                    val clipboard =
                                        context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText(
                                        "Hadits",
                                        "${bookmark.arabicText}\n\n${bookmark.translation}"
                                    )
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(
                                        context,
                                        "Hadits berhasil disalin",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Bagikan Hadits") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    bookmarkMenuExpanded = false
                                    val shareText = formatBookmarkForSharing(bookmark)
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(
                                        Intent.createChooser(
                                            sendIntent,
                                            "Bagikan Hadits"
                                        )
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Hapus dari Bookmark",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    bookmarkMenuExpanded = false
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            }

            // 2. Judul Hadits (Nama Kitab + Nomor)
            Text(
                text = "$officialBookName No. ${bookmark.number}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 3. Teks Arab
            val matanArabic = remember(bookmark.arabicText) {
                extractArabicMatan(bookmark.arabicText)
            }
            Text(
                text = matanArabic,
                fontFamily = getArabicFontFamily(fontSettings.arabicFontName),
                fontSize = fontSettings.arabicFontSize.sp,
                lineHeight = (fontSettings.arabicFontSize * 1.6f).sp,
                textAlign = TextAlign.Center,
                fontWeight = fontSettings.arabicFontWeight,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Ornamen Berlian ❖
            Text(
                text = "❖",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // 4. Terjemahan Indonesia
            val matanTranslation = remember(bookmark.translation) {
                extractMatanOnly(bookmark.translation)
            }
            Text(
                text = "\"$matanTranslation\"",
                fontFamily = getIndonesianFontFamily(fontSettings.indonesianFontName),
                fontSize = fontSettings.indonesianFontSize.sp,
                lineHeight = (fontSettings.indonesianFontSize * 1.5f).sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // 5. Jalur Transmisi Sanad (Opsional/Jika Tersedia)
            val sanadDisplay = remember(bookmark.sanadSnippet, bookmark.translation) {
                if (bookmark.sanadSnippet.isBlank() || bookmark.sanadSnippet.contains("➔ , ➔") || bookmark.sanadSnippet.startsWith(
                        "➔"
                    )
                ) {
                    val extracted = extractPerawiNames(bookmark.translation)
                    if (extracted.isNotEmpty()) extracted.joinToString(" ➔ ") else ""
                } else {
                    bookmark.sanadSnippet
                }
            }
            if (sanadDisplay.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "JALUR TRANSMISI SANAD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = bookmark.sanadSnippet,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 6. Catatan Tersemat Pengguna (Jika Ada)
            if (bookmark.userNote.isNotBlank()) {
                val formattedUserNote = remember(bookmark.userNote){
                    formatMarkdownContent(bookmark.userNote)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                ) {
                    Row(modifier = Modifier.padding(10.dp)) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "📌 Catatan Kajian: ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formattedUserNote,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 0.8.dp
            )

            // 7. Footer: Tanggal Simpan + Tombol Buka Hadis
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📅 Disimpan $formattedDate",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Buka Hadis",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}