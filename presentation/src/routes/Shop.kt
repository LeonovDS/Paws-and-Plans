package routes

import arrow.core.flatMap
import authLens
import buyPet
import getShop
import getTemplateEngine
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.slf4j.Logger
import renderTemplate
import repository.IPetRepository
import repository.IUserRepository
import respondError
import respondSuccess
import selectPet
import java.util.*

context(Logger, IUserRepository, IPetRepository)
internal fun `GET shop`(): RoutingHttpHandler = "/shop" bind Method.GET to { request: Request ->
    with(authLens(request)) {
        getShop().flatMap {
            getTemplateEngine().renderTemplate("Shop.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, IUserRepository, IPetRepository)
internal fun `POST shop|{id}|buy`(): RoutingHttpHandler = "/shop/{id}/buy" bind Method.POST to { request: Request ->
    with(authLens(request)) {
        val petId = UUID.fromString(request.path("id"))
        buyPet(petId).flatMap {
            getTemplateEngine().renderTemplate("Shop.kte", it)
        }.fold(::respondError, ::respondSuccess)
    }
}

context(Logger, IUserRepository, IPetRepository)
internal fun `POST shop|{id}|select`(): RoutingHttpHandler =
    "/shop/{id}/select" bind Method.POST to { request: Request ->
        with(authLens(request)) {
            val petId = UUID.fromString(request.path("id"))
            selectPet(petId).map { "" }.fold(::respondError, ::respondSuccess).header("HX-Location", "/")
        }
    }
