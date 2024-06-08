import java.time.LocalDateTime

data class EventData(
    val name: String,
    val description: String,
    val priority: Priority,
    val deadline: LocalDateTime,
    val isCompleted: Boolean,
)
