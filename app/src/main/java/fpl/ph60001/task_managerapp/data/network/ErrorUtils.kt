package fpl.ph60001.task_managerapp.data.network

import org.json.JSONObject
import retrofit2.HttpException

object ErrorUtils {
    fun parseError(error: Throwable, fallback: String = "Lỗi không xác định"): String {
        if (error is HttpException) {
            try {
                val errorBody = error.response()?.errorBody()?.string()
                if (!errorBody.isNullOrEmpty()) {
                    val json = JSONObject(errorBody)
                    if (json.has("message")) {
                        return json.getString("message")
                    }
                }
            } catch (e: Exception) {
                // Return fallback below
            }
        }
        return error.message ?: fallback
    }
}
