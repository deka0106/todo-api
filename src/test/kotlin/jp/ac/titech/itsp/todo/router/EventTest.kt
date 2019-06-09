package jp.ac.titech.itsp.todo.router

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import jp.ac.titech.itsp.todo.main
import jp.ac.titech.itsp.todo.dao.EventEntity
import jp.ac.titech.itsp.todo.dao.EventData
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import kotlin.test.*

@KtorExperimentalLocationsAPI
class EventTest {

    @Test
    fun get() {
        withTestApplication({
            main()
        }) {
            transaction {
                repeat(10) {
                    EventEntity.new {
                        title = "title$it"
                        memo = "memo$it"
                        deadline = DateTime()
                    }
                }
            }
            handleRequest(HttpMethod.Get, "/api/v1/event").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val node = jacksonObjectMapper().readTree(response.content)
                    jacksonObjectMapper().convertValue<List<EventData>>(node["events"])
                } catch (e: Exception) {
                    fail("invalid response format")
                }
            }
        }
    }

    @Test
    fun post() {
        withTestApplication({ main() }) {
            handleRequest(HttpMethod.Post, "/api/v1/event") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"deadline": "2019-06-11T14:00:00+09:00", "title": "レポート提出", "memo": ""}""")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val node = jacksonObjectMapper().readTree(response.content)
                assertEquals("success", node["status"].asText())
                assertTrue(node.hasNonNull("id"))
            }
            handleRequest(HttpMethod.Post, "/api/v1/event") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"deadline": "2019-06-11", "title": "レポート提出", "memo": ""}""")
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val node = jacksonObjectMapper().readTree(response.content)
                assertEquals("failure", node["status"].asText())
                assertFalse(node.has("id"))
            }
        }
    }

    @Test
    fun getById() {
        withTestApplication({
            main()
        }) {
            transaction {
                EventEntity.new {
                    title = "title"
                    memo = "memo"
                    deadline = DateTime()
                }
            }
            handleRequest(HttpMethod.Get, "/api/v1/event/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    jacksonObjectMapper().readValue<EventData>(response.content ?: "")
                } catch (e: Exception) {
                    fail("invalid response format")
                }
            }
            handleRequest(HttpMethod.Get, "/api/v1/event/114514").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun deleteById() {
        withTestApplication({
            main()
        }) {
            transaction {
                EventEntity.new {
                    title = "title"
                    memo = "memo"
                    deadline = DateTime()
                }
            }
            handleRequest(HttpMethod.Delete, "/api/v1/event/1").apply {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
            assertNull(transaction { EventEntity.findById(1) })
        }
    }

}
