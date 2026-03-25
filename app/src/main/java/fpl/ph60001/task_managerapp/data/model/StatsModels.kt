package fpl.ph60001.task_managerapp.data.model

data class TaskStats(
    val total: Int = 0,
    val todo: Int = 0,
    val inProgress: Int = 0,
    val done: Int = 0,
    val overdue: Int = 0
)

data class UserProfile(
    val id: String,
    val fullName: String,
    val email: String,
    val createdAt: String? = null,
    val taskCount: Int = 0
)
