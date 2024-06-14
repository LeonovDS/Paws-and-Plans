import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import data.Id
import data.UserData
import repository.IAuthRepository
import repository.IPetRepository
import repository.IUserRepository
import java.security.MessageDigest
import java.util.*

fun String.encrypt(): String {
    val md = MessageDigest.getInstance("SHA-512")
    return md.digest(this.encodeToByteArray()).toString(Charsets.UTF_8)
}

context(IAuthRepository, IUserRepository, IPetRepository)
fun signUp(data: SignUpData): Either<DomainError, Unit> = either {
    val id = createAuthData(data.copy(password = data.password.encrypt())).bind()
    with(Id(id)) {
        createUser(
            UserData(
                petName = data.petName,
                happiness = 10,
                coins = 0,
                currentPet = getPet(data.petId).bind()
            )
        ).bind()
    }
    addPetInPossession(id, data.petId).bind()
    Unit.right()
}

context(IAuthRepository)
fun validateUser(data: SignInData): Either<DomainError, UUID> = getAuthId(data.copy(password = data.password.encrypt()))
