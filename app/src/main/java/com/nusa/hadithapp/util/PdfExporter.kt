package com.nusa.hadithapp.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import com.nusa.hadithapp.domain.model.HadithNote
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    fun exportAndShareNotesPdf(context: Context, notes: List<HadithNote>) {
        if (notes.isEmpty()) {
            Toast.makeText(context, "Tidak ada catatan untuk diekspor", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfDocument = PdfDocument()
            val pageWidth = 595
            val pageHeight = 842
            val margin = 40f
            val contentWidth = (pageWidth - 2 * margin).toInt()

            var currentPageNumber = 1
            var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas

            val titlePaint = Paint().apply {
                color = Color.parseColor("#0D3323")
                textSize = 18f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            val subtitlePaint = Paint().apply {
                color = Color.parseColor("#555555")
                textSize = 10f
                isAntiAlias = true
            }

            val linePaint = Paint().apply {
                color = Color.parseColor("#CCCCCC")
                strokeWidth = 1f
            }

            val noteTitlePaint = Paint().apply {
                color = Color.parseColor("#1B1B1B")
                textSize = 12f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            val metaPaint = Paint().apply {
                color = Color.parseColor("#2E7D32")
                textSize = 9f
                isAntiAlias = true
            }

            val bodyPaint = TextPaint().apply {
                color = Color.parseColor("#333333")
                textSize = 10f
                isAntiAlias = true
            }

            val footerPaint = Paint().apply {
                color = Color.parseColor("#888888")
                textSize = 8f
                isAntiAlias = true
            }

            // Header halaman pertama
            var y = 50f
            canvas.drawText("KOMPILASI BINDER KAJIAN HADITS", margin, y, titlePaint)
            y += 16f
            val dateStr = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID")).format(Date())
            canvas.drawText("Diekspor: $dateStr • Total: ${notes.size} Catatan", margin, y, subtitlePaint)
            y += 14f
            canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
            y += 24f

            fun startNewPage() {
                canvas.drawText(
                    "HadithApp • Halaman $currentPageNumber",
                    margin,
                    pageHeight - 25f,
                    footerPaint
                )
                pdfDocument.finishPage(page)
                currentPageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = 50f
            }

            for ((index, note) in notes.withIndex()) {
                if (y > pageHeight - 140f) {
                    startNewPage()
                }

                // Judul Catatan
                val numTitle = "${index + 1}. ${note.title.ifBlank { "Catatan Tanpa Judul" }}"
                canvas.drawText(numTitle, margin, y, noteTitlePaint)
                y += 16f

                // Kategori & Tagar
                val tagsStr = if (note.tags.isNotEmpty()) " • Tagar: " + note.tags.joinToString(", ") { "#$it" } else ""
                val metaText = "Binder: ${note.categoryTag}$tagsStr"
                canvas.drawText(metaText, margin, y, metaPaint)
                y += 14f

                // Rujukan Hadits (jika ada)
                if (note.bookName.isNotBlank() && note.number > 0) {
                    val refText = "Rujukan: ${note.bookName} No. ${note.number}"
                    canvas.drawText(refText, margin, y, subtitlePaint)
                    y += 14f
                }

                // Isi Catatan
                val cleanContent = note.content.ifBlank { "(Tidak ada catatan tertulis)" }
                val staticLayout = StaticLayout.Builder
                    .obtain(cleanContent, 0, cleanContent.length, bodyPaint, contentWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0f, 1.2f)
                    .setIncludePad(false)
                    .build()

                if (y + staticLayout.height > pageHeight - 60f) {
                    startNewPage()
                }

                canvas.save()
                canvas.translate(margin, y)
                staticLayout.draw(canvas)
                canvas.restore()
                y += staticLayout.height + 16f

                // Garis pemisah antar catatan
                canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
                y += 20f
            }

            // Footer halaman terakhir
            canvas.drawText(
                "HadithApp • Halaman $currentPageNumber",
                margin,
                pageHeight - 25f,
                footerPaint
            )
            pdfDocument.finishPage(page)

            // Simpan ke direktori cache documents
            val docsDir = File(context.cacheDir, "documents")
            docsDir.mkdirs()
            val pdfFile = File(docsDir, "Binder_Kajian_${System.currentTimeMillis()}.pdf")
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()

            // Bagikan melalui FileProvider
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "Kompilasi Binder Kajian Hadits")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Dokumen PDF"))
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal membuat PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
