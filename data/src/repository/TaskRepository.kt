package repository

import DomainError
import NotFound
import SqlError
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import data.Id
import data.Priority
import data.TaskData
import org.ktorm.database.Database
import org.ktorm.dsl.*
import table.TaskTable
import java.util.UUID

class TaskRepository(private val db: Database) : ITaskRepository {
    context(Id)
    override fun getAllTasks(): Either<DomainError, List<TaskData>> = catch({
        db.from(TaskTable)
            .select()
            .where(TaskTable.userId eq id)
            .asIterable()
            .map {
                TaskData(
                    name = it[TaskTable.name]!!,
                    description = it[TaskTable.description]!!,
                    priority = Priority.valueOf(it[TaskTable.priority]!!.uppercase()),
                    deadline = it[TaskTable.deadline]!!,
                    isCompleted = it[TaskTable.isCompleted]!!,
                )
            }
            .right()
    }) {
        SqlError(it).left()
    }

    context(Id) override fun createTask(taskData: TaskData): Either<DomainError, UUID> = catch({
        (db.insertAndGenerateKey(TaskTable) {
            set(TaskTable.userId, id)
            set(TaskTable.name, taskData.name)
            set(TaskTable.description, taskData.description)
            set(TaskTable.priority, taskData.priority.toString())
            set(TaskTable.deadline, taskData.deadline)
        } as? UUID)?.right() ?: NotFound.left()
    }) {
        SqlError(it).left() // TODO: Introduce some wrapper
    }

    context(Id) override fun getTask(taskId: UUID): Either<DomainError, TaskData> = catch({
        db.from(TaskTable)
            .select()
            .where((TaskTable.userId eq id) and (TaskTable.id eq taskId))
            .asIterable()
            .map {
                TaskData(
                    name = it[TaskTable.name]!!,
                    description = it[TaskTable.description]!!,
                    priority = Priority.valueOf(it[TaskTable.priority]!!.uppercase()),
                    deadline = it[TaskTable.deadline]!!,
                    isCompleted = it[TaskTable.isCompleted]!!,
                )
            }
            .firstOrNull()
            ?.right() ?: NotFound.left()
    }) {
        SqlError(it).left()
    }
}
