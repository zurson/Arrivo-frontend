package com.thesis.arrivo.communication.road_accidents

import com.thesis.arrivo.communication.employee.Employee
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoadAccidentsService {

    @GET("road-accidents")
    suspend fun getAllRoadAccidents(): List<Employee>

    @POST("road-accidents")
    suspend fun createRoadAccident(@Body roadAccidentCreateRequest: RoadAccidentCreateRequest)

    @PUT("road-accidents/{id}")
    suspend fun updateRoadAccident(
        @Path("id") id: Long,
        @Body roadAccidentUpdateRequest: RoadAccidentUpdateRequest
    )

}