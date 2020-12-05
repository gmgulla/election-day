package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Election
import it.gmgulla.electionDay.shared.model.sqldelight.Database

class ElectionRepository : Repository<Election> {

    private val electionInquirer = Database.getInstance().electionQueries

    override fun add(entity: Election) {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): Election {
        TODO("Not yet implemented")
    }

    fun getByValues(region: String, office: String, year: Int): Election {

    }

    override fun getAll(): List<Election> {
        TODO("Not yet implemented")
    }

    override fun update(entity: Election) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Election) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }
}
