package it.gmgulla.electionDay.shared.db.data

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.String

interface CandidateQueries : Transacter {
  fun <T : Any> selectAllCandidate(mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T>

  fun selectAllCandidate(): Query<CandidateEntity>

  fun <T : Any> selectCandidateById(id: Int, mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T>

  fun selectCandidateById(id: Int): Query<CandidateEntity>

  fun <T : Any> selectCandidatesByName(name: String, mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T>

  fun selectCandidatesByName(name: String): Query<CandidateEntity>

  fun <T : Any> selectCandidatesByParty(party: Int, mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T>

  fun selectCandidatesByParty(party: Int): Query<CandidateEntity>

  fun insertCandidate(name: String, party: Int)

  fun updateCandidate(
    name: String,
    party: Int,
    id: Int
  )

  fun deleteAllCandidates()

  fun deleteCandidateById(id: Int)
}
