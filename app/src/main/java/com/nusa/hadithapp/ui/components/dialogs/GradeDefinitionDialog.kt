package com.nusa.hadithapp.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun GradeDefinitionDialog(
    bookName: String,
    grade: String,
    onDismiss: () -> Unit,
    onOpenMusthalah: ((termId: String) -> Unit)? = null
) {
    val termId = when {
        grade.contains("hasan", ignoreCase = true) -> "hasan_lidzatih"
        grade.contains("dhaif", ignoreCase = true) || grade.contains("lemah", ignoreCase = true) -> "dhaif"
        grade.contains("maudhu", ignoreCase = true) || grade.contains("palsu", ignoreCase = true) -> "maudhu"
        else -> "shahih_lidzatih"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            val judulDialog =
                if (bookName.contains("Bukhari") || bookName.contains("Muslim")) "Definisi Ijma' Ulama" else "Definisi Hadits $grade"
            Text(judulDialog, fontWeight = FontWeight.Bold)
        },
        text = {
            val isiPenjelasan =
                if (bookName.contains("Bukhari") || bookName.contains("Muslim")) {
                    "Kesepakatan mutlak (Ijma') para ulama ahli hadits menetapkan bahwa seluruh isi riwayat dalam Shahih Bukhari dan Shahih Muslim berstatus Shahih (valid/otentik) dan memiliki tingkat otentisitas tertinggi dalam khazanah Islam."
                } else {
                    "Hadits dengan derajat $grade memiliki kriteria penilaian khusus berdasarkan tingkat ketersambungan sanad, kredibilitas hapalan perawi, serta kebersihan matan dari kejanggalan (syadz) atau cacat tersembunyi ('illat) sesuai standar ilmu musthalah hadits."
                }
            Text(
                text = isiPenjelasan,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Paham", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        dismissButton = {
            if (onOpenMusthalah != null) {
                TextButton(onClick = {
                    onDismiss()
                    onOpenMusthalah(termId)
                }) {
                    Text("Kamus Istilah ➔", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    )
}
