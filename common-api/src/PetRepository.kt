import arrow.core.Either

interface PetRepository {
    fun getPet(): Either<DomainError, PetData>
}