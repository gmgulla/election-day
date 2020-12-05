package it.gmgulla.electionDay.shared.model.entities

data class Seat(

    val id: Int = Int.MIN_VALUE,

    val election: Election,

    val name: String,

    val votesRecorded: Long,

    val candidatesVotes: MutableList<CandidateSeatResult>? = null
)