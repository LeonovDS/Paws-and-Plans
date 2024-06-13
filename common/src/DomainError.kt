sealed interface DomainError

data object NotFound : DomainError
sealed interface InternalError : DomainError
data class TemplateRenderFailed(
    val templateError: Throwable,
) : InternalError

data class SqlError(val sqlError: Throwable) : InternalError
data object NotAuthentificated : DomainError