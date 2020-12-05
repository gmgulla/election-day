package it.gmgulla.electionDay.shared.db

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import it.gmgulla.electionDay.shared.db.data.CandidateQueries
import it.gmgulla.electionDay.shared.db.data.CandidateSeatQueries
import it.gmgulla.electionDay.shared.db.data.ElectionQueries
import it.gmgulla.electionDay.shared.db.data.PartyQueries
import it.gmgulla.electionDay.shared.db.data.SeatQueries
import it.gmgulla.electionDay.shared.db.shared.newInstance
import it.gmgulla.electionDay.shared.db.shared.schema

interface AppDatabase : Transacter {
  val candidateQueries: CandidateQueries

  val candidateSeatQueries: CandidateSeatQueries

  val electionQueries: ElectionQueries

  val partyQueries: PartyQueries

  val seatQueries: SeatQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = AppDatabase::class.schema

    operator fun invoke(driver: SqlDriver): AppDatabase = AppDatabase::class.newInstance(driver)}
}
