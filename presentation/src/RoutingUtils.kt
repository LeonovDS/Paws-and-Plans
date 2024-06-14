import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import gg.jte.resolve.ResourceCodeResolver
import org.http4k.core.Response
import org.http4k.core.Status

internal fun getTemplateEngine(): TemplateEngine {
    val resolver = ResourceCodeResolver("template")
    val engine = TemplateEngine.create(resolver, ContentType.Html)
    return engine
}

internal fun <T> TemplateEngine.renderTemplate(template: String, data: T): Either<DomainError, String> = catch({
    val output = StringOutput()
    render(template, data, output)
    output.toString().right()
}) { throwable ->
    TemplateRenderFailed(throwable).left()
}

internal fun respondError(error: DomainError): Response = Response(Status.INTERNAL_SERVER_ERROR).body(error.toString())

internal fun respondSuccess(body: String): Response = Response(Status.OK).body(body)
