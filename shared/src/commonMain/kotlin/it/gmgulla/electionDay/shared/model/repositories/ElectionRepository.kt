package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Election
import it.gmgulla.electionDay.shared.model.sqldelight.Database
import it.gmgulla.electionDay.shared.model.sqldelight.Mapper

class ElectionRepository : Repository<Election> {

    private val electionInquirer = Database.getInstance().electionQueries

    override fun add(entity: Election) {
        electionInquirer.transaction {
            electionInquirer.insertElection(entity.region, entity.office, entity.year.toString())
        }
    }

    override fun getById(id: Int): Election {
        TODO("Not yet implemented")
    }

    fun getByValues(region: String, office: String, year: Int): Election {
        return electionInquirer.transactionWithResult {
            val election = electionInquirer.selectElectionByValues(
                region, office, year.toString(),
                Mapper::mapElection
            ).executeAsOneOrNull()
            election ?: throw EntityNotFoundException(Election::class.simpleName!!)
        }
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
