package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.utilities.location.Location
import java.time.LocalDate
import java.time.LocalDateTime

data class Task(
    val id: Long,
    var title: String,
    var location: Location,
    var addressText: String,
    var status: TaskStatus,
    var assignedDate: LocalDate?,
    var startDate: LocalDateTime?,
    var endDate: LocalDateTime?,
    var employee: Employee?,
    val products: List<Product> = listOf()
) {
    companion object {
        fun emptyTask(): Task {
            return Task (
                id = -1,
                title = "",
                location = Location(0.0,0.0),
                addressText = "",
                status = TaskStatus.UNASSIGNED,
                assignedDate = null,
                startDate = null,
                endDate = null,
                employee = null,
                products = emptyList()
            )
        }
    }
}