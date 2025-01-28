package com.thesis.arrivo.communication.task

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TasksService {

    @GET("tasks")
    suspend fun getAllTasks(): List<Task>

    @POST("tasks")
    suspend fun createTask(@Body taskCreateRequest: TaskCreateRequest)

    @POST("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body taskUpdateRequest: TaskUpdateRequest
    )

    @PATCH("tasks/{id}")
    suspend fun updateTaskStatus(
        @Path("id") id: Long,
        @Body statusUpdateRequest: TaskStatusUpdateRequest
    ): Task

    @GET("tasks/free-tasks")
    suspend fun getFreeTasks(): List<Task>
}