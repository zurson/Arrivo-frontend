package com.thesis.arrivo.communication.delivery

import java.util.LinkedList

data class OptimizedRoutes(
    val timeMinutes: Int,
    val timeExceeded: Boolean,
    val distanceKm: Int,
    val tasksOrder: LinkedList<TaskToOptimize>
)
