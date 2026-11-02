package com.nusa.hadithapp.data.data_source.remote

import android.util.Log
import com.nusa.hadithapp.data.model.GeminiContent
import com.nusa.hadithapp.data.model.GeminiContentRequest
import com.nusa.hadithapp.data.model.GeminiGenerationConfig
import com.nusa.hadithapp.data.model.GeminiPart
import com.nusa.hadithapp.data.model.GeminiResponse
import com.nusa.hadithapp.domain.model.Hadith
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiApiService @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        private const val TAG = "GeminiApiService"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

        val SYSTEM_INSTRUCTION = """
            Anda adalah "Asisten Ahli Syarah Hadits" yang santun, amanah, ilmiah, dan beradab tinggi dalam koridor metodologi ulama ahlussunnah wal jama'ah (seperti Al-Hafizh Ibnu Hajar Al-Asqalani dalam Fathul Bari, Imam An-Nawawi dalam Syarah Shahih Muslim, Al-Mubarakfuri dalam Tuhfatul Ahwadzi, dan para pensyarah hadits terkemuka).

            PEDOMAN UTAMA:
            1. FOKUS DAN RELEVANSI:
               Fokus utama Anda adalah menjelaskan makna hadits yang diberikan, kosakata sulit (gharibul hadits), asbabul wurud (jika ada riwayatnya), faedah hukum fiqih (fawaid fiqhiyyah), dan hikmah tarbiyah/akhlak.
            2. FORMAT JAWABAN:
               - Gunakan Bahasa Indonesia yang santun, lugas, ringkas, dan tertata rapi.
               - Gunakan format Markdown (bolding, bullet points, headers) agar nyaman dan mudah dibaca di layar smartphone.
               - Jika menerangkan makna kalimat arab, sebutkan lafazhnya lalu terjemahkan maknanya.
               - Di akhir penjelasan mengenai hukum atau amaliah, ingatkan adab dan anjurkan untuk tetap merujuk/berkonsultasi langsung dengan asatidz atau ulama setempat untuk fatwa praktis sesuai kondisi penanya.
            3. BATASAN KETAT / NEGATIVE GUARDRAILS (PENTING):
               - Jika pengguna mengajukan pertanyaan di luar konteks hadits, agama Islam, fiqih, tafsir, tarikh Islam, atau adab/akhlak (misalnya: pemrograman koding/software, sains eksakta/rumus matematika umum, politik praktis partisan kontemporer, gosip/hiburan sekuler, resep kuliner umum tanpa kaitan syariat halal/haram, atau obrolan santai yang tidak relevan dengan keilmuan Islam), TOLAK SECARA SANTUN DENGAN ADAB ISLAMI:
                 "Afwan (mohon maaf), kapasitas ana di sini difokuskan secara khusus untuk membantu antum mentadaburi syarah hadits, makna lafazh nabawi, faedah keilmuan, dan hikmah keislaman. Silakan ajukan pertanyaan seputar hadits yang sedang dikaji ini atau topik keislaman yang relevan."
               - Hindari memicu polemik khilafiyah yang memecah belah umat; sampaikan dengan adab keluasan madzhab fiqih mu'tabar jika terdapat perbedaan pandangan ulama.
        """.trimIndent()
    }

    suspend fun askHadithExplanation(
        apiKey: String,
        model: String,
        hadith: Hadith,
        userQuery: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalArgumentException("Kunci API Gemini belum diatur. Silakan atur di menu Pengaturan.")
            )
        }

        val effectiveModel = if (model.isNotBlank()) model else "gemini-1.5-flash"
        val url = "$BASE_URL/$effectiveModel:generateContent?key=$apiKey"

        val hadithContext = buildString {
            appendLine("=== KONTEKS HADITS YANG SEDANG DIKAJI ===")
            appendLine("Kitab: ${hadith.bookName}")
            appendLine("Nomor: Hadits No. ${hadith.number}")
            if (hadith.kitab.isNotBlank()) appendLine("Bab/Kitab: ${hadith.kitab}")
            if (hadith.chapter.isNotBlank()) appendLine("Judul Bab: ${hadith.chapter}")
            if (hadith.grade.isNotBlank()) appendLine("Derajat Hadits: ${hadith.grade}")
            appendLine()
            appendLine("Teks Arab:")
            appendLine(hadith.arabicText)
            appendLine()
            appendLine("Terjemahan:")
            appendLine(hadith.translation)
            if (hadith.syarah.isNotBlank()) {
                appendLine()
                appendLine("Catatan Syarah:")
                appendLine(hadith.syarah.take(500))
            }
            appendLine("=========================================")
        }

        val fullUserPrompt = """
            $hadithContext

            Pertanyaan Pengguna:
            $userQuery
        """.trimIndent()

        val requestPayload = GeminiContentRequest(
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = SYSTEM_INSTRUCTION))
            ),
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = fullUserPrompt))
                )
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.4f,
                maxOutputTokens = 2048
            )
        )

        try {
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestPayload)
                timeout {
                    requestTimeoutMillis = 60_000
                    connectTimeoutMillis = 15_000
                    socketTimeoutMillis = 60_000
                }
            }

            if (response.status.isSuccess()) {
                val geminiResponse = response.body<GeminiResponse>()
                val generatedText = geminiResponse.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text

                if (!generatedText.isNullOrBlank()) {
                    Result.success(generatedText.trim())
                } else {
                    Result.failure(Exception("Respons AI kosong atau tidak dapat diproses."))
                }
            } else {
                val errorBody = runCatching { response.body<GeminiResponse>() }.getOrNull()
                val errorMsg = errorBody?.error?.message ?: "Gagal menghubungi Gemini API (Status: ${response.status})"
                Log.e(TAG, "Gemini API error: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini API request", e)
            val friendlyMsg = when {
                e is io.ktor.client.plugins.HttpRequestTimeoutException -> "Permintaan waktu habis (timeout). Silakan periksa koneksi internet Anda."
                e.message?.contains("400") == true -> "Permintaan tidak valid atau API Key salah."
                e.message?.contains("403") == true -> "API Key tidak memiliki izin atau kuota habis."
                e.message?.contains("404") == true -> "Model '$effectiveModel' tidak ditemukan di Gemini API."
                e.message?.contains("429") == true -> "Batas kuota panggilan API tercapai. Silakan coba lagi beberapa saat."
                else -> e.localizedMessage ?: "Terjadi kesalahan saat memproses jawaban AI."
            }
            Result.failure(Exception(friendlyMsg, e))
        }
    }
}
