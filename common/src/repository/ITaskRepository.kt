package repository

import DomainError
import arrow.core.Either
import data.Id
import data.Priority
import data.PriorityData
import data.TaskData
import java.util.UUID

interface ITaskRepository {
    context(Id)
    fun getAllTasks(): Either<DomainError, List<TaskData>>
    context(Id)
    fun createTask(taskData: TaskData): Either<DomainError, UUID>
    context(Id)
    fun getTask(taskId: UUID): Either<DomainError, TaskData>
    context(Id)
    fun updateTask(taskData: TaskData): Either<DomainError, Unit>
    context(Id)
    fun deleteTask(taskId: UUID): Either<DomainError, Unit>
    context(Id)
    fun completeTask(taskId: UUID): Either<DomainError, Unit>
    context(Id)
    fun getPriorityData(priority: Priority): Either<DomainError, PriorityData>
}