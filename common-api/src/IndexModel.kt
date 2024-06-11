data class TaskModel(
    val name: String,
) {
    companion object {
        fun from(taskData: TaskData): TaskModel = TaskModel(
            name = taskData.name,
        )
    }
}

data class PetModel(
    val name: String,
    val coins: Int,
    val happyness: Int,
) {
    companion object {
        fun from(petData: PetData): PetModel = PetModel(
            name = petData.name,
            coins = petData.coins,
            happyness = petData.happyness,
        )
    }
}

data class IndexModel(
    val petModel: PetModel,
    val tasks: List<TaskModel>,
    val speech: String,
)
