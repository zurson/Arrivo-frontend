package com.thesis.arrivo.utilities.notifications

import android.content.Context

object NotificationIdManager {
    private const val PREFS_NAME = "notification_prefs"
    private const val KEY_NOTIFICATION_ID = "notification_id"
    private const val START_ID = 1001

    fun getNextNotificationId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var nextId = prefs.getInt(KEY_NOTIFICATION_ID, START_ID)

        if (nextId >= Int.MAX_VALUE - 1)
            nextId = START_ID

        prefs.edit().putInt(KEY_NOTIFICATION_ID, nextId + 1).apply()
        return nextId
    }
}
