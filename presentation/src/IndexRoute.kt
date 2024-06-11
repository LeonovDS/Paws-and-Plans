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
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
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

@Suppress("UNUSED_PARAMETER")
fun indexRoute(request: Request): Response =
    getIndexModel(petRepo, taskRepo).flatMap { getTemplateEngine().renderTemplate("index.kte", it) }
        .fold({
            Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
        }) {
            Response(Status.OK).body(it)
        }
