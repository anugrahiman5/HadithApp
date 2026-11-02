package com.nusa.hadithapp.util

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class TtsLanguage(val label: String, val locale: Locale) {
    ARABIC("Bahasa Arab", Locale.forLanguageTag("ar")),
    INDONESIAN("Bahasa Indonesia", Locale.forLanguageTag("id-ID"))
}

sealed interface TtsPlaybackState {
    data object Idle : TtsPlaybackState
    data object Initializing : TtsPlaybackState
    data class Playing(
        val language: TtsLanguage,
        val speed: Float
    ) : TtsPlaybackState
    data class Error(val message: String) : TtsPlaybackState
}

/**
 * Pengelola Audio Text-to-Speech (TTS) bawaan Android untuk Hadits.
 * Menghasilkan suara pelafalan matan Arab dan pembacaan terjemahan Indonesia
 * secara real-time tanpa memerlukan file rekaman audio (0 KB penambahan APK).
 */
class HadithTtsManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _playbackState = MutableStateFlow<TtsPlaybackState>(TtsPlaybackState.Idle)
    val playbackState: StateFlow<TtsPlaybackState> = _playbackState.asStateFlow()

    private var currentText: String = ""
    private var currentLanguage: TtsLanguage = TtsLanguage.ARABIC
    private var currentSpeed: Float = 1.0f

    init {
        initTtsEngine()
    }

    private fun initTtsEngine() {
        try {
            _playbackState.value = TtsPlaybackState.Initializing
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("HADITH_TTS", "Gagal inisialisasi TextToSpeech: ${e.message}", e)
            _playbackState.value = TtsPlaybackState.Error("Gagal membuka modul suara")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            _playbackState.value = TtsPlaybackState.Idle

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _playbackState.value = TtsPlaybackState.Playing(currentLanguage, currentSpeed)
                }

                override fun onDone(utteranceId: String?) {
                    // Ketika seluruh antrean bacaan selesai
                    if (utteranceId?.startsWith("hadith_tts_end") == true || utteranceId?.startsWith("hadith_tts_single") == true) {
                        _playbackState.value = TtsPlaybackState.Idle
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _playbackState.value = TtsPlaybackState.Idle
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    Log.w("HADITH_TTS", "TTS Utterance error code: $errorCode")
                    _playbackState.value = TtsPlaybackState.Idle
                }
            })

            // Jika ada permintaan tertunda saat inisialisasi awal
            if (currentText.isNotBlank()) {
                speak(currentText, currentLanguage, currentSpeed)
            }
        } else {
            isInitialized = false
            _playbackState.value = TtsPlaybackState.Error("Inisialisasi modul suara gagal")
        }
    }

    /**
     * Memutar teks hadits dengan bahasa dan kecepatan tertentu.
     */
    fun speak(
        text: String,
        language: TtsLanguage,
        speed: Float = currentSpeed,
        onError: (String) -> Unit = {}
    ) {
        if (text.isBlank()) return

        currentText = text
        currentLanguage = language
        currentSpeed = speed

        if (!isInitialized || tts == null) {
            initTtsEngine()
            return
        }

        val engine = tts ?: return

        // Atur bahasa
        val langResult = when (language) {
            TtsLanguage.ARABIC -> {
                val r1 = engine.setLanguage(Locale.forLanguageTag("ar"))
                if (r1 == TextToSpeech.LANG_MISSING_DATA || r1 == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setLanguage(Locale.forLanguageTag("ar-SA"))
                } else {
                    r1
                }
            }
            TtsLanguage.INDONESIAN -> {
                val r1 = engine.setLanguage(Locale.forLanguageTag("id-ID"))
                if (r1 == TextToSpeech.LANG_MISSING_DATA || r1 == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setLanguage(Locale.forLanguageTag("id"))
                } else {
                    r1
                }
            }
        }

        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            val errMsg = "Paket suara ${language.label} belum terpasang di perangkat Anda. Silakan unduh di Pengaturan Suara Google."
            _playbackState.value = TtsPlaybackState.Error(errMsg)
            onError(errMsg)
            return
        }

        // Atur kecepatan bicara (0.75x, 1.0x, 1.25x)
        engine.setSpeechRate(speed)

        // Terapkan karakter suara pria (male voice & masculine pitch)
        applyMaleVoiceAndPitch(engine, language.locale)

        val cleanText = sanitizeForTts(text, language)
        if (cleanText.isBlank()) return

        // Tangani teks hadits panjang dengan memecah per kalimat (Android max utterance limit ~3500 chars)
        val chunks = splitTextIntoChunks(cleanText, language)
        val timestamp = System.currentTimeMillis()

        for (index in chunks.indices) {
            val chunk = chunks[index]
            val isLast = index == chunks.lastIndex
            val utteranceId = if (chunks.size == 1) {
                "hadith_tts_single_$timestamp"
            } else if (isLast) {
                "hadith_tts_end_$timestamp"
            } else {
                "hadith_tts_chunk_${index}_$timestamp"
            }

            val queueMode = if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)

            engine.speak(chunk, queueMode, params, utteranceId)
        }

        _playbackState.value = TtsPlaybackState.Playing(language, speed)
    }

    /**
     * Mengubah kecepatan dan memutar ulang dari awal teks aktif
     */
    fun setSpeedAndReplay(speed: Float) {
        currentSpeed = speed
        if (_playbackState.value is TtsPlaybackState.Playing) {
            speak(currentText, currentLanguage, currentSpeed)
        }
    }

    /**
     * Menghentikan audio seketika
     */
    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.w("HADITH_TTS", "Error stopping TTS: ${e.message}")
        }
        _playbackState.value = TtsPlaybackState.Idle
    }

    /**
     * Melepaskan resource TextToSpeech saat layar ditutup
     */
    fun shutdown() {
        try {
            stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            Log.w("HADITH_TTS", "Error shutting down TTS: ${e.message}")
        }
    }

    /**
     * Memilih profil suara pria (Male Voice) dan menyesuaikan nada (pitch)
     * agar terdengar lebih berat, tenang, dan berwibawa khas pembaca hadits.
     */
    private fun applyMaleVoiceAndPitch(engine: TextToSpeech, locale: Locale) {
        // Nada suara pria (0.85f menghasilkan timbre suara yang lebih dalam, berwibawa, dan maskulin)
        engine.setPitch(0.85f)

        try {
            val availableVoices = engine.voices ?: return
            val matchingVoices = availableVoices.filter {
                it.locale.language.equals(locale.language, ignoreCase = true)
            }

            // Cari suara pria berdasarkan metadata feature atau nama voice dari Google / Samsung TTS
            val maleVoice = matchingVoices.firstOrNull { voice ->
                val hasMaleFeature = voice.features?.any { feature ->
                    feature.contains("male", ignoreCase = true) && !feature.contains("female", ignoreCase = true)
                } == true
                val hasMaleInName = voice.name.contains("male", ignoreCase = true) &&
                        !voice.name.contains("female", ignoreCase = true)
                hasMaleFeature || hasMaleInName
            } ?: matchingVoices.firstOrNull { voice ->
                // Varian nama Google TTS Arab/Indo pria (misal ard, ara, man)
                voice.name.contains("-man", ignoreCase = true) ||
                        voice.name.contains("_male", ignoreCase = true) ||
                        voice.name.contains("ard-local", ignoreCase = true)
            }

            if (maleVoice != null) {
                engine.voice = maleVoice
                Log.d("HADITH_TTS", "Suara pria aktif: ${maleVoice.name} untuk bahasa ${locale.language}")
            }
        } catch (e: Exception) {
            Log.w("HADITH_TTS", "Gagal menyetel voice pria, menggunakan pitch pria: ${e.message}")
        }
    }

    /**
     * Membersihkan teks agar pelafalan TTS terdengar fasih & tidak mengeja simbol-simbol kurung/tanda baca.
     */
    private fun sanitizeForTts(raw: String, language: TtsLanguage): String {
        return when (language) {
            TtsLanguage.ARABIC -> {
                raw
                    .replace(Regex("""[\[\]\(\)\{\}«»"'\*]"""), " ")
                    .replace(Regex("""\s+"""), " ")
                    .trim()
            }
            TtsLanguage.INDONESIAN -> {
                raw
                    .replace(Regex("""\[(.*?)\]"""), "$1") // Hapus kurung siku perawi tapi pertahankan nama
                    .replace(Regex("""\((?:HR\.|Shahih|Sunan).*?\)""", RegexOption.IGNORE_CASE), "") // Hapus kutipan buku penutup
                    .replace(Regex("""[\[\]\(\)\{\}«»"'\*~]"""), " ")
                    .replace(Regex("""\s+"""), " ")
                    .trim()
            }
        }
    }

    private fun splitTextIntoChunks(text: String, language: TtsLanguage): List<String> {
        if (text.length <= 2500) return listOf(text)

        val delimiter = if (language == TtsLanguage.ARABIC) Regex("""[\n\.\u06D4\u061F]""") else Regex("""[\n\.\?!]""")
        val rawParts = text.split(delimiter).map { it.trim() }.filter { it.isNotBlank() }

        val chunks = mutableListOf<String>()
        val currentChunk = StringBuilder()

        for (part in rawParts) {
            if (currentChunk.length + part.length > 2000) {
                chunks.add(currentChunk.toString())
                currentChunk.clear()
            }
            if (currentChunk.isNotEmpty()) currentChunk.append(". ")
            currentChunk.append(part)
        }

        if (currentChunk.isNotEmpty()) {
            chunks.add(currentChunk.toString())
        }

        return if (chunks.isEmpty()) listOf(text) else chunks
    }
}

/**
 * Composable helper untuk me-manage siklus hidup [HadithTtsManager]
 * secara otomatis tanpa kebocoran memori (zero leak).
 */
@Composable
fun rememberHadithTtsController(): HadithTtsManager {
    val context = LocalContext.current
    val manager = remember { HadithTtsManager(context) }

    DisposableEffect(manager) {
        onDispose {
            manager.shutdown()
        }
    }

    return manager
}
