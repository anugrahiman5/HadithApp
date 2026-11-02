package com.nusa.hadithapp.domain.model

enum class HadithCategory(
    val className: String,
    val slug: String,
    val displayName: String
) {
    ALQURAN("KumpulanAlQuran", "alquran", "Hadits Al Qur'an"),
    QUDSI("KumpulanQudsi", "qudsi", "Hadits Qudsi"),
    MUTAWATIR("KumpulanMutawatir", "mutawatir", "Hadits Mutawatir"),
    MARFU("KumpulanMarfu", "marfu", "Hadits Marfu'"),
    MAUQUF("KumpulanMauquf", "mauquf", "Hadits Mauquf"),
    MAQTHU("KumpulanMaqthu", "maqthu", "Hadits Maqthu'"),
    MURSAL("KumpulanMursal", "mursal", "Hadits Mursal"),
    MUNQATHI("KumpulanMunqathi", "munqathi", "Hadits Munqathi'"),
    MUALLAQ("KumpulanMuallaq", "muallaq", "Hadits Mu'allaq");

    companion object {
        fun fromSlug(slug: String): HadithCategory? = entries.find { it.slug == slug }
        fun fromClassName(className: String): HadithCategory? = entries.find { it.className == className }
    }
}