import org.http4k.routing.ResourceLoader
import org.http4k.routing.static

val staticRoute =
    static(ResourceLoader.Classpath("static", muteWarning = true))
