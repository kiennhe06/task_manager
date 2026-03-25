package fpl.ph60001.task_managerapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.ph60001.task_managerapp.data.model.TaskStats
import fpl.ph60001.task_managerapp.data.network.ErrorUtils
import fpl.ph60001.task_managerapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val stats: TaskStats = TaskStats(),
    val error: String? = null
)

class DashboardViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardUiState(isLoading = true))
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { repository.getStats() }
                .onSuccess { stats ->
                    _state.value = DashboardUiState(isLoading = false, stats = stats)
                }
                .onFailure { error ->
                    _state.value = DashboardUiState(
                        isLoading = false,
                        error = ErrorUtils.parseError(error, "Khong tai duoc thong ke")
                    )
                }
        }
    }
}
