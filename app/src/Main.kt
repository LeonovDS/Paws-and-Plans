import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

private const val PORT = 2020

fun main() {
    routes(
        "index.html" bind ::indexRoute,
        staticRoute,
    ).asServer(config = Jetty(PORT)).start()
}
