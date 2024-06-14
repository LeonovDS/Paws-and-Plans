package routes

import getIndex
import makeRoute
import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.slf4j.Logger
import repository.IMultiRepository

context(Logger, IMultiRepository)
internal fun `GET |`(): RoutingHttpHandler = "/" bind Method.GET to
        makeRoute { _ -> getIndex() }