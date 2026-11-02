package com.nusa.hadithapp.domain.model.musthalah

enum class MusthalahCategory(val displayName: String, val badgeLabel: String) {
    ALL("Semua Istilah", "Semua"),
    DERAJAT("Derajat & Kualitas", "Derajat"),
    SANDARAN("Sandaran Matan", "Sandaran"),
    SANAD("Kondisi Sanad", "Sanad"),
    KUANTITAS("Kuantitas Jalur", "Kuantitas"),
    PERAWI("Kredibilitas Perawi", "Perawi"),
    METODE("Metode Riwayat", "Metode")
}

enum class HujjahStatus(val label: String) {
    SHAHIH_HUJJAH("Hujjah Pokok (Wajib Diamalkan)"),
    HASAN_HUJJAH("Hujjah Mu'tabar (Diterima)"),
    SYARAT_PENGUAT("Diterima dengan Penguat (Syawahid)"),
    NON_HUJJAH("Tertolak / Bukan Hujjah"),
    INFORMASIONAL("Kaidah & Istilah Klasifikasi")
}

data class MusthalahTerm(
    val id: String,
    val latinTitle: String,
    val arabicTitle: String,
    val category: MusthalahCategory,
    val hujjahStatus: HujjahStatus,
    val shortDefinition: String,
    val fullExplanation: String,
    val criteria: List<String> = emptyList(),
    val hukumHujjah: String,
    val example: String? = null,
    val relatedCategorySlug: String? = null
)
