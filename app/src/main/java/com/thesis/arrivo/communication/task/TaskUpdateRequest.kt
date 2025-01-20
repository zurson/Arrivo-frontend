package com.thesis.arrivo.communication.task

import com.thesis.arrivo.utilities.Location

data class TaskUpdateRequest(
    val title: String,
    val location: Location,
    val addressText: String,
    val products: List<Product>
)