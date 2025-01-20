package com.thesis.arrivo.view_models

import androidx.lifecycle.ViewModel
import com.thesis.arrivo.communication.delivery.Delivery
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.task.Task
import java.time.LocalDate

class DeliverySharedViewModel : ViewModel() {

    val selectedTasks: MutableList<Task> = mutableListOf()
    lateinit var selectedDate: LocalDate
    lateinit var employee: Employee

    var editMode: Boolean = false
    lateinit var deliveryToEdit: Delivery
}