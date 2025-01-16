package com.thesis.arrivo.utilities

import androidx.compose.ui.graphics.Color


class Settings {

    companion object {
        const val APP_BUTTON_DEFAULT_MAX_LINES = 2
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

        val FILTER_ACTIVE_COLOR = Color(0xFF4CAF50)

        val ROAD_ACCIDENTS_ACTIVE_COLOR = Color(0xFF4CAF50)
        val ROAD_ACCIDENTS_FINISHED_COLOR = Color(0xFFCC3333)

        val LOADING_SCREEN_BG = Color(0x80CECCC8)

        const val DEFAULT_MAP_ZOOM: Float = 17f
    }

}