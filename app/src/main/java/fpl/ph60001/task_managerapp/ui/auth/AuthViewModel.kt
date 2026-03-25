package fpl.ph60001.task_managerapp.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.ph60001.task_managerapp.data.repository.AuthRepository
import fpl.ph60001.task_managerapp.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import fpl.ph60001.task_managerapp.data.network.ErrorUtils

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun resetState() {
        _state.value = AuthUiState()
    }

    fun login(context: Context, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState(error = "Vui long nhap day du email va mat khau")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(loading = true)
            runCatching { repository.login(email.trim(), password) }
                .onSuccess { response ->
                    SessionManager.saveToken(context, response.token)
                    _state.value = AuthUiState(success = true)
                }
                .onFailure { error ->
                    _state.value = AuthUiState(error = ErrorUtils.parseError(error, "Dang nhap that bai"))
                }
        }
    }

    fun register(context: Context, fullName: String, email: String, password: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState(error = "Vui long nhap day du thong tin")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(loading = true)
            runCatching { repository.register(fullName.trim(), email.trim(), password) }
                .onSuccess { response ->
                    SessionManager.saveToken(context, response.token)
                    _state.value = AuthUiState(success = true)
                }
                .onFailure { error ->
                    _state.value = AuthUiState(error = ErrorUtils.parseError(error, "Dang ky that bai"))
                }
        }
    }
}
