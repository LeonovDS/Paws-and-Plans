sealed interface DomainError

data object NotFound : DomainError
sealed interface InternalError : DomainError
data class TemplateRenderFailed(
    val templateError: Throwable,
) : InternalError
