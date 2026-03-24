package fpl.ph60001.task_managerapp.data.network

import fpl.ph60001.task_managerapp.data.model.CreateTaskRequest
import fpl.ph60001.task_managerapp.data.model.TaskItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): List<TaskItem>

    @POST("tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): TaskItem
}
