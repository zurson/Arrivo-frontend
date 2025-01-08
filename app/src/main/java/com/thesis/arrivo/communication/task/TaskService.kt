package com.thesis.arrivo.communication.task

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskService {

    @GET("tasks")
    suspend fun getAllTasks(): List<Task>

    @POST("tasks")
    suspend fun createTask(@Body taskCreateRequest: TaskCreateRequest)

    @PATCH("tasks/{id}")
    suspend fun updateTest(
        @Path("id") id: Long,
        @Body taskUpdateRequest: TaskUpdateRequest
    )

}