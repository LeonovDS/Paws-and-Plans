import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.http4k.core.Body
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.cookies
import org.http4k.core.cookie.removeCookie
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import org.http4k.routing.routes
import java.time.Instant
import java.util.*
import kotlin.time.Duration

val loginLens = FormField.required("login")
val passwordLens = FormField.required("password")

val loginFormLens = Body.webForm(
    Validator.Strict,
    loginLens,
    passwordLens
).toLens()

data class LoginData(val login: String, val password: String)

fun loginLens(request: Request): LoginData {
    val form = loginFormLens(request)
    val login = loginLens(form)
    val password = passwordLens(form)
    return LoginData(login, password)
}

fun signInRoute(request: Request): Response {
    return Response(Status.PERMANENT_REDIRECT)
        .header("Location", "/")
        .cookie(Cookie("auth_token", ""))
}

fun signUpRoute(request: Request): Response {
    return Response(Status.PERMANENT_REDIRECT)
        .header("Location", "/SignIn.html")
        .cookie(Cookie("auth_token", ""))
}

val algorithm = Algorithm.HMAC256("temp_secret") //todo: get from config

val jwtVerifyer = JWT.require(algorithm).withIssuer("paws-and-plans").build() //todo: get from config

fun createAuthToken(id: UUID): String {
    return JWT.create()
        .withSubject("paws-and-plans")
        .withIssuer("paws-and-plans")
        .withClaim("id", id.toString())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now() + java.time.Duration.ofDays(7))
        .sign(algorithm)
}

fun validateToken(token: String) : Boolean {
    try {
        jwtVerifyer!!.verify(token)!!
        return true
    } catch (e: Exception) {
        return false
    }
}

val loginFilter = Filter { handler ->
    { request ->
        val token = request.cookies().find { it.name == "auth_token" }
        if (token == null || !validateToken(token.value)) {
            Response(Status.PERMANENT_REDIRECT)
                .header("Location", "/SignIn.html")
                .removeCookie("auth_token")
        } else {
            handler(request)
        }
    }
}

val authRoutes = routes(
    "/auth/signin" bind ::signInRoute,
    "/auth/signUp" bind ::signUpRoute
)