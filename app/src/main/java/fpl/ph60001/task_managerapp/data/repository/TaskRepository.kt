package fpl.ph60001.task_managerapp.data.repository

import fpl.ph60001.task_managerapp.data.model.CreateTaskRequest
import fpl.ph60001.task_managerapp.data.model.TaskItem
import fpl.ph60001.task_managerapp.data.network.ApiClient

class TaskRepository {
    suspend fun getTasks(): List<TaskItem> = ApiClient.taskApi.getTasks()

    suspend fun createTask(title: String): TaskItem =
        ApiClient.taskApi.createTask(CreateTaskRequest(title = title))
}
