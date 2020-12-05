package it.gmgulla.electionDay.shared.db.data

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface SeatQueries : Transacter {
  fun <T : Any> selectAllSeats(mapper: (
    id: Int,
    election: Int,
    name: String,
    votesRecorded: Long
  ) -> T): Query<T>

  fun selectAllSeats(): Query<SeatEntity>

  fun <T : Any> selectSeatByValues(
    election: Int,
    name: String,
    mapper: (
      id: Int,
      election: Int,
      name: String,
      votesRecorded: Long
    ) -> T
  ): Query<T>

  fun selectSeatByValues(election: Int, name: String): Query<SeatEntity>

  fun insertSeat(election: Int, name: String)

  fun updateSeat(votesRecorded: Long, id: Int)

  fun delelteAllSeats()

  fun deleteSeatById(id: Int)
}
