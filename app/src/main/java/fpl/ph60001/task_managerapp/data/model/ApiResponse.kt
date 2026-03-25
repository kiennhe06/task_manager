package fpl.ph60001.task_managerapp.data.model

/**
 * Generic API response wrapper matching backend format:
 * { success: Boolean, message: String, data: T? }
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
