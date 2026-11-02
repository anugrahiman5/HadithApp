package com.nusa.hadithapp.data.session

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data class untuk menampung sesi browsing hadits berdasarkan perawi.
 */
data class PerawiSession(
    val rawiName: String,
    val kodeRawi: Int,
    val bookSlug: String,
    val hadithNumbers: List<Int>,
    var currentIndex: Int
)

/**
 * Manager khusus untuk menampung sesi penjelajahan hadits perawi (ketika user
 * membuka hadits dari profil perawi).
 * Mengatur pergerakan Next / Previous / Jump dan perhitungan pager (misal: 1 / 446).
 */
@Singleton
class PerawiSessionManager @Inject constructor() {

    private val sessions = mutableMapOf<String, PerawiSession>()
    private var _activeSessionKey: String? = null

    private fun sessionKey(bookSlug: String, kodeRawi: Int): String {
        return "${bookSlug.lowercase()}_$kodeRawi"
    }

    fun setSession(
        bookSlug: String,
        kodeRawi: Int,
        rawiName: String,
        hadithNumbers: List<Int>,
        currentHadithNumber: Int
    ) {
        val key = sessionKey(bookSlug, kodeRawi)
        val idx = hadithNumbers.indexOf(currentHadithNumber).let { if (it != -1) it else 0 }
        sessions[key] = PerawiSession(
            rawiName = rawiName,
            kodeRawi = kodeRawi,
            bookSlug = bookSlug,
            hadithNumbers = hadithNumbers,
            currentIndex = idx
        )
        _activeSessionKey = key
    }

    fun hasSession(bookSlug: String, kodeRawi: Int): Boolean {
        return sessions.containsKey(sessionKey(bookSlug, kodeRawi))
    }

    fun getSession(bookSlug: String, kodeRawi: Int): PerawiSession? {
        val key = sessionKey(bookSlug, kodeRawi)
        return sessions[key]
    }

    fun setActiveSession(bookSlug: String, kodeRawi: Int) {
        val key = sessionKey(bookSlug, kodeRawi)
        if (sessions.containsKey(key)) {
            _activeSessionKey = key
        }
    }

    fun getActiveSession(): PerawiSession? {
        return _activeSessionKey?.let { sessions[it] }
    }

    fun setCurrentIndex(bookSlug: String, kodeRawi: Int, index: Int) {
        val session = getSession(bookSlug, kodeRawi) ?: return
        if (index in session.hadithNumbers.indices) {
            session.currentIndex = index
        }
    }

    fun getNextHadithNumber(bookSlug: String, kodeRawi: Int): Int? {
        val session = getSession(bookSlug, kodeRawi) ?: return null
        if (session.currentIndex < session.hadithNumbers.size - 1) {
            session.currentIndex++
            return session.hadithNumbers[session.currentIndex]
        }
        return null
    }

    fun getPreviousHadithNumber(bookSlug: String, kodeRawi: Int): Int? {
        val session = getSession(bookSlug, kodeRawi) ?: return null
        if (session.currentIndex > 0) {
            session.currentIndex--
            return session.hadithNumbers[session.currentIndex]
        }
        return null
    }

    fun getHadithNumberAt(bookSlug: String, kodeRawi: Int, index: Int): Int? {
        val session = getSession(bookSlug, kodeRawi) ?: return null
        if (index in session.hadithNumbers.indices) {
            session.currentIndex = index
            return session.hadithNumbers[index]
        }
        return null
    }

    fun clearSession(bookSlug: String, kodeRawi: Int) {
        val key = sessionKey(bookSlug, kodeRawi)
        sessions.remove(key)
        if (_activeSessionKey == key) {
            _activeSessionKey = null
        }
    }
}
