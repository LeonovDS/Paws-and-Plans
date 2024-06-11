import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

private const val PORT = 2020

fun main() {
    val logger = org.slf4j.LoggerFactory.getLogger("Main")
    val db = initDB()
    val filter: Filter = Filter { handler: HttpHandler ->
        { request: Request ->
            logger.info("${request.method} ${request.uri}")
            handler(request)
        }
    }

    filter.then(
                    routes(
                            templateRoutes,
                            staticRoute,
                    )
            )
            .asServer(config = Jetty(PORT))
            .start()
}
