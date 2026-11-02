package com.nusa.hadithapp.domain.model

data class SanadPerawi(
    val kodeRawi: Int,
    val name: String,
    val quality: Int,
    val kalangan: String,

    val nasab: String? = null,
    val kuniyah: String? = null,
    val laqob: String? = null,
    val negeriHidup: String? = null,
    val negeriWafat: String? = null,
    val tahunWafat: String? = null,
    val rBukhari: Int? = null,
    val rMuslim: Int? = null,
    val rAbuDaud: Int? = null,
    val rTirmidzi: Int? = null,
    val rNasai: Int? = null,
    val rIbnuMajah: Int? = null,
    val rAhmad: Int? = null,
    val rMalik: Int? = null,
    val rDarimi: Int? = null
)
