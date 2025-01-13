package com.thesis.arrivo.communication.road_accidents

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.http.Body

class RoadAccidentsRepository {
    private val roadAccidentsService = RetrofitInstance.roadAccidentsService

    suspend fun getAllRoadAccidents(): List<RoadAccident> {
        return roadAccidentsService.getAllRoadAccidents()
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
}