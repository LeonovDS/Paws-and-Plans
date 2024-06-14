import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import data.Id
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import gg.jte.resolve.ResourceCodeResolver
import model.Model
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.Header
import org.slf4j.Logger
import repository.IMultiRepository
import java.util.*

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

internal val authLens =
    Header
        .map(
            { Id(UUID.fromString(it)) },
            { it.id.toString() }
        ).required("id")

context(Logger, IMultiRepository)
internal fun makeUserlessRoute(
    domain: context(Logger, IMultiRepository)(Request) -> Either<DomainError, Model>,
): HttpHandler = { request ->
    domain(this@Logger, this@IMultiRepository, request)
        .flatMap { getTemplateEngine().renderTemplate(it.template, it) }
        .fold(::respondError, ::respondSuccess)
}

context(Logger, IMultiRepository)
internal fun makeRoute(
    domain: context(Id, Logger, IMultiRepository)(Request) -> Either<DomainError, Model>,
): HttpHandler = makeUserlessRoute {
    domain(authLens(it), this@Logger, this@IMultiRepository, it)
}


