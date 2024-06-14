import java.util.*

data class SignUpData(
    val login: String,
    val password: String,
    val petName: String,
    val petId: UUID,
)
data class SignInData(
    val login: String,
    val password: String,
)
