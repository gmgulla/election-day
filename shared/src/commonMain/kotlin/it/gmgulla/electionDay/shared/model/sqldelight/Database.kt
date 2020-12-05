package it.gmgulla.electionDay.shared.model.sqldelight

import it.gmgulla.electionDay.shared.db.AppDatabase

internal object Database {

    private var database: AppDatabase? = null

    internal fun getInstance(): AppDatabase {
        if (database == null) {
            database = AppDatabase(DatabaseDriverFactory.createDriver())
        }
        return database!!
    }

}