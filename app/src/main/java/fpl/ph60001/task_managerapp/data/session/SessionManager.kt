package fpl.ph60001.task_managerapp.data.session

import android.content.Context

object SessionManager {
    private const val PREF_NAME = "task_manager_session"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_DARK_MODE = "dark_mode"

    @Volatile
    private var token: String? = null
    @Volatile
    private var isDarkMode: Boolean = false
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        token = pref.getString(KEY_TOKEN, null)
        isDarkMode = pref.getBoolean(KEY_DARK_MODE, false)
        initialized = true
    }

    fun getToken(): String? = token

    fun saveToken(context: Context, newToken: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, newToken)
            .apply()
        token = newToken
    }

    fun clearToken(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_TOKEN)
            .apply()
        token = null
    }

    fun isDarkMode(): Boolean = isDarkMode

    fun setDarkMode(context: Context, dark: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_MODE, dark)
            .apply()
        isDarkMode = dark
    }
}
