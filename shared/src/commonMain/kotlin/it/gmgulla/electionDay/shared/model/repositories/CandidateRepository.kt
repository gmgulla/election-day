package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Candidate
import it.gmgulla.electionDay.shared.model.sqldelight.Database
import it.gmgulla.electionDay.shared.model.sqldelight.Mapper

class CandidateRepository : Repository<Candidate> {

    private val candidateInquirer = Database.getInstance().candidateQueries

    override fun add(entity: Candidate) {
        candidateInquirer.transaction {
            candidateInquirer.insertCandidate(entity.name, entity.party.id)
        }
    }

    override fun getById(id: Int): Candidate {
        val candidate = candidateInquirer.transactionWithResult<Candidate?> {
            candidateInquirer.selectCandidateById(id, Mapper::mapCandidate).executeAsOneOrNull()
        }
        return candidate ?: throw EntityNotFoundException(Candidate::class.simpleName!!, Candidate::id.name, id)
    }

    fun getByName(name: String): List<Candidate> {
        return candidateInquirer.transactionWithResult {
            candidateInquirer.selectCandidatesByName(name, Mapper::mapCandidate).executeAsList()
        }
    }

    override fun getAll(): List<Candidate> {
        return candidateInquirer.transactionWithResult {
            candidateInquirer.selectAllCandidate(Mapper::mapCandidate).executeAsList()
        }
    }
    
    override fun update(entity: Candidate) {
        candidateInquirer.transaction {
            candidateInquirer.updateCandidate(entity.name, entity.party.id, entity.id)
        }
    }

    override fun delete(entity: Candidate) {
        candidateInquirer.transaction {
            candidateInquirer.deleteCandidateById(entity.id)
        }
    }

    override fun deleteAll() {
        candidateInquirer.transaction {
            candidateInquirer.deleteAllCandidates()
        }
    }

}