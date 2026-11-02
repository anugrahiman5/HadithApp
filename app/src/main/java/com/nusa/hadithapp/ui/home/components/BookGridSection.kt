package com.nusa.hadithapp.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nusa.hadithapp.domain.model.HadithBook
import com.nusa.hadithapp.util.getArabicFontFamily
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BookGridSection(
    books: List<HadithBook>,
    onBookClick: (HadithBook) -> Unit,
    modifier: Modifier = Modifier
) {
    val orderedBooks = HadithBook.canonicalOrder

    val badgeBg = MaterialTheme.colorScheme.surfaceVariant
    val badgeTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = modifier.fillMaxWidth()) {
        // Header 14 Kitab Hadis & Badge Koleksi Muktabar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${orderedBooks.size} Kitab Hadis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Kutubut Tis'ah & Khamsah",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Badge Kanan: Koleksi Muktabar
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(badgeBg)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Koleksi Muktabar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = badgeTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid 2 Kolom Manual untuk scroll yang mulus di LazyColumn
        val chunkedBooks = orderedBooks.chunked(2)
        chunkedBooks.forEach { rowBooks ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowBooks.forEach { book ->
                    BookCard(
                        book = book,
                        onClick = { onBookClick(book) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowBooks.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BookCard(
    book: HadithBook,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val idLocale = Locale.Builder().setLanguage("id").setRegion("ID").build()
    val numberFormatter = NumberFormat.getNumberInstance(idLocale)
    val hadithCountFormatted = numberFormatter.format(book.totalHadith)

    val calligraphyBoxBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val calligraphyTextColor = MaterialTheme.colorScheme.primary

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Baris Atas: Dot Status (Hijau/Cokelat) & Tag Kitab
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(book.dotColor)
                )
                Text(
                    text = book.categoryTag,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Kotak Kaligrafi Arab Ringkas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(calligraphyBoxBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = book.shortArabicName.ifBlank { book.initial },
                    fontFamily = getArabicFontFamily("scheherazade"),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = calligraphyTextColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Judul Kitab
            Text(
                text = book.cleanName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Baris Bawah: Total Hadits & Chevron Kanan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$hadithCountFormatted hadis",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
