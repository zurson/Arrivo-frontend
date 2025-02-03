package com.thesis.arrivo.communication.delivery

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
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

    suspend fun notifyBreakStart(id: Long) {
        deliveryService.notifyBreakStart(id)
    }

    suspend fun updateDelivery(id: Long, updateRequest: DeliveryUpdateRequest): Delivery {
        return deliveryService.updateDelivery(id, updateRequest)
    }

    suspend fun updateDeliveryStatus(
        id: Long,
        statusUpdateRequest: DeliveryUpdateStatusRequest
    ): Delivery {
        return deliveryService.updateDeliveryStatus(id, statusUpdateRequest)
    }

    suspend fun cancelDelivery(id: Long) {
        deliveryService.cancelDelivery(id)
    }

    suspend fun getDeliveryByEmployeeIdAndDate(
        employeeId: Long,
        date: LocalDate? = null
    ): Response<Delivery> {
        return deliveryService.getDeliveryByEmployeeIdAndDate(employeeId, date)
    }

    suspend fun addRoutePoints(@Body request: TrackPointInsertRequest) {
        deliveryService.addRoutePoints(request)
    }


    suspend fun getRoutePoints(@Path("deliveryId") deliveryId: Long): List<RoutePoint> {
        return deliveryService.getRoutePoints(deliveryId)
    }
}