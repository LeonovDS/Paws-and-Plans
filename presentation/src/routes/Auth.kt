package routes

import Auth
import SignInData
import SignUpData
import arrow.core.flatMap
import arrow.core.right
import authLens
import data.Id
import getPetSamples
import getTemplateEngine
import kotlinx.serialization.Serializable
import makeUserlessRoute
import model.SignInModel
import model.SignUpModel
import org.http4k.core.Body
import org.http4k.core.Filter
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.cookies
import org.http4k.core.cookie.removeCookie
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.slf4j.Logger
import renderTemplate
import repository.IAuthRepository
import repository.IMultiRepository
import repository.IPetRepository
import repository.IUserRepository
import respondError
import respondSuccess
import signUp
import validateUser
import java.util.*

private val auth = Auth()

context(Logger, IMultiRepository)
internal fun `GET sign-up`(): RoutingHttpHandler = "/sign-up" bind Method.GET to
        makeUserlessRoute { _ ->
            getPetSamples()
        }

context(Logger, IMultiRepository)
internal fun `GET sign-in`(): RoutingHttpHandler = "/sign-in" bind Method.GET to
        makeUserlessRoute { _ -> SignInModel.right() }

@Serializable
data class SignUp(
    val login: String,
    val password: String,
    val petName: String,
    val petKind: String
)

context(Logger, IMultiRepository)
internal fun `POST sign-up`(): RoutingHttpHandler = "/sign-up" bind Method.POST to
        makeUserlessRoute {
            val signUpData = Body.auto<SignUp>().toLens()(it)
            info("POST /sign-up: $signUpData")
            signUp(
                SignUpData(
                    signUpData.login,
                    signUpData.password,
                    signUpData.petName,
                    UUID.fromString(signUpData.petKind)
                )
            )
        }

context(Logger, IAuthRepository)
internal fun `POST sign-in`(): RoutingHttpHandler = "/sign-in" bind Method.POST to { request: Request ->
    val signInLens = Body.auto<SignIn>().toLens()
    val signInData = signInLens(request)
    info("POST /sign-in: $signInData")
    validateUser(SignInData(signInData.login, signInData.password)).fold(::respondError) { uuid ->
        respondSuccess("")
            .header("HX-Redirect", "/")
            .cookie(Cookie("auth", auth.createAuthToken(uuid)))
    }
}

@Serializable
data class SignIn(
    val login: String,
    val password: String,
)

context(Logger, IMultiRepository)
internal fun authFilter() = Filter { handler ->
    { request: Request ->
        val id = request.cookies().find { it.name == "auth" }?.value?.let {
            auth.validateToken(it)
        }
        if (id == null) {
            info("Attempt to access ${request.uri} without auth")
            `GET sign-in`()(request.uri(Uri.of("/sign-in"))).removeCookie("auth")
        } else {
            info("Authentificated: ${request.uri}")
            handler(authLens(Id(id), request))
        }
    }
}
