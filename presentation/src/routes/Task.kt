package routes

import LocalDateSerializer
import LocalTimeSerializer
import arrow.core.flatMap
import authLens
import completeTaskDomain
import create
import data.Priority
import data.TaskData
import deleteTaskDomain
import getTasks
import getTemplateEngine
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.slf4j.Logger
import renderTemplate
import repository.IPetRepository
import repository.ITaskRepository
import repository.IUserRepository
import respondError
import respondSuccess
import updateTaskDomain
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `GET tasks`(): RoutingHttpHandler = "/tasks" bind Method.GET to { request: Request ->
    with(authLens(request)) {
        getTasks().flatMap {
            getTemplateEngine().renderTemplate("Tasks.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `GET tasks|{id}`(): RoutingHttpHandler = "/tasks/{id}" bind Method.GET to { request: Request ->
    with(authLens(request)) {
        val taskId = UUID.fromString(request.path("id"))
        getTasks(taskId).flatMap {
            getTemplateEngine().renderTemplate("Tasks.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}


@Serializable
data class TaskForm(
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
internal fun `POST tasks`(): RoutingHttpHandler = "/tasks" bind Method.POST to { request: Request ->
    with(authLens(request)) {
        val taskFormLens = Body.auto<TaskForm>().toLens()
        val createTaskData = taskFormLens(request)
        create(
            TaskData(
                name = createTaskData.name,
                description = createTaskData.description,
                priority = createTaskData.priority,
                deadline = LocalDateTime.of(createTaskData.date, createTaskData.time),
                isCompleted = createTaskData.isCompleted,
            )
        ).flatMap { getTemplateEngine().renderTemplate("Task.kte", it) }
            .fold(::respondError, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `DELETE tasks|{id}`(): RoutingHttpHandler = "/tasks/{id}" bind Method.DELETE to { request: Request ->
    with(authLens(request)) {
        val taskId = UUID.fromString(request.path("id"))
        deleteTaskDomain(taskId).flatMap { getTemplateEngine().renderTemplate("Tasks.kte", it) }
            .fold(::respondError, ::respondSuccess)
    }
}

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `POST tasks|{id}|complete`(): RoutingHttpHandler =
    "/tasks/{id}/complete" bind Method.POST to { request: Request ->
        with(authLens(request)) {
            val taskId = UUID.fromString(request.path("id"))
            completeTaskDomain(taskId).flatMap { getTemplateEngine().renderTemplate("Tasks.kte", it) }
                .fold(::respondError, ::respondSuccess)
        }
    }


context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `POST tasks|{id}`(): RoutingHttpHandler =
    "/tasks/{id}/complete" bind Method.POST to { request: Request ->
        with(authLens(request)) {
            val taskId = UUID.fromString(request.path("id"))
            val taskFormLens = Body.auto<TaskForm>().toLens()
            val createTaskData = taskFormLens(request)
            val taskData = TaskData(
                id = taskId,
                name = createTaskData.name,
                description = createTaskData.description,
                priority = createTaskData.priority,
                deadline = LocalDateTime.of(createTaskData.date, createTaskData.time),
                isCompleted = createTaskData.isCompleted,
            )
            updateTaskDomain(taskData).flatMap { getTemplateEngine().renderTemplate("Tasks.kte", it) }
                .fold(::respondError, ::respondSuccess)
        }
    }
