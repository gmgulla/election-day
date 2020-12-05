package it.gmgulla.electionDay.shared.db.data

import kotlin.Int
import kotlin.String

data class PartyEntity(
  val id: Int,
  val name: String
) {
  override fun toString(): String = """
  |PartyEntity [
  |  id: $id
  |  name: $name
  |]
  """.trimMargin()
}
