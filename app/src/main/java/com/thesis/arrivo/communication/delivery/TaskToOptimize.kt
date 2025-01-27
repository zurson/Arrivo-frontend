package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.utilities.location.Location

data class TaskToOptimize(
    val id: Long,
    val location: Location,
)
