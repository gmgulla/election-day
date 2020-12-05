package it.gmgulla.electionDay.shared.model.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import it.gmgulla.electionDay.shared.db.AppDatabase

internal actual object DatabaseDriverFactory {

   internal actual fun createDriver(): SqlDriver {
       val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
       driver.execute(null, "PRAGMA foreign_keys=ON", 0)
       AppDatabase.Schema.create(driver)
       return driver
    }
}