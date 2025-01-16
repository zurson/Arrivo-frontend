package com.thesis.arrivo.communication.delivery

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DeliveryService {

    @GET("delivery")
    suspend fun getAllDeliveries(): List<Delivery>

    @POST("delivery/optimize")
    suspend fun getOptimizedRoutes(@Body optimizationRequest: OptimizeRoutesRequest): OptimizedRoutes

    @POST("delivery/create")
    suspend fun createDelivery(@Body createRequest: DeliveryCreateRequest): Delivery
}