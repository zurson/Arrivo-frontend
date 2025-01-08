package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.employee.EmployeeResponse
import com.thesis.arrivo.ui.admin.admin_tasks.create_task.Product
import com.thesis.arrivo.utilities.Location

data class Task(
    val id: Long,
    var title: String,
    var location: Location,
    var addressText: String,
    var status: TaskStatus,
    var employee: EmployeeResponse?,
    val products: List<Product> = listOf()
)