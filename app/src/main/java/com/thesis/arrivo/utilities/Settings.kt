package com.thesis.arrivo.utilities

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


class Settings {

    companion object {

        // Server Communication
        private const val SERVER_LOCAL_HOST = "http://10.0.2.2:8080/"
        private const val SERVER_REMOTE_HOST = "https://arrivo-server-latest.onrender.com/"
        const val SERVER_USING_HOST = SERVER_LOCAL_HOST

        // Layouts
        const val START_END_PERCENTAGE: Float = 0.05f
        val LOADING_SCREEN_BG = Color(0x80CECCC8)

        // Maps
        const val DEFAULT_MAP_ZOOM: Float = 17f

        // AppButton
        const val APP_BUTTON_DEFAULT_MAX_LINES = 2
        const val APP_BUTTON_DISABLE_ALPHA: Float = 0.4f

        // Employee Account - Create
        const val FIRST_NAME_MAX_LEN = 20
        const val LAST_NAME_MAX_LEN = 20
        const val EMAIL_MAX_LEN = 50
        const val PHONE_NUMBER_MAX_LEN = 9
        const val PHONE_NUMBER_MASK = "000-000-000"
        const val PHONE_NUMBER_MASK_LETTER = '0'

        // Tasks
        val TASK_FREE_COLOR = Color(0xFF4CAF50)
        val TASK_IN_PROGRESS_COLOR = Color(0xFFFFC107)
        val TASK_ASSIGNED_COLOR = Color(0xFF03A9F4)
        val TASK_FINISHED_COLOR = Color(0xFFCC3333)

        // Products
        const val PRODUCT_PIECES_UNIT: String = "x"

        // Deliveries
        val DELIVERY_COMPLETED_COLOR = Color(0xFFCC3333)
        val DELIVERY_IN_PROGRESS_COLOR = Color(0xFFFFC107)
        val DELIVERY_ASSIGNED_COLOR = Color(0xFF03A9F4)
        val DELIVERY_TIME_OK_COLOR = Color.Green
        val DELIVERY_TIME_EXCEEDED_COLOR = Color.Red
        private val DELIVERY_SCHEDULE_CURRENT_TASK_COLOR_LIGHT = Color(0xFF9EEFA0)
        private val DELIVERY_SCHEDULE_CURRENT_TASK_COLOR_DARK = Color(0x999EEFA0)

        // Filters
        val FILTER_ACTIVE_COLOR = Color(0xFF4CAF50)

        // Road Accidents - Global
        val ROAD_ACCIDENTS_ACTIVE_COLOR = Color(0xFF4CAF50)
        val ROAD_ACCIDENTS_FINISHED_COLOR = Color(0xFFCC3333)

        // Road Accidents - Reports
        val ACCIDENT_REPORT_MAX_DESCRIPTION_LEN = 200
        val ACCIDENT_REPORT_MIN_DESCRIPTION_LEN = 1
        val ACCIDENT_REPORT_CAR_ID_MAX_LEN = 10
        val ACCIDENT_REPORT_CAR_ID_MIN_LEN = 5

        // PERMISSIONS
        const val REQUIRED_PERMISSION: String = Manifest.permission.ACCESS_FINE_LOCATION

        private val PERMISSIONS_ASK_DURING_START: List<String> = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val PERMISSIONS_BELOW_API_33: List<String> = listOf(
            Manifest.permission.POST_NOTIFICATIONS
        )

        /**
         * Functions
         **/

        // Permissions
        fun getAskDuringStartPermissions(): List<String> {
            val permissions = PERMISSIONS_ASK_DURING_START.toMutableList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                permissions.addAll(PERMISSIONS_BELOW_API_33)
            return permissions
        }

        // Deliveries
        @Composable
        fun getCurrentTaskContainerColor(): Color {
            return if (isSystemInDarkTheme())
                DELIVERY_SCHEDULE_CURRENT_TASK_COLOR_DARK
            else
                DELIVERY_SCHEDULE_CURRENT_TASK_COLOR_LIGHT
        }
    }
}