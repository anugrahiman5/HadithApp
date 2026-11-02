package com.nusa.hadithapp.util.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nusa.hadithapp.MainActivity
import com.nusa.hadithapp.R
import com.nusa.hadithapp.ui.home.DailyHadithProvider
import java.util.Calendar

object DailyHadithReminderManager {

    const val CHANNEL_ID = "hadith_daily_reminder_channel"
    const val CHANNEL_NAME = "Pengingat Hadits Harian"
    const val NOTIFICATION_ID = 1001
    const val REQUEST_CODE_ALARM = 2001

    private val INSPIRING_HADITHS = listOf(
        "\"Menuntut ilmu adalah kewajiban bagi setiap muslim.\" (HR. Ibnu Majah)",
        "\"Sebaik-baik kalian adalah orang yang belajar Al-Qur'an dan mengajarkannya.\" (HR. Bukhari)",
        "\"Barangsiapa menempuh jalan untuk mencari ilmu, Allah akan mudahkan baginya jalan menuju surga.\" (HR. Muslim)",
        "\"Sesungguhnya amalan itu tergantung pada niatnya.\" (HR. Bukhari & Muslim)",
        "\"Tersenyummu di hadapan saudaramu adalah sedekah bagimu.\" (HR. Tirmidzi)",
        "\"Orang mukmin yang kuat lebih dicintai Allah daripada mukmin yang lemah.\" (HR. Muslim)",
        "\"Sesungguhnya kejujuran itu membimbing ke arah kebajikan.\" (HR. Bukhari)"
    )

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Pengingat harian untuk membaca dan mentadabburi hadis Nabi ﷺ"
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleReminder(context: Context, hour: Int, minute: Int) {
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, DailyHadithReminderReceiver::class.java).apply {
            action = "com.nusa.hadithapp.ACTION_DAILY_HADITH_REMINDER"
            putExtra("hour", hour)
            putExtra("minute", minute)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_MONTH, 1)
        }

        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            // Fallback for security restrictions on exact alarms
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                target.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, DailyHadithReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun showNotification(context: Context, customMessage: String? = null): Boolean {
        createNotificationChannel(context)

        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) {
            return false
        }

        val dailyHadith = DailyHadithProvider.getDailyHadith()
        val quoteText = customMessage ?: "\"${dailyHadith.translation}\""
        val bookInfo = "${dailyHadith.bookName} No. ${dailyHadith.number}"

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_book_slug", dailyHadith.bookSlug)
            putExtra("target_hadith_number", dailyHadith.number)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            dailyHadith.number,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_hadith)
            .setContentTitle("Hadits Pilihan Hari Ini 📖")
            .setContentText("$quoteText ($bookInfo)")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$quoteText\n\n📌 $bookInfo • ${dailyHadith.grade}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return try {
            notificationManager.notify(NOTIFICATION_ID, builder.build())
            true
        } catch (e: SecurityException) {
            false
        }
    }
}
