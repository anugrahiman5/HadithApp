package com.nusa.hadithapp.domain.model

import kotlinx.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface AppError {
    val message: String

    data class Network(override val message: String = "Koneksi internet bermasalah. Periksa jaringan Anda.") : AppError
    data class Timeout(override val message: String = "Koneksi melebihi batas waktu (timeout). Coba lagi.") : AppError
    data class NotFound(override val message: String = "Data hadits tidak ditemukan.") : AppError
    data class Server(override val message: String = "Terjadi gangguan pada server hadits. Silakan coba beberapa saat lagi.") : AppError
    data class Unknown(override val message: String) : AppError

    companion object {
        fun fromThrowable(throwable: Throwable): AppError {
            return when (throwable) {
                is UnknownHostException -> Network()
                is SocketTimeoutException -> Timeout()
                is IOException -> Network("Gagal terhubung ke server.")
                else -> {
                    val msg = throwable.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui."
                    if (msg.contains("kosong", ignoreCase = true) || msg.contains("tidak ditemukan", ignoreCase = true)) {
                        NotFound()
                    } else {
                        Unknown(msg)
                    }
                }
            }
        }
    }
}