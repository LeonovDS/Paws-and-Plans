import arrow.core.Either
import arrow.core.raise.either
import data.Id
import data.TaskData
import model.TasksModel
import repository.ITaskRepository
import repository.IUserRepository
import java.util.*
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow

context(Id, ITaskRepository)
fun getTasks(): Either<DomainError, TasksModel> = either {
    TasksModel(tasks = getAllTasks().bind(), null)
}

context(Id, ITaskRepository)
fun getTasks(taskId: UUID): Either<DomainError, TasksModel> = either {
    val tasks = getAllTasks().bind()
    TasksModel(
        tasks = tasks,
        currentTask = tasks.firstOrNull { it.id == taskId } ?: raise(NotFound)
    )
}

context(Id, ITaskRepository)
fun create(data: TaskData): Either<DomainError, TaskData> = either {
    val taskId = createTask(taskData = data).bind()
    getTask(taskId).bind()
}

context(Id, ITaskRepository)
fun deleteTaskDomain(taskId: UUID): Either<DomainError, TasksModel> = either {
    deleteTask(taskId).bind()
    TasksModel(
        tasks = getAllTasks().bind(),
        currentTask = null
    )
}

context(Id, ITaskRepository)
fun updateTaskDomain(data: TaskData): Either<DomainError, TasksModel> = either {
    updateTask(taskData = data).bind()
    TasksModel(
        tasks = getAllTasks().bind(),
        currentTask = data
    )
}

const val scale = 100.0
const val power = 2.0
fun sigmoid(x: Double): Double = 2 * scale * (1 / (1 + exp(-x)) - 0.5).pow(power)
fun invsigmoid(x: Double): Double = -ln(1 / ((x / 2/ scale).pow(1 / power) + 0.5) - 1)

fun calculateHappiness(currentHappiness: Int, delta: Int): Int {
    println("currentHappiness: $currentHappiness, delta: $delta")
    val x = invsigmoid(currentHappiness.toDouble())
    println("x: $x")
    val xDelta = x + delta
    println("xDelta: $xDelta")
    val y = sigmoid(xDelta)
    println("y: $y, int: ${y.toInt()}")
    return y.toInt()
}

context(Id, ITaskRepository, IUserRepository)
fun completeTaskDomain(taskId: UUID): Either<DomainError, TasksModel> = either {
    val task = getTask(taskId).bind()
    val userData = getUserData().bind()
    val priority = getPriorityData(task.priority).bind()
    updateCoins(userData.coins + priority.coinsAward).bind()
    updateHappiness(calculateHappiness(userData.happiness,  priority.happinessAward)).bind()
    completeTask(taskId).bind()
    TasksModel(
        tasks = getAllTasks().bind(),
        currentTask = null
    )
}