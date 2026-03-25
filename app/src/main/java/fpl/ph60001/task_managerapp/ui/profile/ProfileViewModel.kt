package fpl.ph60001.task_managerapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.ph60001.task_managerapp.data.model.UserProfile
import fpl.ph60001.task_managerapp.data.network.ErrorUtils
import fpl.ph60001.task_managerapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null
)

class ProfileViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState(isLoading = true))
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { repository.getProfile() }
                .onSuccess { profile ->
                    _state.value = ProfileUiState(isLoading = false, profile = profile)
                }
                .onFailure { error ->
                    _state.value = ProfileUiState(
                        isLoading = false,
                        error = ErrorUtils.parseError(error, "Khong tai duoc thong tin")
                    )
                }
        }
    }
}
