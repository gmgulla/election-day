package it.gmgulla.electionDay.shared.db.data

import kotlin.Int
import kotlin.Long
import kotlin.String

data class CandidateSeatEntity(
  val id: Int,
  val candidate: Int,
  val seat: Int,
  val votes: Long
) {
  override fun toString(): String = """
  |CandidateSeatEntity [
  |  id: $id
  |  candidate: $candidate
  |  seat: $seat
  |  votes: $votes
  |]
  """.trimMargin()
}
