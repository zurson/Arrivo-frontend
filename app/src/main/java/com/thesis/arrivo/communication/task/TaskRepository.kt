package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.http.Body

class TaskRepository {
    private val taskService = RetrofitInstance.taskService

    suspend fun getAllTasks(): List<Task> {
        return taskService.getAllTasks()
    }

    suspend fun createTask(@Body taskCreateRequest: TaskCreateRequest) {
        taskService.createTask(taskCreateRequest)
    }

    suspend fun updateTask(
        id: Long,
        taskUpdateRequest: TaskUpdateRequest
    ) {
        taskService.updateTest(id, taskUpdateRequest)
    }
}