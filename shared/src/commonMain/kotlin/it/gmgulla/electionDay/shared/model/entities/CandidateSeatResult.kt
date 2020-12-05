package it.gmgulla.electionDay.shared.model.entities

data class CandidateSeatResult(

    val id: Int = Int.MIN_VALUE,

    val candidate: Candidate,

    val seat: Seat,

    val votes: Long
)