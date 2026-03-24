package fpl.ph60001.task_managerapp.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.ph60001.task_managerapp.data.model.TaskItem
import fpl.ph60001.task_managerapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TaskUiState(
    val loading: Boolean = false,
    val tasks: List<TaskItem> = emptyList(),
    val error: String? = null
)

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(TaskUiState(loading = true))
    val state: StateFlow<TaskUiState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching { repository.getTasks() }
                .onSuccess { tasks ->
                    _state.value = TaskUiState(loading = false, tasks = tasks)
                }
                .onFailure { error ->
                    _state.value = TaskUiState(
                        loading = false,
                        tasks = emptyList(),
                        error = error.message ?: "Khong tai duoc danh sach cong viec"
                    )
                }
        }
    }

    fun createTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            runCatching { repository.createTask(title.trim()) }
                .onSuccess { loadTasks() }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Khong tao duoc cong viec"
                    )
                }
        }
    }
}
