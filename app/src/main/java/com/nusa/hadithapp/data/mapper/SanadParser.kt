package com.nusa.hadithapp.data.mapper

import com.nusa.hadithapp.data.model.PerawiProfileDto
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

object SanadParser {
    fun extractPerawiList(sanadObject: JsonObject): List<PerawiProfileDto> {
        val perawiList = mutableListOf<PerawiProfileDto>()

        for (suffix in 'A'..'J') {
            val kode = sanadObject["KodeRawi$suffix"]?.jsonPrimitive?.intOrNull ?: continue
            val nama = sanadObject["Nama$suffix"]?.jsonPrimitive?.contentOrNull ?: continue

            if (kode <= 0 || nama.isBlank()) continue

            perawiList.add(
                PerawiProfileDto(
                    kodeRawi = kode,
                    name = nama,
                    quality = sanadObject["Quality$suffix"]?.jsonPrimitive?.intOrNull ?: 1,
                    kalangan = sanadObject["Kalangan$suffix"]?.jsonPrimitive?.contentOrNull ?: "Perawi",
                    nasab = sanadObject["Nasab$suffix"]?.jsonPrimitive?.contentOrNull,
                    kuniyah = sanadObject["Kuniyah$suffix"]?.jsonPrimitive?.contentOrNull,
                    laqob = sanadObject["Laqob$suffix"]?.jsonPrimitive?.contentOrNull,
                    negeriHidup = sanadObject["NegeriHidup$suffix"]?.jsonPrimitive?.contentOrNull,
                    negeriWafat = sanadObject["NegeriWafat$suffix"]?.jsonPrimitive?.contentOrNull,
                    tahunWafat = sanadObject["TahunWafat$suffix"]?.jsonPrimitive?.contentOrNull,
                    rBukhari = sanadObject["RBukhari$suffix"]?.jsonPrimitive?.intOrNull,
                    rMuslim = sanadObject["RMuslim$suffix"]?.jsonPrimitive?.intOrNull,
                    rAbuDaud = sanadObject["RAbuDaud$suffix"]?.jsonPrimitive?.intOrNull,
                    rTirmidzi = sanadObject["RTirmidzi$suffix"]?.jsonPrimitive?.intOrNull,
                    rNasai = sanadObject["RNasai$suffix"]?.jsonPrimitive?.intOrNull,
                    rIbnuMajah = sanadObject["RIbnuMajah$suffix"]?.jsonPrimitive?.intOrNull,
                    rAhmad = sanadObject["RAhmad$suffix"]?.jsonPrimitive?.intOrNull,
                    rMalik = sanadObject["RMalik$suffix"]?.jsonPrimitive?.intOrNull,
                    rDarimi = sanadObject["RDarimi$suffix"]?.jsonPrimitive?.intOrNull
                )
            )
        }

        return perawiList
    }
}