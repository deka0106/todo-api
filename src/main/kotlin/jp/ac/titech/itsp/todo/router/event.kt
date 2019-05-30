package jp.ac.titech.itsp.todo.router

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import jp.ac.titech.itsp.todo.model.EventDao
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

@KtorExperimentalLocationsAPI
@Location("/event")
class Event {
    @Location("/{id}")
    data class Id(val id: Int)
}

data class NewEventRequest(
    var title: String,
    var memo: String,
    var deadline: Date
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NewEventResponse(
    var status: String,
    var message: String,
    var id: Int? = null
)

@KtorExperimentalLocationsAPI
fun Route.event() {

    get<Event> {
        val events = transaction {
            EventDao.all().map { it.toData() }
        }
        call.respond(HttpStatusCode.OK, events)
    }

    post<Event> {
        val request = try {
            call.receive<NewEventRequest>()
        } catch (e: InvalidFormatException) {
            call.respond(HttpStatusCode.BadRequest, NewEventResponse("failure", "invalid date format"))
            return@post
        }
        val event = transaction {
            EventDao.new {
                title = request.title
                memo = request.memo
                deadline = DateTime(request.deadline)
            }
        }
        call.respond(HttpStatusCode.OK, NewEventResponse("success", "registered", event.id.value))
    }

    get<Event.Id> {
        val event = transaction {
            EventDao.findById(it.id)?.toData()
        }
        if (event == null) call.respond(HttpStatusCode.NotFound)
        else call.respond(HttpStatusCode.OK, event)
    }

}
