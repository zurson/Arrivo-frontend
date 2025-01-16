package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.RetrofitInstance

class DeliveryRepository {

    private val deliveryService = RetrofitInstance.deliveryService

    suspend fun getAllDeliveries(): List<Delivery> {
        return deliveryService.getAllDeliveries()
    }

    suspend fun getOptimizedRoutes(optimizationRequest: OptimizeRoutesRequest): OptimizedRoutes {
        return deliveryService.getOptimizedRoutes(optimizationRequest)
    }

    suspend fun createDelivery(createRequest: DeliveryCreateRequest): Delivery {
        return deliveryService.createDelivery(createRequest)
    }

}