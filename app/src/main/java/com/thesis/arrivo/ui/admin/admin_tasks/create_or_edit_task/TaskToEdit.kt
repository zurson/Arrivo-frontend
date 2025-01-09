package com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task

import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.utilities.Location

data class TaskToEdit(
    val task: Task,
    val address: String,
    val location: Location
)