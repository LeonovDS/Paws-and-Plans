package routes

import buyPet
import getShop
import makeRoute
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.slf4j.Logger
import repository.IMultiRepository
import selectPet
import java.util.*

context(Logger, IMultiRepository)
internal fun `GET shop`(): RoutingHttpHandler = "/shop" bind Method.GET to makeRoute { getShop() }

context(Logger, IMultiRepository)
internal fun `POST shop|{id}|buy`(): RoutingHttpHandler = "/shop/{id}/buy" bind Method.POST to
        makeRoute {
            buyPet(
                UUID.fromString(it.path("id"))
            )
        }

context(Logger, IMultiRepository)
internal fun `POST shop|{id}|select`(): RoutingHttpHandler =
    "/shop/{id}/select" bind Method.POST to makeRoute { request: Request ->
        selectPet(UUID.fromString(request.path("id")))
    }