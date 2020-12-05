package it.gmgulla.electionDay.shared.model.sqldelight

import com.squareup.sqldelight.db.SqlDriver

internal expect object DatabaseDriverFactory {

    internal fun createDriver(): SqlDriver
}
