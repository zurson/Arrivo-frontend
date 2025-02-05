package com.thesis.arrivo.utilities

import com.thesis.arrivo.utilities.Settings.Companion.BREAK_TIME_FROM_START_IN_SECONDS
import com.thesis.arrivo.utilities.Settings.Companion.BREAK_TIME_IN_SECONDS
import java.time.Duration
import java.time.LocalDateTime

object BreakManager {

    fun getBreakStartTime(startTime: LocalDateTime): LocalDateTime {
        return startTime.plusSeconds(BREAK_TIME_FROM_START_IN_SECONDS)
    }


    fun isDuringBreak(breakStartTime: LocalDateTime?): Boolean {
        return breakStartTime != null &&
                Duration.between(breakStartTime, LocalDateTime.now())
                    .toSeconds() < BREAK_TIME_IN_SECONDS
    }


    fun getDurationBetweenNowAndBreakTime(breakStartTime: LocalDateTime): Duration {
        return Duration.between(LocalDateTime.now(), breakStartTime)
    }


    fun calculateBreakEndTime(breakStartTime: LocalDateTime): LocalDateTime {
        return breakStartTime.plusSeconds(BREAK_TIME_IN_SECONDS)
    }

}