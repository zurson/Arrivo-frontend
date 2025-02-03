package com.thesis.arrivo.communication

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.thesis.arrivo.communication.available_products.AvailableProductsService
import com.thesis.arrivo.communication.delivery.DeliveryService
import com.thesis.arrivo.communication.employee.EmployeeService
import com.thesis.arrivo.communication.gson.LocalDateAdapter
import com.thesis.arrivo.communication.gson.LocalDateTimeAdapter
import com.thesis.arrivo.communication.road_accidents.RoadAccidentsService
import com.thesis.arrivo.communication.task.TasksService
import com.thesis.arrivo.utilities.Settings.Companion.SERVER_USING_HOST
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime

object RetrofitInstance {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_USING_HOST)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    val employeeService: EmployeeService by lazy {
        retrofit.create(EmployeeService::class.java)
    }

    val tasksService: TasksService by lazy {
        retrofit.create(TasksService::class.java)
    }

    val availableProductsService: AvailableProductsService by lazy {
        retrofit.create(AvailableProductsService::class.java)
    }

    val roadAccidentsService: RoadAccidentsService by lazy {
        retrofit.create(RoadAccidentsService::class.java)
    }

    val deliveryService: DeliveryService by lazy {
        retrofit.create(DeliveryService::class.java)
    }
}

