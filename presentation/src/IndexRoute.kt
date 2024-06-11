import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import domain.getIndexModel
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import gg.jte.resolve.ResourceCodeResolver
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.boolean
import org.http4k.lens.localDate
import org.http4k.lens.localTime
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.LocalDateTime

fun getTemplateEngine(): TemplateEngine {
    val resolver = ResourceCodeResolver("template")
    val engine = TemplateEngine.create(resolver, ContentType.Html)
    return engine
}

fun <T> TemplateEngine.renderTemplate(template: String, data: T): Either<DomainError, String> =
    catch({
        val output = StringOutput()
        render(template, data, output)
        output.toString().right()
    }) { throwable ->
        TemplateRenderFailed(throwable).left()
    }

val petRepo = object : PetRepository {
    override fun getPet(): Either<DomainError, PetData> = PetData(
        name = "Пушок",
        coins = 10,
        happyness = 15,
    ).right()
}

val taskRepo = object : TaskRepository {
    override fun getTasks(): Either<DomainError, List<TaskData>> = listOf(
        TaskData(
            name = "Сделать ДЗ",
            description = "Сделать ДЗ",
            priority = Priority.HIGH,
            deadline = LocalDateTime.now(),
            isCompleted = false,
        ),
    ).right()
}
internal val intTemplateEngine = getTemplateEngine()

val tasksModel = TasksModel(
    tasks = listOf(
        TaskData(
            name = "Сделать ДЗ",
            description = "Сделать ДЗ",
            priority = Priority.HIGH,
            deadline = LocalDateTime.now(),
            isCompleted = false,
        ),
        TaskData(
            name = "Сделать ДЗ",
            description = "Сделать ДЗ",
            priority = Priority.LOW,
            deadline = LocalDateTime.now(),
            isCompleted = false,
        ),
        TaskData(
            name = "Сделать ДЗ",
            description = "Сделать ДЗ",
            priority = Priority.MEDIUM,
            deadline = LocalDateTime.now(),
            isCompleted = false,
        ),
    ),
)


@Suppress("UNUSED_PARAMETER")
internal fun indexRoute(request: Request): Response =
    getIndexModel(petRepo, taskRepo).flatMap { intTemplateEngine.renderTemplate("Index.kte", it) }
        .fold({
            Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
        }) {
            Response(Status.OK).body(it)
        }

@Suppress("UNUSED_PARAMETER")
internal fun shopRoute(request: Request): Response =
    intTemplateEngine.renderTemplate("Shop.kte", Unit)
        .fold({
            Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
        }) {
            Response(Status.OK).body(it)
        }

@Suppress("UNUSED_PARAMETER")
internal fun settingsRoute(request: Request): Response =
    intTemplateEngine.renderTemplate("Settings.kte", Unit)
        .fold({
            Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
        }) {
            Response(Status.OK).body(it)
        }

@Suppress("UNUSED_PARAMETER")
internal fun tasksRoute(request: Request): Response =
    intTemplateEngine.renderTemplate("Tasks.kte", tasksModel)
        .fold({
            Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
        }) {
            Response(Status.OK).body(it)
        }

private val taskNameLens = FormField.required("taskInput")
private val taskDescriptionLens = FormField.required("descriptionInput")
private val priorityLens = FormField.required("priorityInput")
private val deadlineDateLens = FormField.localDate().optional("deadlineDateInput")
private val deadlineTimeLens = FormField.localTime().optional("deadlineTimeInput")
private val isCompletedLens = FormField.boolean().required("isCompletedInput")

val formLens = Body.webForm(
    Validator.Strict,
    taskNameLens,
    taskDescriptionLens,
    priorityLens,
    deadlineDateLens,
    deadlineTimeLens,
    isCompletedLens
).toLens()

private fun taskLens(request: Request) : TaskData {
    val form = formLens(request)
    val name = taskNameLens(form)
    val description = taskDescriptionLens(form)
    val priority = priorityLens(form)
    val deadlineDate = deadlineDateLens(form)
    val deadlineTime = deadlineTimeLens(form)
    val isCompleted = isCompletedLens(form)
    return TaskData(
        name = name,
        description = description,
        priority = Priority.valueOf(priority.uppercase()),
        deadline = LocalDateTime.of(deadlineDate, deadlineTime),
        isCompleted = isCompleted
    ).also {
        println(it)
    }
}

internal fun addTaskRoute(request: Request): Response =
    intTemplateEngine.renderTemplate("Task.kte", taskLens(request))
        .fold({
            Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
        }) {
            Response(Status.OK).body(it)
        }


val templateRoutes = routes(
    "/" bind ::indexRoute,
    "/shop" bind ::shopRoute,
    "/settings" bind ::settingsRoute,
    "/tasks" bind ::tasksRoute,
    "/add-task" bind ::addTaskRoute,
)