package it.gmgulla.electionDay.shared.db.data

import kotlin.Int
import kotlin.String

data class SelectPartyByIdWithCandidates(
  val id: Int,
  val name: String,
  val id_: Int,
  val name_: String,
  val party: Int
) {
  override fun toString(): String = """
  |SelectPartyByIdWithCandidates [
  |  id: $id
  |  name: $name
  |  id_: $id_
  |  name_: $name_
  |  party: $party
  |]
  """.trimMargin()
}