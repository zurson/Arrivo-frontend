package com.thesis.arrivo.communication.road_accidents

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoadAccidentsService {

    @GET("road-accidents")
    suspend fun getAllRoadAccidents(): List<RoadAccident>

    @POST("road-accidents")
    suspend fun createRoadAccident(@Body roadAccidentCreateRequest: RoadAccidentCreateRequest)

    @PUT("road-accidents/{id}")
    suspend fun updateRoadAccident(
        @Path("id") id: Long,
        @Body roadAccidentUpdateRequest: RoadAccidentUpdateRequest
    )

    @PATCH("road-accidents/{id}")
    suspend fun markRoadAccidentAsResolved(
        @Path("id") id: Long,
    )

}