import arrow.core.Either
import arrow.core.raise.either
import data.Id
import data.TaskData
import repository.ITaskRepository

context(Id, ITaskRepository)
fun getTasks(): Either<DomainError, List<TaskData>> = getAllTasks()

context(Id, ITaskRepository)
fun create(data: TaskData): Either<DomainError, TaskData> = either {
    val taskId = createTask(taskData = data).bind()
    getTask(taskId).bind()
}