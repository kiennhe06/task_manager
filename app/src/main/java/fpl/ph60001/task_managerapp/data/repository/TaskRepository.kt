package fpl.ph60001.task_managerapp.data.repository

import fpl.ph60001.task_managerapp.data.model.CreateTaskRequest
import fpl.ph60001.task_managerapp.data.model.TaskItem
import fpl.ph60001.task_managerapp.data.model.TaskStats
import fpl.ph60001.task_managerapp.data.network.ApiClient

class TaskRepository {
    suspend fun getTasks(search: String? = null, status: String? = null): List<TaskItem> {
        val response = ApiClient.taskApi.getTasks(search = search, status = status)
        return response.data ?: emptyList()
    }

    suspend fun getStats(): TaskStats {
        val response = ApiClient.taskApi.getStats()
        return response.data ?: TaskStats()
    }

    suspend fun createTask(title: String, description: String = "", priority: String = "medium", dueDate: String? = null): TaskItem {
        val request = CreateTaskRequest(
            title = title,
            description = description,
            priority = priority,
            dueDate = dueDate
        )
        val response = ApiClient.taskApi.createTask(request)
        return response.data ?: throw Exception(response.message)
    }

    suspend fun updateTask(task: TaskItem): TaskItem {
        val response = ApiClient.taskApi.updateTask(
            id = task._id,
            request = CreateTaskRequest(
                title = task.title,
                description = task.description,
                status = task.status,
                priority = task.priority,
                dueDate = task.dueDate
            )
        )
        return response.data ?: throw Exception(response.message)
    }

    suspend fun deleteTask(id: String) {
        ApiClient.taskApi.deleteTask(id)
    }
}
