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
) {
    companion object {
        fun emptyRoadAccident(): RoadAccident {
            return RoadAccident(
                id = -1,
                status = RoadAccidentStatus.ACTIVE,
                location = Location(0.0, 0.0),
                category = RoadAccidentCategory.OTHER,
                licensePlate = "",
                date = LocalDate.now(),
                description = "",
                employee = Employee.emptyEmployee()
            )
        }
    }
}