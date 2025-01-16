package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.task.Task

data class Delivery(
    val id: Long,
    val tasks: List<Task>,
    var timeMinutes: Int,
    var distanceKm: Int
)