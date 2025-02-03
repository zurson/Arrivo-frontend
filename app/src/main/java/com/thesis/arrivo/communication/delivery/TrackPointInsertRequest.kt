package com.thesis.arrivo.communication.delivery

data class TrackPointInsertRequest(

    val points: List<RoutePoint>,
    val deliveryId: Long

)
