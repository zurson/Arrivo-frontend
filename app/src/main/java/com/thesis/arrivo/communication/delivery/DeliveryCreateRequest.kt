package com.thesis.arrivo.communication.delivery

import java.time.LocalDate

data class DeliveryCreateRequest(
    val tasksIdList: List<DeliveryTask>,
    val timeMinutes: Int,
    val distanceKm: Int,
    val employeeId: Long,
    val date: LocalDate,
)
