package routes

import arrow.core.flatMap
import authLens
import getIndex
import getTemplateEngine
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.slf4j.Logger
import renderTemplate
import repository.IPetRepository
import repository.ITaskRepository
import repository.IUserRepository
import respondSuccess

context(Logger, ITaskRepository, IUserRepository, IPetRepository)
internal fun `GET |`(): RoutingHttpHandler = "/" bind Method.GET to { request ->
    with(authLens(request)) {
        getIndex().flatMap { getTemplateEngine().renderTemplate("Index.kte", it) }.fold({
            Response(Status.TEMPORARY_REDIRECT).header("Location", "/sign-in")
        }, ::respondSuccess)
    }
}
