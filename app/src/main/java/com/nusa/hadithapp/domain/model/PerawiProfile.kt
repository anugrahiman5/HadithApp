package com.nusa.hadithapp.domain.model

data class PerawiProfile(
    val kodeRawi: Int,
    val nama: String?,
    val quality: Int?,
    val kalangan: String?,
    val kuniyah: String?,
    val laqob: String?,
    val nasab: String?,
    val negeriHidup: String?,
    val negeriWafat: String?,
    val tahunWafat: String?,
    val rBukhari: Int?,
    val rMuslim: Int?,
    val rAbuDaud: Int?,
    val rTirmidzi: Int?,
    val rNasai: Int?,
    val rIbnuMajah: Int?,
    val rAhmad: Int?,
    val rMalik: Int?,
    val rDarimi: Int?
)
