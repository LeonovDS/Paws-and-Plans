import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.ktorm.database.Database
import org.ktorm.dsl.asIterable
import org.ktorm.dsl.from
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.select
import table.TaskTable
import java.util.*

context(Database)
private fun insertTask(taskData: TaskData): UUID? =
    insertAndGenerateKey(TaskTable) {
        set(it.name, taskData.name)
        set(it.description, taskData.description)
        set(it.priority, taskData.priority.name)
        set(it.deadline, taskData.deadline)
        set(it.isCompleted, taskData.isCompleted)
    } as? UUID

context(Database)
fun createTask(taskData: TaskData): Either<DomainError, UUID> =
    insertTask(taskData)
        ?.right() ?: NotFound.left()

context(Database)
fun getTasks(): Either<DomainError, List<TaskData>> =
    from(TaskTable)
        .select()
        .asIterable()
        .map {
            TaskData(
                it[TaskTable.name]!!,
                it[TaskTable.description]!!,
                Priority.valueOf(it[TaskTable.priority]!!),
                it[TaskTable.deadline]!!,
                it[TaskTable.isCompleted]!!,
            )
        }.right()