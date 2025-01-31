package com.thesis.arrivo.utilities

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color


class Settings {

    companion object {
        const val APP_BUTTON_DEFAULT_MAX_LINES = 2
        const val APP_BUTTON_DISABLE_ALPHA: Float = 0.4f
        const val START_END_PERCENTAGE: Float = 0.05f

        const val FIRST_NAME_MAX_LEN = 20
        const val LAST_NAME_MAX_LEN = 20
        const val EMAIL_MAX_LEN = 50
        const val PHONE_NUMBER_MAX_LEN = 9
        const val PHONE_NUMBER_MASK = "000-000-000"
        const val PHONE_NUMBER_MASK_LETTER = '0'

        const val AUTH_ACCOUNT_STATUS_CHECK_INTERVAL_MS: Long = 10_000

        val TASK_FREE_COLOR = Color(0xFF4CAF50)
        val TASK_IN_PROGRESS_COLOR = Color(0xFFFFC107)
        val TASK_ASSIGNED_COLOR = Color(0xFF03A9F4)
        val TASK_FINISHED_COLOR = Color(0xFFCC3333)

        val DELIVERY_COMPLETED_COLOR = Color(0xFFCC3333)
        val DELIVERY_IN_PROGRESS_COLOR = Color(0xFFFFC107)
        val DELIVERY_ASSIGNED_COLOR = Color(0xFF03A9F4)

        val FILTER_ACTIVE_COLOR = Color(0xFF4CAF50)

        val ROAD_ACCIDENTS_ACTIVE_COLOR = Color(0xFF4CAF50)
        val ROAD_ACCIDENTS_FINISHED_COLOR = Color(0xFFCC3333)

        val LOADING_SCREEN_BG = Color(0x80CECCC8)

        const val DEFAULT_MAP_ZOOM: Float = 17f

        val DELIVERY_TIME_OK_COLOR = Color.Green
        val DELIVERY_TIME_EXCEEDED_COLOR = Color.Red

        val ACCIDENT_REPORT_MAX_DESCRIPTION_LEN = 200
        val ACCIDENT_REPORT_MIN_DESCRIPTION_LEN = 1
        val ACCIDENT_REPORT_CAR_ID_MAX_LEN = 10
        val ACCIDENT_REPORT_CAR_ID_MIN_LEN = 5

        val DELIVERY_SCHEDULE_CURRENT_TASK_COLOR = Color(0xFF9EEFA0)

        // PERMISSIONS
        private val PERMISSIONS_ASK_DURING_START: List<String> = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val PERMISSIONS_BELOW_API_33: List<String> = listOf(
            Manifest.permission.POST_NOTIFICATIONS
        )

        const val REQUIRED_PERMISSION: String = Manifest.permission.ACCESS_FINE_LOCATION

        fun getAskDuringStartPermissions(): List<String> {
            val permissions = PERMISSIONS_ASK_DURING_START.toMutableList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                permissions.addAll(PERMISSIONS_BELOW_API_33)
            return permissions
        }
    }
}