package com.thesis.arrivo.communication.work_time

import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface WorkTimeService {

    @GET("analysis/working-hours")
    suspend fun getWorkingTimeBetweenDates(
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate
    ): List<WorkingHours>

}