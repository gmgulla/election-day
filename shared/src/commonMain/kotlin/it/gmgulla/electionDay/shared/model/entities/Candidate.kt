package it.gmgulla.electionDay.shared.model.entities

data class Candidate(

    val id: Int = Int.MIN_VALUE,

    val name: String,

    val party: Party
){
    val seatsParticipating: List<CandidateSeatResult>
        get() = TODO("Get by CandidateQueries object")
}

val Candidate.isPersisted: Boolean
    get() = this.id >= 0