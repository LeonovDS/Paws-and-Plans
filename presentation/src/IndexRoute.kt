import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import gg.jte.resolve.ResourceCodeResolver
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import presentation.IndexModel

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

@Suppress("UNUSED_PARAMETER")
fun indexRoute(request: Request): Response =
    getTemplateEngine().renderTemplate("index.kte", IndexModel("World")).fold({
        Response(Status.INTERNAL_SERVER_ERROR).body(it.toString())
    }) {
        Response(Status.OK).body(it)
    }
