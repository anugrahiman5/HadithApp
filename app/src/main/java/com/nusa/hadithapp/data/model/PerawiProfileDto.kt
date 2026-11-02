package com.nusa.hadithapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerawiProfileDto(
    @SerialName("KodeRawi") val kodeRawi: Int,
    @SerialName("Nama") val name: String,
    @SerialName("Quality") val quality: Int = 0,
    @SerialName("Kalangan") val kalangan: String = "",

    @SerialName("Nasab") val nasab: String? = null,
    @SerialName("Kuniyah") val kuniyah: String? = null,
    @SerialName("Laqob") val laqob: String? = null,
    @SerialName("NegeriHidup") val negeriHidup: String? = null,
    @SerialName("NegeriWafat") val negeriWafat: String? = null,
    @SerialName("TahunWafat") val tahunWafat: String? = null,
    @SerialName("RBukhari") val rBukhari: Int? = null,
    @SerialName("RMuslim") val rMuslim: Int? = null,
    @SerialName("RAbuDaud") val rAbuDaud: Int? = null,
    @SerialName("RTirmidzi") val rTirmidzi: Int? = null,
    @SerialName("RNasai") val rNasai: Int? = null,
    @SerialName("RIbnuMajah") val rIbnuMajah: Int? = null,
    @SerialName("RAhmad") val rAhmad: Int? = null,
    @SerialName("RMalik") val rMalik: Int? = null,
    @SerialName("RDarimi") val rDarimi: Int? = null
)