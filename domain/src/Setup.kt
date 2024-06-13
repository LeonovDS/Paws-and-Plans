import arrow.core.Either
import arrow.core.raise.either
import data.Id
import data.UserData
import repository.IPetRepository
import repository.IUserRepository

data class SetupData(
    val petName: String,
    val petKind: String,
)

context(Id, IUserRepository, IPetRepository)
fun setup(data: SetupData): Either<DomainError, Unit> = either {
    val pet = getPetByKind(data.petKind).bind()
    addPetInPossession(id, pet.id!!)
    createUser(
        UserData(
            petName = data.petName,
            happiness = 10, //TODO: Introduce constant for default value
            coins = 0,
            currentPet = pet
        )
    ).bind()
}

