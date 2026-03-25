package fpl.ph60001.task_managerapp.data.network

import fpl.ph60001.task_managerapp.data.model.ApiResponse
import fpl.ph60001.task_managerapp.data.model.AuthRequest
import fpl.ph60001.task_managerapp.data.model.AuthResponse
import fpl.ph60001.task_managerapp.data.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): ApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): ApiResponse<AuthResponse>

    @GET("auth/profile")
    suspend fun getProfile(): ApiResponse<UserProfile>
}
