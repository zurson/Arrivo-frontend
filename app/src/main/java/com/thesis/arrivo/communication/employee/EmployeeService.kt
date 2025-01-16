package com.thesis.arrivo.communication.employee

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EmployeeService {

    @GET("employees")
    suspend fun getAllEmployees(): List<Employee>

    @POST("employees")
    suspend fun createEmployeeAccount(@Body createAccountRequest: EmployeeCreateAccountRequest)

    @PATCH("employees/{id}")
    suspend fun updateEmployeeAccount(
        @Path("id") id: Long,
        @Body updateAccountRequest: EmployeeUpdateRequest
    )

    @GET("/employees/unassigned")
    suspend fun getUnassignedEmployeesOnDate(@Query("date") date: String): List<Employee>
}