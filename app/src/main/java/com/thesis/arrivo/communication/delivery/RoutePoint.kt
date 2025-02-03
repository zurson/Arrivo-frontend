package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.utilities.location.Location
import java.time.LocalDateTime

data class RoutePoint(
    val location: Location,
    val timestamp: LocalDateTime
)
