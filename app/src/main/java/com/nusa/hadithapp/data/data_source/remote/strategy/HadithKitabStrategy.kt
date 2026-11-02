package com.nusa.hadithapp.data.data_source.remote.strategy

interface HadithKitabStrategy {
    fun getResearchers(): Pair<String, String>
    fun replaceDerajat(code: String?): String {
        return when (code){
            "1" -> "Shahih"
            "2" -> "Shahihul Isnad"
            "3" -> "Hasan Shahih"
            "4" -> "Hasan"
            "5" -> "Hasanul Isnad"
            "6" -> "Dha'if"
            "7" -> "Dha'iful Isnad"
            "8" -> "Dha'if Jiddan"
            "9" -> "Munkar"
            "10" -> "Maudhu'"
            "11" -> "Syadz"
            "12" -> "Shahih Bima Qablahu"
            "13" -> "Hasan Bima Qablahu"
            else -> code ?: "-"
        }
    }
}

object BukhariMuslimStrategy : HadithKitabStrategy{
    override fun getResearchers(): Pair<String, String> =
        Pair("Ijma' Ulama", "-")
}

object AlbaniZaiStrategy : HadithKitabStrategy{
    override fun getResearchers(): Pair<String, String> =
        Pair("M. Nashiruddin Al Albani","Abu Thahir Zubair 'Ali Zai")
}

object DarimiStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("Husain Salim Asad Ad Daroni", "-")
}

object AhmadStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("Syu'aib Al Arnauth", "Ahmad Syakir")
}

object MalikStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("Salim bin 'Id Al Hilali", "-")
}

object DaruquthniStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("Majdi bin Manshur bin Sayyid", "-")
}

object IbnukhuzaimahStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("M. Nashiruddin Al Albani", "Al A'dhomi")
}

object IbnuhibbanStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("M. Nashiruddin Al Albani", "Syu'aib Al Arnauth")
}

object MustadrakStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("Adz Dzahabi", "-")
}

object DefaultKitabStrategy : HadithKitabStrategy {
    override fun getResearchers(): Pair<String, String> = Pair("-", "-")
}

object HadithStrategyFactory {
    fun getStrategy(bookSlug: String): HadithKitabStrategy {
        val cleanKey = bookSlug.lowercase().replace("-","").replace("_", "").replace(" ", "")
        return when (cleanKey){
            "shahihbukhari", "bukhari", "shahihmuslim", "muslim" -> BukhariMuslimStrategy
            "sunantirmidzi", "tirmidzi", "sunanabudaud", "abudaud", "sunannasai", "nasai", "sunanibnumajah", "ibnumajah" -> AlbaniZaiStrategy
            "sunandarimi", "darimi" -> DarimiStrategy
            "musnadahmad", "ahmad" -> AhmadStrategy
            "muwathamalik", "malik" -> MalikStrategy
            "sunandaruquthni", "daruquthni" -> DaruquthniStrategy
            "shahihibnukhuzaimah", "ibnukhuzaimah" -> IbnukhuzaimahStrategy
            "shahihibnuhibban", "ibnuhibban" -> IbnuhibbanStrategy
            "almustadrak", "mustadrak" -> MustadrakStrategy
            else -> DefaultKitabStrategy
        }
    }
}