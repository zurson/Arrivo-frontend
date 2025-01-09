package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.Product
import com.thesis.arrivo.utilities.Location
import java.time.LocalDateTime

data class Task(
    val id: Long,
    val title: String,
    val location: Location,
    val addressText: String,
    val status: TaskStatus,
    val assignedDate: LocalDateTime?,
    val employee: Employee?,
    val products: List<Product> = listOf()
) {
    companion object {
        fun emptyTask(): Task {
            return Task (
                id = 0,
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