package fpl.ph60001.task_managerapp.data.model

data class TaskItem(
    val _id: String,
    val title: String,
    val description: String = "",
    val status: String = "todo",
    val priority: String = "medium"
)

data class CreateTaskRequest(
    val title: String,
    val description: String = "",
    val status: String = "todo",
    val priority: String = "medium"
)
