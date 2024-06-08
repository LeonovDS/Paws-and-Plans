import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.net.URL

fun main() {
    val oldLoader = ResourceLoader.Directory("frontend")
    val loader : ResourceLoader  = object : ResourceLoader { override fun load(dir: String): URL? {
        return oldLoader.load(dir)
    }
    }

    DebuggingFilters.PrintRequestAndResponse().then(
        routes(
            "api" bind { r ->
                print(r); Response(Status.OK)},
            "" bind static(ResourceLoader.Directory("frontend")),
            )
    ).asServer(config = Jetty(2020)).start()
}
