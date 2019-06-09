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
import jp.ac.titech.itsp.todo.dao.EventData
import jp.ac.titech.itsp.todo.dao.EventEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

@KtorExperimentalLocationsAPI
fun Route.event() {

    get<Event> {
        val events = transaction { EventEntity.all().map { it.toData() } }
        call.respond(HttpStatusCode.OK, GetAllEventsResponse(events))
    }

    post<Event> {
        val request = try {
            call.receive<PostEventRequest>()
        } catch (e: InvalidFormatException) {
            call.respond(
                HttpStatusCode.BadRequest,
                PostEventResponse("failure", "invalid date format")
            )
            return@post
        }
        val event = transaction {
            EventEntity.new {
                title = request.title
                memo = request.memo
                deadline = DateTime(request.deadline)
            }
        }
        call.respond(
            HttpStatusCode.OK,
            PostEventResponse("success", "registered", event.id.value)
        )
    }

    get<Event.Id> {
        val event = transaction { EventEntity.findById(it.id)?.toData() }
        if (event == null) call.respond(HttpStatusCode.NotFound)
        else call.respond(HttpStatusCode.OK, event)
    }

}

@KtorExperimentalLocationsAPI
@Location("/event")
internal class Event {
    @Location("/{id}")
    internal data class Id(val id: Int)
}

data class GetAllEventsResponse(
    val events: List<EventData>
)

data class PostEventRequest(
    var title: String,
    var memo: String,
    var deadline: Date
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostEventResponse(
    var status: String,
    var message: String,
    var id: Int? = null
)