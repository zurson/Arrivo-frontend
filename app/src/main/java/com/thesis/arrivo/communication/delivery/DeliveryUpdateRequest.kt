package com.thesis.arrivo.communication.delivery

import java.time.LocalDate

data class DeliveryUpdateRequest(
    val tasksIdList: List<DeliveryTask>,
    val timeMinutes: Int,
    val distanceKm: Int,
    val employeeId: Long,
    val date: LocalDate,
)