package com.nusa.hadithapp.util.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DailyHadithReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        DailyHadithReminderManager.showNotification(context)

        // Reschedule for next day if needed
        val hour = intent.getIntExtra("hour", 5)
        val minute = intent.getIntExtra("minute", 30)
        DailyHadithReminderManager.scheduleReminder(context, hour, minute)
    }
}
