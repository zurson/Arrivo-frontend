package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.employee.EmployeeResponse
import com.thesis.arrivo.ui.admin.admin_tasks.create_task.Product
import com.thesis.arrivo.utilities.Location
import java.time.LocalDateTime

data class Task(
    val id: Long,
    val title: String,
    val location: Location,
    val addressText: String,
    val status: TaskStatus,
    val assignedDate: LocalDateTime?,
    val employee: EmployeeResponse?,
    val products: List<Product> = listOf()
)