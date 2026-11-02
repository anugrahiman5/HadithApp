package com.nusa.hadithapp.ui.detail.components

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.nusa.hadithapp.domain.model.Hadith
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

enum class CardTheme(val label: String, val bgStart: Color, val bgEnd: Color, val accentColor: Color) {
    EMERALD("Emerald", Color(0xFF0D3323), Color(0xFF184E38), Color(0xFF55D6A0)),
    MIDNIGHT("Midnight", Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF60A5FA)),
    MAROON("Maroon", Color(0xFF3B1219), Color(0xFF561D27), Color(0xFFF87171)),
    SLATE("Slate", Color(0xFF1E1E1E), Color(0xFF2D2D2D), Color(0xFFE2E8F0))
}

enum class CardRatio(val label: String, val badge: String) {
    FEED("1:1 Persegi", "Feed"),
    STORY("9:16 Vertikal", "WA / IG Story")
}

enum class CardContentMode(val label: String) {
    FULL("Lengkap"),
    ARABIC_ONLY("Arab Saja"),
    TRANSLATION_ONLY("Arti Saja")
}

data class QuoteCardData(
    val bookName: String,
    val number: Int,
    val arabicText: String,
    val translation: String,
    val grade: String
)

@Composable
fun QuoteCardDialog(
    hadith: Hadith,
    onDismiss: () -> Unit
) {
    QuoteCardDialog(
        data = QuoteCardData(
            bookName = hadith.bookName,
            number = hadith.number,
            arabicText = hadith.arabicText,
            translation = hadith.translation,
            grade = hadith.grade
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun QuoteCardDialog(
    data: QuoteCardData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var selectedTheme by remember { mutableStateOf(CardTheme.EMERALD) }
    var selectedRatio by remember { mutableStateOf(CardRatio.FEED) }
    var selectedContentMode by remember { mutableStateOf(CardContentMode.FULL) }
    var isCapturing by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bagikan Kartu Hadits",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Unified Format & Palette Bar (Single clean row)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ratio / Format Toggle
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardRatio.values().forEach { ratio ->
                            val isSelected = ratio == selectedRatio
                            Surface(
                                onClick = { selectedRatio = ratio },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Text(
                                    text = when (ratio) {
                                        CardRatio.FEED -> "Feed 1:1"
                                        CardRatio.STORY -> "Story 9:16"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Color Palette Selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardTheme.values().forEach { theme ->
                            val isSelected = theme == selectedTheme
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(theme.bgStart)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                                    .clickable { selectedTheme = theme },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(theme.accentColor)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Content Mode Selector (Lengkap / Arab Saja / Arti Saja)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Konten:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    CardContentMode.values().forEach { mode ->
                        val isSelected = mode == selectedContentMode
                        Surface(
                            onClick = { selectedContentMode = mode },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary) else null
                        ) {
                            Text(
                                text = mode.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Preview Container (TopCenter alignment guarantees no cut-off header)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val arabicLen = data.arabicText.length
                    val transLen = data.translation.length
                    val isVeryLong = arabicLen > 450 || transLen > 450
                    val isLong = arabicLen > 200 || transLen > 240

                    val arabicFontSize = when {
                        isVeryLong -> 13.5.sp
                        isLong -> 15.sp
                        else -> if (selectedRatio == CardRatio.STORY) 17.sp else 19.sp
                    }
                    val arabicLineHeight = when {
                        isVeryLong -> 23.sp
                        isLong -> 26.sp
                        else -> if (selectedRatio == CardRatio.STORY) 30.sp else 34.sp
                    }
                    val transFontSize = when {
                        isVeryLong -> 10.5.sp
                        isLong -> 11.5.sp
                        else -> if (selectedRatio == CardRatio.STORY) 12.sp else 13.sp
                    }
                    val transLineHeight = when {
                        isVeryLong -> 16.sp
                        isLong -> 17.5.sp
                        else -> if (selectedRatio == CardRatio.STORY) 19.sp else 21.sp
                    }

                    // Rendered Card captured via graphicsLayer
                    Card(
                        modifier = Modifier
                            .width(if (selectedRatio == CardRatio.STORY) 290.dp else 320.dp)
                            .heightIn(min = if (selectedRatio == CardRatio.STORY) 515.dp else 320.dp)
                            .drawWithContent {
                                graphicsLayer.record {
                                    this@drawWithContent.drawContent()
                                }
                                drawLayer(graphicsLayer)
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(selectedTheme.bgStart, selectedTheme.bgEnd)
                                    )
                                )
                                .padding(horizontal = 18.dp, vertical = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Header section
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = data.bookName.uppercase(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = selectedTheme.accentColor,
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = "NO. ${data.number}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                                    if (selectedRatio == CardRatio.STORY) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Serif,
                                            color = selectedTheme.accentColor.copy(alpha = 0.9f)
                                        )
                                    }
                                }

                                // Middle section: Arabic and Translation based on selectedContentMode
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (selectedContentMode != CardContentMode.TRANSLATION_ONLY) {
                                        val customArabicSize = if (selectedContentMode == CardContentMode.ARABIC_ONLY) {
                                            (arabicFontSize.value + 3f).sp
                                        } else arabicFontSize
                                        val customArabicLineHeight = if (selectedContentMode == CardContentMode.ARABIC_ONLY) {
                                            (arabicLineHeight.value + 6f).sp
                                        } else arabicLineHeight

                                        Text(
                                            text = data.arabicText,
                                            color = Color.White,
                                            fontSize = customArabicSize,
                                            lineHeight = customArabicLineHeight,
                                            textAlign = TextAlign.Right,
                                            fontFamily = FontFamily.Serif,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                    if (selectedContentMode == CardContentMode.FULL) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "✦ ✦ ✦",
                                            fontSize = 10.sp,
                                            color = selectedTheme.accentColor.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }

                                    if (selectedContentMode != CardContentMode.ARABIC_ONLY) {
                                        val customTransSize = if (selectedContentMode == CardContentMode.TRANSLATION_ONLY) {
                                            (transFontSize.value + 2f).sp
                                        } else transFontSize
                                        val customTransLineHeight = if (selectedContentMode == CardContentMode.TRANSLATION_ONLY) {
                                            (transLineHeight.value + 3.5f).sp
                                        } else transLineHeight

                                        Text(
                                            text = "\"${data.translation}\"",
                                            color = Color.White.copy(alpha = 0.92f),
                                            fontSize = customTransSize,
                                            lineHeight = customTransLineHeight,
                                            textAlign = TextAlign.Left,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                // Footer with Grade and App branding
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                selectedTheme.accentColor.copy(alpha = 0.2f),
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = data.grade,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = selectedTheme.accentColor
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "HadithApp",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.85f),
                                            letterSpacing = 1.sp
                                        )
                                        if (selectedRatio == CardRatio.STORY) {
                                            Text(
                                                text = "Kajian Hadits Sahih",
                                                fontSize = 8.sp,
                                                color = Color.White.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (isCapturing) return@OutlinedButton
                            isCapturing = true
                            coroutineScope.launch {
                                try {
                                    val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                    saveBitmapToGallery(context, bitmap)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isCapturing = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simpan", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            if (isCapturing) return@Button
                            isCapturing = true
                            coroutineScope.launch {
                                try {
                                    val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                    shareBitmap(context, bitmap)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isCapturing = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isCapturing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bagikan", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

private fun shareBitmap(context: Context, bitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "hadith_quote_${System.currentTimeMillis()}.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Bagikan Kartu Hadits"))
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal membagikan gambar: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
    try {
        val filename = "Hadith_${System.currentTimeMillis()}.png"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HadithApp")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            }
            Toast.makeText(context, "Gambar berhasil disimpan ke Galeri", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal menyimpan gambar: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
