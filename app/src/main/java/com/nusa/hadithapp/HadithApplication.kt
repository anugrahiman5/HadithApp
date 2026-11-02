package com.nusa.hadithapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class sebagai entry point Hilt dependency injection
 * Mengelola global app initialization dan crash handling
 */
@HiltAndroidApp
class HadithApplication : Application() {

    companion object {
        private const val TAG = "HadithApplication"
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Application onCreate - Hadith App initialized")

        // Setup crash handling (optional)
        setupExceptionHandler()
    }

    /**
     * Setup global exception handler untuk uncaught exceptions
     * Ini membantu debug crash yang tidak tertangani
     */
    private fun setupExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(TAG, "Uncaught exception in thread ${thread.name}", throwable)

            // Pass ke default handler agar app crash secara normal
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}