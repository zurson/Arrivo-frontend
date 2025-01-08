package com.thesis.arrivo.communication.task

import com.thesis.arrivo.ui.admin.admin_tasks.Product
import com.thesis.arrivo.utilities.Location

data class TaskUpdateRequest(
    val title: String,
    val location: Location,
    val addressText: String,
    val status: TaskStatus,
    val employeeId: Long,
    val products: List<Product>
)