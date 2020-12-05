package it.gmgulla.electionDay.shared.db.data

import kotlin.Int
import kotlin.String

data class ElectionEntity(
  val id: Int,
  val region: String,
  val office: String,
  val year: String
) {
  override fun toString(): String = """
  |ElectionEntity [
  |  id: $id
  |  region: $region
  |  office: $office
  |  year: $year
  |]
  """.trimMargin()
}
