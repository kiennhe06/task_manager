package fpl.ph60001.task_managerapp.data.network

import fpl.ph60001.task_managerapp.data.model.ApiResponse
import fpl.ph60001.task_managerapp.data.model.CreateTaskRequest
import fpl.ph60001.task_managerapp.data.model.TaskItem
import fpl.ph60001.task_managerapp.data.model.TaskStats
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(
        @Query("search") search: String? = null,
        @Query("status") status: String? = null
    ): ApiResponse<List<TaskItem>>

    @GET("tasks/stats")
    suspend fun getStats(): ApiResponse<TaskStats>

    @POST("tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): ApiResponse<TaskItem>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body request: CreateTaskRequest): ApiResponse<TaskItem>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): ApiResponse<Any>
}
