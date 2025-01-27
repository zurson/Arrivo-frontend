package com.thesis.arrivo.communication.road_accidents

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.http.Body
import retrofit2.http.Path

class RoadAccidentsRepository {
    private val roadAccidentsService = RetrofitInstance.roadAccidentsService

    suspend fun getAllRoadAccidents(): List<RoadAccident> {
        return roadAccidentsService.getAllRoadAccidents()
    }

    suspend fun getAllRoadAccidents(id: Long): List<RoadAccident> {
        return roadAccidentsService.getAllRoadAccidents(id)
    }

    suspend fun createRoadAccident(@Body roadAccidentCreateRequest: RoadAccidentCreateRequest) {
        roadAccidentsService.createRoadAccident(roadAccidentCreateRequest)
    }

    suspend fun updateRoadAccident(
        id: Long,
        roadAccidentUpdateRequest: RoadAccidentUpdateRequest
    ) {
        roadAccidentsService.updateRoadAccident(id, roadAccidentUpdateRequest)
    }

    suspend fun markRoadAccidentAsResolved(id: Long) {
        roadAccidentsService.markRoadAccidentAsResolved(id)
    }
}