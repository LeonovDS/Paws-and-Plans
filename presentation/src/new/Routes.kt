package new

import Auth
import SignInData
import SignUpData
import SqlError
import arrow.core.flatMap
import arrow.core.right
import buyPet
import completeTaskDomain
import create
import data.Id
import data.Priority
import data.TaskData
import deleteTaskDomain
import getIndex
import getPetSamples
import getShop
import getTasks
import getTemplateEngine
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import model.SignInModel
import model.SignUpModel
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.cookies
import org.http4k.core.cookie.removeCookie
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Header
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.slf4j.Logger
import renderTemplate
import repository.*
import respondError
import respondSuccess
import selectPet
import signUp
import validateUser
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

private val intTemplateEngine = getTemplateEngine()
private val auth = Auth()

context(Logger, IPetRepository)
internal fun `GET sign-up`(): RoutingHttpHandler = "/sign-up" bind Method.GET to { _ ->
    getPetSamples().flatMap {
        intTemplateEngine.renderTemplate("Login.kte", SignUpModel(it))
    }.fold(::respondError, ::respondSuccess)
}

internal fun `GET sign-in`(): RoutingHttpHandler = "/sign-in" bind Method.GET to { _ ->
    intTemplateEngine.renderTemplate("Login.kte", SignInModel).fold(::respondError, ::respondSuccess)
}

@Serializable
data class SignUp(
    val login: String,
    val password: String,
    val petName: String,
    val petKind: String
)

context(Logger, IAuthRepository, IUserRepository, IPetRepository)
internal fun `POST sign-up`(): RoutingHttpHandler = "/sign-up" bind Method.POST to { request: Request ->
    val signUpLens = Body.auto<SignUp>().toLens()
    val signUpData = signUpLens(request)
    info("POST /sign-up: $signUpData")
    signUp(
        SignUpData(
            signUpData.login,
            signUpData.password,
            signUpData.petName,
            UUID.fromString(signUpData.petKind)
        )
    ).fold(::respondError) { _ ->
        respondSuccess("").header("HX-Redirect", "/sign-in")
    }
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

fun placeholder(request: Request): Response =
    Response(Status.NOT_IMPLEMENTED).body("Not Implemented: ${request.method} ${request.uri}")

val authLens =
    Header
        .map(
            { Id(UUID.fromString(it)) },
            { it.id.toString() }
        ).required("id")

context(Logger)
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

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `GET |`(): RoutingHttpHandler = "/" bind Method.GET to { request ->
    with(authLens(request)) {
        getIndex().flatMap { intTemplateEngine.renderTemplate("Index.kte", it) }.fold({
            Response(Status.TEMPORARY_REDIRECT).header("Location", "/sign-in")
        }, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `GET tasks`(): RoutingHttpHandler = "/tasks" bind Method.GET to { request: Request ->
    with(authLens(request)) {
        getTasks().flatMap {
            intTemplateEngine.renderTemplate("Tasks.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `GET tasks|{id}`(): RoutingHttpHandler = "/tasks/{id}" bind Method.GET to { request: Request ->
    with(authLens(request)) {
        val taskId = UUID.fromString(request.path("id"))
        getTasks(taskId).flatMap {
            intTemplateEngine.renderTemplate("Tasks.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, IUserRepository, IPetRepository)
internal fun `GET shop`(): RoutingHttpHandler = "/shop" bind Method.GET to { request: Request ->
    with(authLens(request)) {
        getShop().flatMap {
            getTemplateEngine().renderTemplate("Shop.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, IUserRepository, IPetRepository)
internal fun `POST shop|{id}|buy`(): RoutingHttpHandler = "/shop/{id}/buy" bind Method.POST to { request: Request ->
    with(authLens(request)) {
        val petId = UUID.fromString(request.path("id"))
        buyPet(petId).flatMap {
            getTemplateEngine().renderTemplate("Shop.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, IUserRepository, IPetRepository)
internal fun `POST shop|{id}|select`(): RoutingHttpHandler =
    "/shop/{id}/select" bind Method.POST to { request: Request ->
        with(authLens(request)) {
            val petId = UUID.fromString(request.path("id"))
            selectPet(petId).map { "" }.fold(::respondError, ::respondSuccess).header("HX-Location", "/")
        }
    }

@Serializable
data class CreateTask(
    @SerialName("taskInput")
    val name: String,
    @SerialName("descriptionInput")
    val description: String,
    @SerialName("priorityInput")
    val priority: Priority,
    @SerialName("deadlineDateInput")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    @SerialName("deadlineTimeInput")
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    @SerialName("isCompletedInput")
    val isCompleted: Boolean,
)

context(Logger, ITaskRepository)
internal fun `POST tasks|create`(): RoutingHttpHandler = "/tasks/create" bind Method.POST to { request: Request ->
    with(authLens(request)) {
        val createTaskLens = Body.auto<CreateTask>().toLens()
        val createTaskData = createTaskLens(request)
        create(
            TaskData(
                name = createTaskData.name,
                description = createTaskData.description,
                priority = createTaskData.priority,
                deadline = LocalDateTime.of(createTaskData.date, createTaskData.time),
                isCompleted = createTaskData.isCompleted,
            )
        ).flatMap { intTemplateEngine.renderTemplate("Task.kte", it) }
            .fold(::respondError, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `DELETE tasks|{id}`(): RoutingHttpHandler = "/tasks/{id}" bind Method.DELETE to { request: Request ->
    with(authLens(request)) {
        val taskId = UUID.fromString(request.path("id"))
        deleteTaskDomain(taskId).flatMap { intTemplateEngine.renderTemplate("Tasks.kte", it) }
            .fold(::respondError, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `POST tasks|{id}|complete`(): RoutingHttpHandler =
    "/tasks/{id}/complete" bind Method.POST to { request: Request ->
        with(authLens(request)) {
            val taskId = UUID.fromString(request.path("id"))
            completeTaskDomain(taskId).flatMap { intTemplateEngine.renderTemplate("Tasks.kte", it) }
                .fold(::respondError, ::respondSuccess)
        }
    }

context(Logger, IMultiRepository)
fun getRoutes(): RoutingHttpHandler =
    routes(
        `GET sign-up`(),
        `POST sign-up`(),
        `GET sign-in`(),
        `POST sign-in`(),
        authFilter().then(
            routes(
                `GET |`(),
                `GET shop`(),
                `POST shop|{id}|buy`(),
                `POST shop|{id}|select`(),

                `GET tasks`(),
                `GET tasks|{id}`(),
                "/task" bind Method.GET to ::placeholder,
                `POST tasks|create`(),
                `DELETE tasks|{id}`(),
                `POST tasks|{id}|complete`(),
                "/task/update" bind Method.POST to ::placeholder,
                "/task/cancel" bind Method.GET to ::placeholder,
            )
        )

    )