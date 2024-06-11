import arrow.core.Either

class TaskRepositoryImpl : TaskRepository {
    override fun getTasks(): Either<DomainError, List<TaskData>> = getTasks()
}