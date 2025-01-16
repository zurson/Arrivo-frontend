package com.thesis.arrivo.communication.delivery

import java.time.LocalDate

data class OptimizeRoutesRequest(
    val tasksToOptimize: List<TaskToOptimize>,
    val date: LocalDate
)
