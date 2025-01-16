package com.thesis.arrivo.communication.employee

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.http.Body
import java.time.LocalDate

class EmployeeRepository {
    private val employeeService = RetrofitInstance.employeeService

    suspend fun getAllEmployees(): List<Employee> {
        return employeeService.getAllEmployees()
    }

    suspend fun createEmployeeAccount(@Body createAccountRequest: EmployeeCreateAccountRequest) {
        employeeService.createEmployeeAccount(createAccountRequest)
    }

    suspend fun updateEmployeeAccount(
        id: Long,
        updateAccountRequest: EmployeeUpdateRequest
    ) {
        employeeService.updateEmployeeAccount(id, updateAccountRequest)
    }

    suspend fun getUnassignedEmployeesOnDate(date: LocalDate): List<Employee> {
        return employeeService.getUnassignedEmployeesOnDate(date.toString())
    }
}