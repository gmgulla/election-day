package it.gmgulla.electionDay.shared.model.entities

import it.gmgulla.electionDay.shared.model.sqldelight.Database
import it.gmgulla.electionDay.shared.model.sqldelight.Mapper

data class Party(

    val id: Int = Int.MIN_VALUE,

    val name: String
) {
    val candidates: List<Candidate>
        get() = Database.getInstance().candidateQueries
            .selectCandidatesByParty(this.id, Mapper::mapCandidate).executeAsList()


}

val Party.isPersisted: Boolean
    get() = this.id >= 0
