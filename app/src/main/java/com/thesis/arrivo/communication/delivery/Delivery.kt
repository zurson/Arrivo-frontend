package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.task.Task
import java.time.LocalDate
import java.time.LocalDateTime

data class Delivery(
    val id: Long,
    val tasks: List<Task>,
    var timeMinutes: Int,
    var distanceKm: Int,
    val assignedDate: LocalDate,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val breakDate: LocalDateTime?,
    val status: DeliveryStatus,
    val employee: Employee
) {
    companion object {
        fun emptyDelivery(): Delivery {
            return Delivery(
                id = -1,
                tasks = emptyList(),
                timeMinutes = -1,
                distanceKm = -1,
                assignedDate = LocalDate.now(),
                startDate = null,
                endDate = null,
                breakDate = null,
                status = DeliveryStatus.ASSIGNED,
                employee = Employee.emptyEmployee(),
            )
        }
    }
}