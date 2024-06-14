import arrow.core.Either
import data.PetData
import model.SignUpModel
import repository.IPetRepository

context(IPetRepository)
fun getPetSamples(): Either<DomainError, SignUpModel> = getAllPets().map { SignUpModel(it) }