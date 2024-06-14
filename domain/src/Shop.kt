import arrow.core.Either
import arrow.core.raise.either
import data.Id
import model.Pet
import model.ShopModel
import repository.IPetRepository
import repository.IUserRepository
import java.util.UUID

context(Id, IUserRepository, IPetRepository)
fun getShop(): Either<DomainError, ShopModel> = either {
    val user = getUserData().bind()
    val allPets = getAllPets().bind()
    val pets = getPetsInPossession(id).bind()
    ShopModel(pets = allPets.map { pet ->
        Pet(
            id = pet.id!!,
            kind = pet.kind,
            price = pet.price,
            isOwned = pets.find { it == pet.id } != null,
            isSelected = user.currentPet.id == pet.id,
            image = getImages(pet.id!!).bind().maxBy { it.minHappiness }.url
        )
    })
}

context(Id, IUserRepository, IPetRepository)
fun buyPet(petId: UUID): Either<DomainError, ShopModel> = either {
    val user = getUserData().bind()
    val pet = getPet(petId).bind()
    updateCoins(user.coins - pet.price).bind()
    addPetInPossession(id, pet.id!!).bind()
    getShop().bind()
}