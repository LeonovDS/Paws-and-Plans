import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import org.slf4j.Logger
import repository.IMultiRepository
import routes.*

context(Logger, IMultiRepository)
fun getRoutes(): RoutingHttpHandler =
    routes(
        `GET sign-up`(),
        `POST sign-up`(),
        `GET sign-in`(),
        `POST sign-in`(),
        authFilter().then(
            routes(
                `GET |`(),
                `GET shop`(),
                `POST shop|{id}|buy`(),
                `POST shop|{id}|select`(),

                `GET tasks`(),
                `POST tasks`(),
                `GET tasks|{id}`(),
                `POST tasks|{id}`(),
                `DELETE tasks|{id}`(),
                `POST tasks|{id}|complete`(),
            )
        )
    )