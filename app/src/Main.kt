import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.then
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

private const val PORT = 8080

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: PORT
    val logger = org.slf4j.LoggerFactory.getLogger("pnp")
    val loggingFilter = Filter { handler: HttpHandler ->
        { request: Request ->
            val response = handler(request)
            logger.info("${request.method} ${request.uri} > ${response.status}")
            response
        }
    }
    with(logger) {
        with(DbRepository(initDB())) {
            loggingFilter.then(
                routes(
                    getRoutes(),
                    staticRoute,
                )
            ).asServer(config = Jetty(port)).start()
        }
    }
}
