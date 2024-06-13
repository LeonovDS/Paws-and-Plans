import arrow.core.Either
import repository.IAuthRepository
import java.security.MessageDigest
import java.util.*

fun String.encrypt(): String {
    val md = MessageDigest.getInstance("SHA-512")
    return md.digest(this.encodeToByteArray()).toString(Charsets.UTF_8)
}

context(IAuthRepository)
fun signUp(data: SignUpData): Either<DomainError, Unit> =
    createAuthData(data.copy(password = data.password.encrypt())).map { }

context(IAuthRepository)
fun validateUser(data: SignInData): Either<DomainError, UUID> =
    getAuthId(data.copy(password = data.password.encrypt()))
