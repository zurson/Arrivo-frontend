package com.thesis.arrivo.communication.task

import com.thesis.arrivo.communication.RetrofitInstance
import retrofit2.http.Body

class TasksRepository {
    private val taskService = RetrofitInstance.tasksService

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
        taskService.updateTask(id, taskUpdateRequest)
    }

    suspend fun getFreeTasks(): List<Task> {
        return taskService.getFreeTasks()
    }
}