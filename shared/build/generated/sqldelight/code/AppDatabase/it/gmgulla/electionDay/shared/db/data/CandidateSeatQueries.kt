package it.gmgulla.electionDay.shared.db.data

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long

interface CandidateSeatQueries : Transacter {
  fun <T : Any> selectAllCandidateSeats(mapper: (
    id: Int,
    candidate: Int,
    seat: Int,
    votes: Long
  ) -> T): Query<T>

  fun selectAllCandidateSeats(): Query<CandidateSeatEntity>

  fun <T : Any> selectCandidateSeatsByCandidate(candidate: Int, mapper: (
    id: Int,
    candidate: Int,
    seat: Int,
    votes: Long
  ) -> T): Query<T>

  fun selectCandidateSeatsByCandidate(candidate: Int): Query<CandidateSeatEntity>

  fun <T : Any> selectCandidateSeatsBySeat(seat: Int, mapper: (
    id: Int,
    candidate: Int,
    seat: Int,
    votes: Long
  ) -> T): Query<T>

  fun selectCandidateSeatsBySeat(seat: Int): Query<CandidateSeatEntity>

  fun insertCandidateSeat(candidate: Int, seat: Int)

  fun insertCandidateSeatWithVotes(
    candidate: Int,
    seat: Int,
    votes: Long
  )

  fun upadeCandidateSeat(votes: Long, id: Int)

  fun deleteAllCandidateSeats()

  fun deleteCandidateSeatById(id: Int)
}
