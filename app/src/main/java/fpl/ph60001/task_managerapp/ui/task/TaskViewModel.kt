package fpl.ph60001.task_managerapp.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpl.ph60001.task_managerapp.data.model.TaskItem
import fpl.ph60001.task_managerapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import fpl.ph60001.task_managerapp.data.network.ErrorUtils

data class TaskUiState(
    val isFetching: Boolean = false,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val tasks: List<TaskItem> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val filterStatus: String? = null // null = all
)

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(TaskUiState(isFetching = true))
    val state: StateFlow<TaskUiState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isFetching = true, error = null)
            val search = _state.value.searchQuery.takeIf { it.isNotBlank() }
            val status = _state.value.filterStatus
            runCatching { repository.getTasks(search = search, status = status) }
                .onSuccess { tasks ->
                    _state.value = _state.value.copy(isFetching = false, tasks = tasks, error = null)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isFetching = false,
                        tasks = emptyList(),
                        error = ErrorUtils.parseError(error, "Khong tai duoc danh sach cong viec")
                    )
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        loadTasks()
    }

    fun onFilterStatusChanged(status: String?) {
        _state.value = _state.value.copy(filterStatus = status)
        loadTasks()
    }

    fun createTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)
            runCatching { repository.createTask(title.trim()) }
                .onSuccess {
                    _state.value = _state.value.copy(isCreating = false)
                    loadTasks()
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = ErrorUtils.parseError(error, "Khong tao duoc cong viec")
                    )
                }
        }
    }

    fun updateTask(task: TaskItem) {
        if (task.title.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, error = null)
            runCatching { repository.updateTask(task) }
                .onSuccess {
                    _state.value = _state.value.copy(isUpdating = false)
                    loadTasks()
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        error = ErrorUtils.parseError(error, "Khong cap nhat duoc cong viec")
                    )
                }
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)
            runCatching { repository.deleteTask(id) }
                .onSuccess {
                    _state.value = _state.value.copy(isDeleting = false)
                    loadTasks()
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = ErrorUtils.parseError(error, "Khong xoa duoc cong viec")
                    )
                }
        }
    }
}
