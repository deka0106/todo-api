package jp.ac.titech.itsp.todo.dao

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import java.util.*

object Events : IntIdTable() {
    val title = varchar("title", 100)
    val memo = varchar("memo", 500)
    val deadline = datetime("deadline")
}

class EventEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventEntity>(Events)

    var title by Events.title
    var memo by Events.memo
    var deadline by Events.deadline

    fun toData() = EventData(id.value, title, memo, deadline.toDate())
}

data class EventData(
    var id: Int,
    var title: String,
    var memo: String,
    var deadline: Date
)
