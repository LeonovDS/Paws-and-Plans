package routes

import LocalDateSerializer
import LocalTimeSerializer
import completeTaskDomain
import create
import data.Priority
import data.TaskData
import deleteTaskDomain
import getTasks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import makeRoute
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.slf4j.Logger
import repository.IMultiRepository
import updateTaskDomain
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


context(Logger, IMultiRepository)
internal fun `GET tasks`(): RoutingHttpHandler = "/tasks" bind Method.GET to
        makeRoute { _: Request -> getTasks() }

context(Logger, IMultiRepository)
internal fun `GET tasks|{id}`(): RoutingHttpHandler = "/tasks/{id}" bind Method.GET to makeRoute {
    getTasks(UUID.fromString(it.path("id")))
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

context(Logger, IMultiRepository)
internal fun `POST tasks`(): RoutingHttpHandler = "/tasks" bind Method.POST to
        makeRoute {
            val form = Body.auto<TaskForm>().toLens()(it)
            create(
                TaskData(
                    name = form.name,
                    description = form.description,
                    priority = form.priority,
                    deadline = LocalDateTime.of(form.date, form.time),
                    isCompleted = form.isCompleted,
                )
            )
        }


context(Logger, IMultiRepository)
internal fun `DELETE tasks|{id}`(): RoutingHttpHandler = "/tasks/{id}" bind Method.DELETE to
        makeRoute {
            deleteTaskDomain(UUID.fromString(it.path("id")))
        }

context(Logger, IMultiRepository)
internal fun `POST tasks|{id}|complete`(): RoutingHttpHandler =
    "/tasks/{id}/complete" bind Method.POST to
            makeRoute {
                completeTaskDomain(UUID.fromString(it.path("id")))
            }


context(Logger, IMultiRepository)
internal fun `POST tasks|{id}`(): RoutingHttpHandler =
    "/tasks/{id}/complete" bind Method.POST to makeRoute {
        val form = Body.auto<TaskForm>().toLens()(it)
        val taskId = UUID.fromString(it.path("id"))
        val taskData = TaskData(
            id = taskId,
            name = form.name,
            description = form.description,
            priority = form.priority,
            deadline = LocalDateTime.of(form.date, form.time),
            isCompleted = form.isCompleted,
        )
        updateTaskDomain(taskData)
    }
