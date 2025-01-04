package com.thesis.arrivo.communication.employee

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.http.Body

class EmployeeRepository {
    private val employeeService = RetrofitInstance.employeeService

    suspend fun getAllEmployees(): List<EmployeeResponse> {
        return employeeService.getAllEmployees()
    }

    suspend fun createEmployeeAccount(@Body createAccountRequest: EmployeeCreateAccountRequest) {
        employeeService.createEmployeeAccount(createAccountRequest)
    }

    suspend fun updateEmployeeAccount(
        id: Long,
        updateAccountRequest: EmployeeUpdateRequest
    ) {
        println("UPDATE: $updateAccountRequest")
        println("ID: $id")
        employeeService.updateEmployeeAccount(id, updateAccountRequest)
    }
}