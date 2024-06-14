package data

import java.time.LocalDateTime
import java.util.UUID

data class TaskData(
    val id: UUID? = null,
    val name: String,
    val description: String,
    val priority: Priority,
    val deadline: LocalDateTime,
    val isCompleted: Boolean,
)
