import arrow.core.Either

interface TaskRepository {
    fun getTasks(): Either<DomainError, List<TaskData>>
}