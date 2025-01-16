package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.task.Task

data class OptimizedDeliveryRoute(
    val timeMinutes: Int,
    val distanceKm: Int,
    val tasksOrder: List<Task>
)