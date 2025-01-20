package com.thesis.arrivo.communication.task

import com.thesis.arrivo.utilities.Location

data class TaskToEdit(
    val task: Task,
    val address: String,
    val location: Location
)