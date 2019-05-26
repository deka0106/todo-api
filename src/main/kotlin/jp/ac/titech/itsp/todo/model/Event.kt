package jp.ac.titech.itsp.todo.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Events : IntIdTable() {
    val title = varchar("title", 100)
    val memo = varchar("memo", 500)
    val deadline = date("deadline")
}

class Event(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Event>(Events)

    var title by Events.title
    var memo by Events.memo
    var deadline by Events.deadline
}

