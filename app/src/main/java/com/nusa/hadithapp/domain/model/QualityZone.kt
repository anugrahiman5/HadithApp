package com.nusa.hadithapp.domain.model

import androidx.compose.ui.graphics.Color

data class QualityZone(
    val color: Color,
    val textColor: Color,
    val symbol: String,
    val groupName: String,
    val description: String
)

fun getQualityZoneInfo(quality: Int): QualityZone {
    return when (quality) {
        1 -> QualityZone(
            Color(0xFF00A2E8),
            Color.Black,
            "👑",
            "Shahabat Nabi",
            "Sahabat ialah orang yang bertemu rasulullah shallallahu 'alaihi wa salllam dan ia seorang muslim sampai akhir hayatnya."
        )

        2 -> QualityZone(
            Color(0xFF2E7D32),
            Color.White,
            "✓",
            "Diterima (Tsiqatun Tsiqah)",
            "Tsiqatun Tsiqah atau Tsiqah Hafidh ialah perawi yang mempunyai kredibilitas yang tinggi, yang terkumpul pada dirinya sifat adil dan hafalannya sangat kuat."
        )

        3 -> QualityZone(
            Color(0xFF4CAF50),
            Color.Black,
            "✓",
            "Diterima (Tsiqah / 'Adil)",
            "Tsiqah / Mutqin / 'Adil, ialah perawi yang mempunyai sifat 'adil dan kuat hafalannya."
        )

        4 -> QualityZone(
            Color(0xFF81C784),
            Color.Black,
            "✓",
            "Diterima (Shaduuq La Ba'sa Bih)",
            "Shaduq La Ba'sa Bihi ialah perawi yang jujur terhadap apa yang diberitakan dan perawi tersebut tidak bermasalah (cacat dalam periwayatan)."
        )

        5 -> QualityZone(
            Color(0xFFFFF176),
            Color.Black,
            "⚠️",
            "Diperselisihkan (Buruk Hafalan)",
            "Shaduq, buruk hafalannya ialah perawi yang jujur terhadap apa yang diberitakan, tetapi ia memiliki hafalan yang buruk dan sering keliru dalam periwayatan."
        )

        6 -> QualityZone(
            Color(0xFFFFEE58),
            Color.Black,
            "⚠️",
            "Diperselisihkan (Maqbul)",
            "Maqbul ialah perawi yang diterima periwayatannya dan dapat dijadikan sebagai hujjah."
        )

        7 -> QualityZone(
            Color(0xFFF0F4C3),
            Color.Black,
            "⚠️",
            "Diperselisihkan (Majhul Hal)",
            "Majhul Al Haal / Mastur ialah perawi yang tidak diketahui jati dirinya."
        )

        8 -> QualityZone(
            Color(0xFFFF8A80),
            Color.Black,
            "✕",
            "Tertolak (Dha'if)",
            "Dha'if ialah perawi yang lemah periwayatannya (lemah/cacat hafalannya, lemah ilmunya, lemah dalam agama)."
        )

        9 -> QualityZone(
            Color(0xFFE53935),
            Color.White,
            "✕",
            "Tidak di Percaya / Majhul",
            "Majhul ialah perawi yang hanya diriwayatkan oleh satu orang saja."
        )

        10 -> QualityZone(
            Color(0xFFC62828),
            Color.White,
            "✕",
            "Tertolak (Matruk)",
            "Matruk ialah perawi yang dituduh berdusta atau perawi yang banyak melakukan kekeliruan, sehingga periwayatannya bertentangan dengan periwayatan perawi yang tsiqah. atau perawi yang sering meriwayatkan hadits-hadits yang tidak dikenal (gharib) dari perawi yang terkenal tsiqah."
        )

        11 -> QualityZone(
            Color(0xFFB71C1C),
            Color.White,
            "🚨",
            "Tertolak Berat (Tertuduh Dusta)",
            "Tertuduh berdusta ialah orang yang tertuduh berdusta derajat haditsnya seperti hadits palsu, tetapi tidak dapat dipastikan bahwasanya hadits tersebut hadits palsu, karena kondisi dia yang tertuduh mengakibatkan derajat haditsnya turun mendekati hadits palsu."
        )

        12 -> QualityZone(
            Color(0xFF7F0000),
            Color.White,
            "🚨",
            "Tertolak Berat (Kadzab)",
            "Kadzaab (pendusta) ialah perawi yang pendusta, haditsnya tidak bisa dijadikan hujjah."
        )

        else -> QualityZone(
            Color.Gray,
            Color.White,
            "?",
            "Tidak Diketahui",
            "Data kualitas perawi belum teridentifikasi."
        )
    }
}