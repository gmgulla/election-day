package it.gmgulla.electionDay.shared.db.data

import kotlin.Int
import kotlin.String

data class CandidateEntity(
  val id: Int,
  val name: String,
  val party: Int
) {
  override fun toString(): String = """
  |CandidateEntity [
  |  id: $id
  |  name: $name
  |  party: $party
  |]
  """.trimMargin()
}
