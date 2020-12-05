package it.gmgulla.electionDay.shared.db.data

import kotlin.Int
import kotlin.Long
import kotlin.String

data class SeatEntity(
  val id: Int,
  val election: Int,
  val name: String,
  val votesRecorded: Long
) {
  override fun toString(): String = """
  |SeatEntity [
  |  id: $id
  |  election: $election
  |  name: $name
  |  votesRecorded: $votesRecorded
  |]
  """.trimMargin()
}
