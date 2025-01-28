package com.thesis.arrivo.communication.delivery

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface DeliveryService {

    @GET("delivery")
    suspend fun getAllDeliveries(): List<Delivery>

    @POST("delivery/optimize")
    suspend fun getOptimizedRoutes(@Body optimizationRequest: OptimizeRoutesRequest): OptimizedRoutes

    @POST("delivery/create")
    suspend fun createDelivery(@Body createRequest: DeliveryCreateRequest): Delivery

    @PUT("delivery/{id}")
    suspend fun updateDelivery(
        @Path("id") id: Long,
        @Body updateRequest: DeliveryUpdateRequest
    ): Delivery

    @DELETE("delivery/{id}")
    suspend fun cancelDelivery(@Path("id") id: Long)

    @GET("delivery/{employeeId}")
    suspend fun getDeliveryByEmployeeIdAndDate(
        @Path("employeeId") employeeId: Long,
        @Query("date") date: LocalDate? = null
    ): Delivery?
}