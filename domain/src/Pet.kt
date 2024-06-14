import arrow.core.Either
import arrow.core.raise.either
import data.Id
import repository.IPetRepository
import repository.IUserRepository

context(Id, IUserRepository, IPetRepository)
fun getRandomImage(): Either<DomainError, String> = either {
    val user = getUserData().bind()
    getImages(user.currentPet.id!!).bind().filter { user.happiness in it.minHappiness..it.maxHappiness }
        .randomOrNull()?.url
        ?: raise(NotFound)
}

context(Id, IUserRepository, IPetRepository)
fun getRandomQuote(): Either<DomainError, String> = either {
    val user = getUserData().bind()
    getQuotes(user.currentPet.id!!).bind().filter { user.happiness in it.minHappiness..it.maxHappiness }
        .randomOrNull()?.quote
        ?: raise(NotFound)
}

