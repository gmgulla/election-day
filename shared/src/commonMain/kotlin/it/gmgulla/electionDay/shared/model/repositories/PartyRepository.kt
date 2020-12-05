package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Party
import it.gmgulla.electionDay.shared.model.sqldelight.Database
import it.gmgulla.electionDay.shared.model.sqldelight.Mapper

class PartyRepository : Repository<Party> {

    private val partyInquirer = Database.getInstance().partyQueries

    override fun add(entity: Party) {
        try {
            partyInquirer.transaction {
                partyInquirer.insertParty(entity.name)
            }
        } catch (sqliteEx: Throwable) {
            throwDueToExistingUniqueValue(entity.name)
        }
    }

    private fun throwDueToExistingUniqueValue(name: String) {
        val existingParty = getByName(name)
        throw ExistingUniqueValueException(existingParty)
    }

    override fun getAll(): List<Party> {
        return partyInquirer.transactionWithResult {
            partyInquirer.selectAllParties(Mapper::mapParty).executeAsList()
        }
    }

    fun getByName(name: String): Party {
        val party = partyInquirer.transactionWithResult<Party?> {
            partyInquirer.selectPartyByName(name, Mapper::mapParty).executeAsOneOrNull()
        }
        return party ?: throw EntityNotFoundException(Party::class.simpleName!!, Party::name.name, name)
    }

    override fun getById(id: Int): Party {
        val party = partyInquirer.transactionWithResult<Party?> {
            partyInquirer.selectPartyById(id, Mapper::mapParty).executeAsOneOrNull()
        }
        return party ?: throw EntityNotFoundException(Party::class.simpleName!!, Party::id.name,id)
    }

    override fun update(entity: Party) {
        partyInquirer.transaction {
            partyInquirer.updateParty(entity.name, entity.id)
        }

    }

    override fun delete(entity: Party) {
        partyInquirer.transaction {
            partyInquirer.deletePartyById(entity.id)
        }
    }

    override fun deleteAll() {
        partyInquirer.transaction {
            partyInquirer.deleteAllParties()
        }
    }

}