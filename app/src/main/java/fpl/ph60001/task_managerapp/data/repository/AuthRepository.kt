package fpl.ph60001.task_managerapp.data.repository

import fpl.ph60001.task_managerapp.data.model.AuthRequest
import fpl.ph60001.task_managerapp.data.model.AuthResponse
import fpl.ph60001.task_managerapp.data.model.UserProfile
import fpl.ph60001.task_managerapp.data.network.ApiClient

class AuthRepository {
    suspend fun login(email: String, password: String): AuthResponse {
        val response = ApiClient.authApi.login(AuthRequest(email = email, password = password))
        return response.data ?: throw Exception(response.message)
    }

    suspend fun register(fullName: String, email: String, password: String): AuthResponse {
        val response = ApiClient.authApi.register(
            AuthRequest(fullName = fullName, email = email, password = password)
        )
        return response.data ?: throw Exception(response.message)
    }

    suspend fun getProfile(): UserProfile {
        val response = ApiClient.authApi.getProfile()
        return response.data ?: throw Exception(response.message)
    }
}
