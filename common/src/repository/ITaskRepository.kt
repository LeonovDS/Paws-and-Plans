package repository

import DomainError
import arrow.core.Either
import data.Id
import data.TaskData
import java.util.UUID

interface ITaskRepository {
    context(Id)
    fun getAllTasks(): Either<DomainError, List<TaskData>>
    context(Id)
    fun createTask(taskData: TaskData): Either<DomainError, UUID>
    context(Id)
    fun getTask(taskId: UUID): Either<DomainError, TaskData>
}