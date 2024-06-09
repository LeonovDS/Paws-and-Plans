package presentation

data class TaskModel(
    val name: String,
)

data class IndexModel(
    val name: String,
    val coins: Int,
    val happyness: Int,
    val tasks: List<TaskModel>,
    val speech: String,
)
