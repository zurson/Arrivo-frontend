package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.task.Task
import java.time.LocalDate

data class Delivery(
    val id: Long,
    val tasks: List<Task>,
    val timeMinutes: Int,
    val distanceKm: Int,
    val assignedDate: LocalDate,
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
                status = DeliveryStatus.ASSIGNED,
                employee = Employee.emptyEmployee(),
            )
        }
    }
}