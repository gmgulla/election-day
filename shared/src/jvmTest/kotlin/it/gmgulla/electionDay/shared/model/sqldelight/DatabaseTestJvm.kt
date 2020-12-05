package it.gmgulla.electionDay.shared.model.sqldelight

import org.junit.Test
import kotlin.test.assertNotNull

class DatabaseTestJvm {

    @Test fun correctDataabseCreation() {
        val result = Database.getInstance()
        assertNotNull(result)
    }
}