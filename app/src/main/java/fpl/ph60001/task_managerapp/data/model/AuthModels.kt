package fpl.ph60001.task_managerapp.data.model

data class AuthRequest(
    val email: String,
    val password: String,
    val fullName: String? = null
)

data class AuthUser(
    val id: String,
    val fullName: String,
    val email: String
)

data class AuthResponse(
    val token: String,
    val user: AuthUser
)
