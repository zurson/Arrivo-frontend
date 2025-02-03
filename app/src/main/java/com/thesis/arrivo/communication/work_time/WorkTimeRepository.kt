package com.thesis.arrivo.communication.work_time

import com.thesis.arrivo.communication.RetrofitInstance
import java.time.LocalDate

class WorkTimeRepository {
    private val workTimeService = RetrofitInstance.workTimeService

    suspend fun getWorkingTimeBetweenDates(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WorkingHours> {
        return workTimeService.getWorkingTimeBetweenDates(startDate, endDate)
    }

}