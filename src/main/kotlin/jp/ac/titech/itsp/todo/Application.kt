package jp.ac.titech.itsp.todo

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import jp.ac.titech.itsp.todo.config.Database
import jp.ac.titech.itsp.todo.router.event
import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>) = EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.main() {
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            setTimeZone(TimeZone.getDefault())
        }
    }
    install(Locations)

    Database.init()

    routing {
        route("/api") {
            route("/v1") {
                event()
            }
        }
    }

}
