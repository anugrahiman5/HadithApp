package com.nusa.hadithapp.data.data_source.remote

import android.util.Log
import com.nusa.hadithapp.data.data_source.remote.strategy.HadithStrategyFactory
import com.nusa.hadithapp.data.mapper.HadithMapper
import com.nusa.hadithapp.data.mapper.SanadParser
import com.nusa.hadithapp.data.model.BabItemDto
import com.nusa.hadithapp.data.model.HadithDto
import com.nusa.hadithapp.data.model.HaditsSoftRawItem
import com.nusa.hadithapp.data.model.KitabItemDto
import com.nusa.hadithapp.data.model.PerawiProfileDto
import com.nusa.hadithapp.data.model.RawiListItemDto
import com.nusa.hadithapp.data.model.RawiSearchDto
import com.nusa.hadithapp.data.model.ResearcherGradeDto
import com.nusa.hadithapp.data.model.ScholarCommentDto
import com.nusa.hadithapp.data.model.SearchRequestDto
import com.nusa.hadithapp.data.model.SearchResponseDto
import com.nusa.hadithapp.data.model.SimilarHadithDto
import com.nusa.hadithapp.domain.model.SanadPerawi
import com.nusa.hadithapp.util.BookSlugUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import javax.inject.Inject

class HadithApiService @Inject constructor(
    private val client: HttpClient
) {
    private companion object {
        const val BASE_URL = "https://api.haditssoft.workers.dev"
    }

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Fungsi dinamis: Menerima kode kitab (e.g., "bukhari", "muslim") dan nomor hadits dari internet
    suspend fun getHadithDetail(bookSlug: String, number: Int): HadithDto =
        withContext(Dispatchers.IO) {
            try {
                val cleanSlug = BookSlugUtil.normalize(bookSlug)
                val urlHadith = "$BASE_URL/loadMainData/$cleanSlug/$number"
                val urlSanad = "$BASE_URL/loadSanadHadits/Sanad$cleanSlug/$number"

                val (rootHadithArray, rootSanadArray) = coroutineScope {
                    val hadithDeffered = async {
                        client.get(urlHadith) { contentType(ContentType.Application.Json) }
                            .body<JsonArray>()
                    }
                    val sanadDeffered = async {
                        try {
                            val response = client.get(urlSanad) { contentType(ContentType.Application.Json) }
                            if (response.status.isSuccess()) {
                                val rawJson = response.body<String>()
                                jsonConfig.parseToJsonElement(rawJson).jsonArray
                            } else null
                        } catch (e: Exception) {
                            Log.e("SANAD_FETCH_ERROR", "Gagal memuat sanad ($urlSanad): ${e.message}")
                            null
                        }
                    }
                    Pair(hadithDeffered.await(), sanadDeffered.await())
                }

                // Parsing Data Hadits Utama
                val innerHadithArray = rootHadithArray.firstOrNull() as? JsonArray
                    ?: throw Exception("Format data server tidak valid.")
                val firstElement = innerHadithArray.firstOrNull()
                    ?: throw Exception("Data hadits kosong.")
                val remoteData =
                    jsonConfig.decodeFromJsonElement<HaditsSoftRawItem>(firstElement)

                // Parsing Data Sanad & Jalur Perawi
                val innerSanadArray = rootSanadArray?.firstOrNull() as? JsonArray
                val masterDaftarJalur = mutableListOf<List<PerawiProfileDto>>()

                innerSanadArray?.forEach { sanadElement ->
                    try {
                        val jsonObject = sanadElement as? JsonObject
                        if (jsonObject != null) {
                            val satuJalurBersih = SanadParser.extractPerawiList(jsonObject)
                            if (satuJalurBersih.isNotEmpty()) masterDaftarJalur.add(satuJalurBersih)
                        }
                    } catch (e: Exception) {
                        Log.e("SANAD_PARSING_ERROR", "Gagal parse sub-jalur sanad: ", e)
                    }
                }

                if (masterDaftarJalur.isEmpty()) {
                    masterDaftarJalur.add(
                        listOf(
                            PerawiProfileDto(
                                1,
                                "Para Perawi Hadits",
                                1,
                                "Tsiqah"
                            )
                        )
                    )
                }

                HadithMapper.toDto(
                    cleanSlug = cleanSlug,
                    remoteData = remoteData,
                    sanadJalur = masterDaftarJalur
                )
            } catch (e: Exception) {
                Log.e("HADITSSOFT_API_ERROR", "Gagal mengambil data: ", e)
                val penyebabAsli =
                    e.localizedMessage ?: e.message ?: "Koneksi internet bermasalah."
                throw Exception("Gagal memuat data hadits: $penyebabAsli")
            }
        }

    // Fungsi ambil profil lengkap perawi dari internet
    suspend fun getPerawiProfile(kodeRawi: Int): PerawiProfileDto? = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/loadCompleteProfile/$kodeRawi"
            val rootArray = client.get(url) {
                contentType(ContentType.Application.Json)
            }.body<JsonArray>()

            val innerArray = rootArray.firstOrNull() as? JsonArray
            val firstElement = innerArray?.firstOrNull()

            if (firstElement != null) {
                jsonConfig.decodeFromJsonElement<PerawiProfileDto>(firstElement)
            } else null
        } catch (e: Exception) {
            Log.e("PERAWI_API_ERROR", "Gagal load profile perawi: ", e)
            null
        }
    }

    // Fungsi ambil daftar nomor hadits perawi dalam suatu kitab (loadTotalHadith)
    suspend fun getPerawiTotalHadith(bookSlug: String, kodeRawi: Int): List<Int> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadTotalHadith/$cleanSlug/$kodeRawi"
            val rootArray = client.get(url) {
                contentType(ContentType.Application.Json)
            }.body<JsonArray>()

            val rowsArray = rootArray.firstOrNull() as? JsonArray
            rowsArray?.mapNotNull { element ->
                val obj = element as? JsonObject
                obj?.get("NoHdt")?.jsonPrimitive?.contentOrNull?.toIntOrNull()
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e("LOAD_TOTAL_HADITH_ERROR", "Gagal load total hadith perawi: ", e)
            emptyList()
        }
    }

    // Fungsi ambil komentar ulama
    suspend fun getScholarComments(kodeRawi: Int): List<ScholarCommentDto> = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/loadScholarComment/$kodeRawi"
            val rootArray = client.get(url) {
                contentType(ContentType.Application.Json)
            }.body<JsonArray>()

            val innerArray = rootArray.firstOrNull() as? JsonArray

            if (innerArray != null) {
                jsonConfig.decodeFromJsonElement<List<ScholarCommentDto>>(innerArray)
            } else emptyList()
        } catch (e: Exception) {
            Log.e("COMMENT_ERROR", "Gagal load scholar comment: ", e)
            emptyList()
        }
    }

    suspend fun getAllBooks(bookSlug: String): List<KitabItemDto> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadAllBooks/$cleanSlug"

            val rootArray = client.get(url) {
                contentType(ContentType.Application.Json)
            }.body<JsonArray>()

            val innerArray = rootArray.firstOrNull() as? JsonArray

            if (innerArray != null) jsonConfig.decodeFromJsonElement<List<KitabItemDto>>(innerArray)
            else emptyList()
        } catch (e: Exception) {
            Log.e("HADITH_API_ERROR", "Gagal memuat daftar kitab: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllChapters(
        bookSlug: String,
        start: Int,
        end: Int
    ): List<BabItemDto> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadAllChapters/$cleanSlug/$start/$end"

            val rootArray = client.get(url) {
                contentType(ContentType.Application.Json)
            }.body<JsonArray>()

            val innerArray = rootArray.firstOrNull() as? JsonArray

            if (innerArray != null) jsonConfig.decodeFromJsonElement<List<BabItemDto>>(innerArray)
            else emptyList()
        } catch (e: Exception) {
            Log.e("HADITH_API_ERROR", "Gagal memuat daftar bab: ${e.message}")
            emptyList()
        }
    }

    suspend fun getHadithByTheme(
        bookSlug: String,
        vMember: Int,
        themeName: String
    ): List<HaditsSoftRawItem> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/classificationData/$cleanSlug/$vMember/$themeName"

            val rootArray = client.get(url) {
                contentType(ContentType.Application.Json)
            }.body<JsonArray>()

            // Struktur respons: [ [ {hadith1}, {hadith2}, ... ], "CLASSIFICATIONDATA", "1" ]
            val innerArray = rootArray.firstOrNull() as? JsonArray
            if (innerArray != null) {
                jsonConfig.decodeFromJsonElement<List<HaditsSoftRawItem>>(innerArray)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("THEME_API_ERROR", "Gagal memuat hadits berdasarkan tema: ${e.message}")
            emptyList()
        }
    }

    suspend fun searchHadith(column: String, request: SearchRequestDto): SearchResponseDto = withContext(
        Dispatchers.IO
    ) {
        try {
            val url = "$BASE_URL/searchHadits/all/$column"
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<SearchResponseDto>()
        } catch (e: Exception) {
            Log.e("SEARCH_API_ERROR", "Gagal melakukan pencarian hadits: ${e.message}", e)
            SearchResponseDto(emptyMap(), 0)
        }
    }

    suspend fun searchRawi(request: RawiSearchDto): List<RawiListItemDto> = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/loadListOfRawiName/"
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                val rootArray = response.body<JsonArray>()
                val innerArray = rootArray.firstOrNull() as? JsonArray
                if (innerArray != null) {
                    jsonConfig.decodeFromJsonElement<List<RawiListItemDto>>(innerArray)
                } else emptyList()
            } else emptyList()
        } catch (e: Exception) {
            Log.e("SEARCH_RAWI_API_ERROR", "Gagal memuat daftar rawi: ${e.message}", e)
            emptyList()
        }
    }

    // Fungsi ambil hadits serupa (takhrij) antar kitab
    suspend fun getSimilarHadith(bookSlug: String, number: Int): List<SimilarHadithDto> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadSimilarHadith/Banding$cleanSlug/$number"
            val response = client.get(url) {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val rootArray = response.body<JsonArray>()
                val innerArray = rootArray.firstOrNull() as? JsonArray
                if (innerArray != null) {
                    jsonConfig.decodeFromJsonElement<List<SimilarHadithDto>>(innerArray)
                } else emptyList()
            } else emptyList()
        } catch (e: Exception) {
            Log.e("SIMILAR_HADITH_ERROR", "Gagal memuat hadits serupa: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getBiographyRawText(imamName: String): String? = withContext(Dispatchers.IO) {
        try {
            val queryName = when (imamName.trim().lowercase()) {
                "nasa", "an-nasai", "annasai", "nasa'i" -> "Nasai"
                "abudaud", "abu dawud" -> "Abu Daud"
                "ibnumajah", "ibnu maja" -> "Ibnu Majah"
                "daraquthni" -> "Daruquthni"
                else -> imamName.trim()
            }
            val url = "$BASE_URL/loadBiographyData/$queryName"
            val response = client.get(url) {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val rootArray = response.body<JsonArray>()
                val innerArray = rootArray.firstOrNull() as? JsonArray
                val firstObj = innerArray?.firstOrNull() as? JsonObject
                firstObj?.get("Biografi")?.jsonPrimitive?.contentOrNull
            } else null
        } catch (e: Exception) {
            Log.e("BIOGRAPHY_API_ERROR", "Gagal memuat biografi imam $imamName: ${e.message}", e)
            null
        }
    }

    suspend fun getSimilarCustomData(bookSlug: String, number: Int): ComparedHadithRaw? = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadCustomData/$cleanSlug/$number/1/SIMILARDATA"
            val response = client.get(url) {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val rootArray = response.body<JsonArray>()
                val innerArray = rootArray.firstOrNull() as? JsonArray
                val firstObj = innerArray?.firstOrNull() as? JsonObject
                if (firstObj != null) {
                    val arabic = firstObj["Arabic"]?.jsonPrimitive?.contentOrNull ?: ""
                    val indonesia = firstObj["Indonesia"]?.jsonPrimitive?.contentOrNull ?: ""
                    val albaniRaw = firstObj["Albani"]?.jsonPrimitive?.contentOrNull?.trim() ?: ""
                    val darussalamRaw = firstObj["Darussalam"]?.jsonPrimitive?.contentOrNull?.trim() ?: ""
                    val kitabList = firstObj["kitabTitle"] as? JsonArray
                    val babList = firstObj["babTitle"] as? JsonArray
                    val kitabName = (kitabList?.firstOrNull() as? JsonObject)?.get("NKitab")?.jsonPrimitive?.contentOrNull ?: ""
                    val babName = (babList?.firstOrNull() as? JsonObject)?.get("NBab")?.jsonPrimitive?.contentOrNull ?: ""
                    
                    val strategy = HadithStrategyFactory.getStrategy(cleanSlug)
                    val albaniGrade = if (albaniRaw.isNotBlank() && albaniRaw != "null") strategy.replaceDerajat(albaniRaw) else ""
                    val darussalamGrade = if (darussalamRaw.isNotBlank() && darussalamRaw != "null") strategy.replaceDerajat(darussalamRaw) else ""

                    val grade = when {
                        albaniGrade.isNotBlank() && albaniGrade != "-" -> albaniGrade
                        darussalamGrade.isNotBlank() && darussalamGrade != "-" -> darussalamGrade
                        cleanSlug.contains("bukhari", true) || cleanSlug.contains("muslim", true) -> "Shahih"
                        else -> "Shahih"
                    }
                    ComparedHadithRaw(
                        arabic = arabic,
                        indonesia = indonesia,
                        kitabName = kitabName,
                        babName = babName,
                        grade = grade
                    )
                } else null
            } else null
        } catch (e: Exception) {
            Log.e("COMPARE_API_ERROR", "Gagal memuat custom data ($bookSlug #$number): ${e.message}", e)
            null
        }
    }

    suspend fun getSanadChain(bookSlug: String, number: Int): List<SanadPerawi> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadSanadHadits/Sanad$cleanSlug/$number"
            val response = client.get(url) {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val rawJson = response.body<String>()
                val rootArray = jsonConfig.parseToJsonElement(rawJson).jsonArray
                val innerArray = rootArray.firstOrNull() as? JsonArray
                val firstSanadObj = innerArray?.firstOrNull() as? JsonObject
                if (firstSanadObj != null) {
                    val rawis = mutableListOf<SanadPerawi>()
                    val letters = listOf("J", "I", "H", "G", "F", "E", "D", "C", "B", "A")
                    for (letter in letters) {
                        val kode = firstSanadObj["KodeRawi$letter"]?.jsonPrimitive?.intOrNull ?: 0
                        val rawiName = firstSanadObj["Nama$letter"]?.jsonPrimitive?.contentOrNull
                        if (kode > 0 && !rawiName.isNullOrBlank() && rawiName != "null") {
                            val quality = firstSanadObj["Quality$letter"]?.jsonPrimitive?.intOrNull ?: 1
                            val kalangan = firstSanadObj["Kalangan$letter"]?.jsonPrimitive?.contentOrNull ?: "Perawi"
                            val nasab = firstSanadObj["Nasab$letter"]?.jsonPrimitive?.contentOrNull
                            val kuniyah = firstSanadObj["Kuniyah$letter"]?.jsonPrimitive?.contentOrNull
                            val laqob = firstSanadObj["Laqob$letter"]?.jsonPrimitive?.contentOrNull
                            val negeriHidup = firstSanadObj["NegeriHidup$letter"]?.jsonPrimitive?.contentOrNull
                            val negeriWafat = firstSanadObj["NegeriWafat$letter"]?.jsonPrimitive?.contentOrNull
                            val tahunWafat = firstSanadObj["TahunWafat$letter"]?.jsonPrimitive?.contentOrNull
                            rawis.add(
                                SanadPerawi(
                                    kodeRawi = kode,
                                    name = rawiName.trim(),
                                    quality = quality,
                                    kalangan = kalangan,
                                    nasab = nasab,
                                    kuniyah = kuniyah,
                                    laqob = laqob,
                                    negeriHidup = negeriHidup,
                                    negeriWafat = negeriWafat,
                                    tahunWafat = tahunWafat
                                )
                            )
                        }
                    }
                    rawis
                } else emptyList()
            } else emptyList()
        } catch (e: Exception) {
            Log.e("SANAD_CHAIN_ERROR", "Gagal load sanad chain: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getSanadChainNames(bookSlug: String, number: Int): List<String> = withContext(Dispatchers.IO) {
        try {
            val cleanSlug = BookSlugUtil.normalize(bookSlug)
            val url = "$BASE_URL/loadSanadHadits/Sanad$cleanSlug/$number"
            val response = client.get(url) {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val rawJson = response.body<String>()
                val rootArray = jsonConfig.parseToJsonElement(rawJson).jsonArray
                val innerArray = rootArray.firstOrNull() as? JsonArray
                val firstSanadObj = innerArray?.firstOrNull() as? JsonObject
                if (firstSanadObj != null) {
                    val names = mutableListOf<String>()
                    val letters = listOf("J", "I", "H", "G", "F", "E", "D", "C", "B", "A")
                    for (letter in letters) {
                        val rawiName = firstSanadObj["Nama$letter"]?.jsonPrimitive?.contentOrNull
                        if (!rawiName.isNullOrBlank() && rawiName != "null") {
                            names.add(rawiName.trim())
                        }
                    }
                    names
                } else emptyList()
            } else emptyList()
        } catch (e: Exception) {
            Log.e("SANAD_CHAIN_ERROR", "Gagal load sanad chain: ${e.message}", e)
            emptyList()
        }
    }
}

data class ComparedHadithRaw(
    val arabic: String,
    val indonesia: String,
    val kitabName: String,
    val babName: String,
    val grade: String
)
