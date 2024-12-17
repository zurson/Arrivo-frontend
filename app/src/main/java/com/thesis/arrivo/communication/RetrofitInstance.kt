package com.thesis.arrivo.communication

import com.thesis.arrivo.communication.employee.EmployeeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val employeeService: EmployeeService by lazy {
        retrofit.create(EmployeeService::class.java)
    }
}