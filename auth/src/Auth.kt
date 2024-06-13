import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.Instant
import java.util.*

class Auth {
    private val algorithm = Algorithm.HMAC256("temp_secret") //todo: get from config
    private val jwtVerifier = JWT.require(algorithm)
        .withIssuer("paws-and-plans")
        .build() //todo: get from config

    fun createAuthToken(id: UUID): String = JWT.create()
        .withSubject("paws-and-plans")
        .withIssuer("paws-and-plans")
        .withClaim("id", id.toString())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now() + java.time.Duration.ofDays(7))
        .sign(algorithm)

    fun validateToken(token: String): UUID? = try {
        UUID.fromString(jwtVerifier!!.verify(token)!!.getClaim("id").asString())
    } catch (e: Exception) {
        null
    }
}
