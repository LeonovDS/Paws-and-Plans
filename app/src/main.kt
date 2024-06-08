import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.asServer
import org.http4k.server.Jetty
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

fun main() {
    routes(
        "/api" bind { _ -> Response(Status.OK) },
        "/" bind static(ResourceLoader.Directory(Paths.get("frontend").absolutePathString()))
    ).asServer(config = Jetty(8080)).start()
}
