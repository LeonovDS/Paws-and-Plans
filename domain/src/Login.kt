import arrow.core.Either
import data.PetData
import repository.IPetRepository

context(IPetRepository)
fun getPetSamples(): Either<DomainError, List<PetData>> = getAllPets()