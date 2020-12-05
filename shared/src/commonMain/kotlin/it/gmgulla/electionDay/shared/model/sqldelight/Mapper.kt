package it.gmgulla.electionDay.shared.model.sqldelight

import it.gmgulla.electionDay.shared.model.entities.Candidate
import it.gmgulla.electionDay.shared.model.entities.Party
import it.gmgulla.electionDay.shared.model.repositories.PartyRepository

internal object Mapper {

    internal fun mapParty(id: Int, name: String): Party {
        return Party(id, name)
    }

    internal fun mapCandidate(id: Int, name: String, partyId: Int): Candidate {
        val partyRepo = PartyRepository()
        val party = partyRepo.getById(partyId)
        return Candidate(id, name, party)

    }
}