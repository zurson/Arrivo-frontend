package com.thesis.arrivo.communication.delivery

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @PATCH("delivery/break/{id}")
    suspend fun notifyBreakStart(@Path("id") id: Long)

    @PATCH("delivery/{id}")
    suspend fun updateDeliveryStatus(
        @Path("id") id: Long,
        @Body statusUpdateRequest: DeliveryUpdateStatusRequest
    ): Delivery

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
    ): Response<Delivery>
}