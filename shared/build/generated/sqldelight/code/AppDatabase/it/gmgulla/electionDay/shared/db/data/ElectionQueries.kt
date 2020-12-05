package it.gmgulla.electionDay.shared.db.data

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.String

interface ElectionQueries : Transacter {
  fun <T : Any> selectAllElections(mapper: (
    id: Int,
    region: String,
    office: String,
    year: String
  ) -> T): Query<T>

  fun selectAllElections(): Query<ElectionEntity>

  fun <T : Any> selectElectionByValues(
    region: String,
    office: String,
    year: String,
    mapper: (
      id: Int,
      region: String,
      office: String,
      year: String
    ) -> T
  ): Query<T>

  fun selectElectionByValues(
    region: String,
    office: String,
    year: String
  ): Query<ElectionEntity>

  fun insertElection(
    region: String,
    office: String,
    year: String
  )

  fun deleteAllElections()

  fun deleteElectionById(id: Int)
}
