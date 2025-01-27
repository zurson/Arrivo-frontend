package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.utilities.location.Location
import java.time.LocalDate

data class Task(
    val id: Long,
    val title: String,
    val location: Location,
    val addressText: String,
    val status: TaskStatus,
    val assignedDate: LocalDate?,
    val employee: Employee?,
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
                employee = null,
                products = emptyList()
            )
        }
    }
}