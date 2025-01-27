package com.thesis.arrivo.communication.road_accidents

import com.thesis.arrivo.ui.common.road_accidents_list.RoadAccidentCategory
import com.thesis.arrivo.utilities.Location
import java.time.LocalDate

data class RoadAccidentCreateRequest(
    val location: Location,
    val category: RoadAccidentCategory,
    val licensePlate: String,
    val date: LocalDate,
    val description: String,
    val employeeId: Long
)