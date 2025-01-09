package com.thesis.arrivo.communication.task

import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.Product
import com.thesis.arrivo.utilities.Location

data class TaskCreateRequest(
    val title: String,
    val location: Location,
    val addressText: String,
    val products: List<Product>
)