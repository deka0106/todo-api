package jp.ac.titech.itsp.todo.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jp.ac.titech.itsp.todo.model.Events
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object Database {

    fun init() {
        Database.connect(hikariInMemory())
        transaction {
            addLogger(StdOutSqlLogger)
            create(Events)
        }
    }

    private fun hikariInMemory(): HikariDataSource =
        HikariDataSource(HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test;DATABASE_TO_UPPER=false"
            maximumPoolSize = 5
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        })

}