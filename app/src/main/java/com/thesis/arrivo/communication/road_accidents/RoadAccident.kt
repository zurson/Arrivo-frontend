package com.thesis.arrivo.communication.road_accidents

import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.ui.admin.admin_accidents.RoadAccidentCategory
import com.thesis.arrivo.ui.admin.admin_accidents.RoadAccidentStatus
import com.thesis.arrivo.utilities.Location
import java.time.LocalDate

class RoadAccident(
    val id: Long,
    val status: RoadAccidentStatus,
    val location: Location,
    val category: RoadAccidentCategory,
    val licensePlate: String,
    val date: LocalDate,
    val description: String,
    val employee: Employee
)