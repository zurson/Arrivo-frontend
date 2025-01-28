package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.RetrofitInstance
import java.time.LocalDate

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

    suspend fun updateDelivery(id: Long, updateRequest: DeliveryUpdateRequest): Delivery {
        return deliveryService.updateDelivery(id, updateRequest)
    }

    suspend fun cancelDelivery(id: Long) {
        deliveryService.cancelDelivery(id)
    }

    suspend fun getDeliveryByEmployeeIdAndDate(
        employeeId: Long,
        date: LocalDate? = null
    ): Delivery? {
        return deliveryService.getDeliveryByEmployeeIdAndDate(employeeId, date)
    }
}