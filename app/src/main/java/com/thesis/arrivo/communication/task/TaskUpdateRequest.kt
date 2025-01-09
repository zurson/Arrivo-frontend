package com.thesis.arrivo.communication.task

import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.Product
import com.thesis.arrivo.utilities.Location
import java.time.LocalDateTime

data class TaskUpdateRequest(
    val title: String,
    val location: Location,
    val addressText: String,
    val status: TaskStatus,
    val assignedDate: LocalDateTime?,
    val employeeId: Long?,
    val products: List<Product>
)