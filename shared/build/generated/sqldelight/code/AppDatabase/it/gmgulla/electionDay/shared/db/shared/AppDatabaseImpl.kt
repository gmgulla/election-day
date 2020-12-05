package it.gmgulla.electionDay.shared.db.shared

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import it.gmgulla.electionDay.shared.db.AppDatabase
import it.gmgulla.electionDay.shared.db.data.CandidateEntity
import it.gmgulla.electionDay.shared.db.data.CandidateQueries
import it.gmgulla.electionDay.shared.db.data.CandidateSeatEntity
import it.gmgulla.electionDay.shared.db.data.CandidateSeatQueries
import it.gmgulla.electionDay.shared.db.data.ElectionEntity
import it.gmgulla.electionDay.shared.db.data.ElectionQueries
import it.gmgulla.electionDay.shared.db.data.PartyEntity
import it.gmgulla.electionDay.shared.db.data.PartyQueries
import it.gmgulla.electionDay.shared.db.data.SeatEntity
import it.gmgulla.electionDay.shared.db.data.SeatQueries
import it.gmgulla.electionDay.shared.db.data.SelectAllPartiesWithCandidate
import it.gmgulla.electionDay.shared.db.data.SelectPartyByIdWithCandidates
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

internal val KClass<AppDatabase>.schema: SqlDriver.Schema
  get() = AppDatabaseImpl.Schema

internal fun KClass<AppDatabase>.newInstance(driver: SqlDriver): AppDatabase =
    AppDatabaseImpl(driver)

private class AppDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), AppDatabase {
  override val candidateQueries: CandidateQueriesImpl = CandidateQueriesImpl(this, driver)

  override val candidateSeatQueries: CandidateSeatQueriesImpl = CandidateSeatQueriesImpl(this,
      driver)

  override val electionQueries: ElectionQueriesImpl = ElectionQueriesImpl(this, driver)

  override val partyQueries: PartyQueriesImpl = PartyQueriesImpl(this, driver)

  override val seatQueries: SeatQueriesImpl = SeatQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE ElectionEntity(
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    region TEXT NOT NULL,
          |    office TEXT NOT NULL,
          |    year TEXT NOT NULL,
          |    UNIQUE (region, office, year)
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE SeatEntity(
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    election INTEGER NOT NULL,
          |    name TEXT NOT NULL,
          |    votesRecorded INTEGER NOT NULL DEFAULT 0,
          |    FOREIGN KEY (election) REFERENCES ElectionEntity(id) ON DELETE CASCADE,
          |    UNIQUE (election, name)
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE CandidateSeatEntity(
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    candidate INTEGER NOT NULL,
          |    seat INTEGER NOT NULL,
          |    votes INTEGER NOT NULL DEFAULT 0,
          |    UNIQUE (candidate, seat),
          |    FOREIGN KEY (candidate) REFERENCES CandidateEntity(id) ON DELETE CASCADE,
          |    FOREIGN KEY (seat) REFERENCES SeatEntity(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE CandidateEntity (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    name TEXT NOT NULL,
          |    party INTEGER NOT NULL,
          |    FOREIGN KEY (party) REFERENCES PartyEntity(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE PartyEntity(
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    name TEXT NOT NULL UNIQUE
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class ElectionQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), ElectionQueries {
  internal val selectAllElections: MutableList<Query<*>> = copyOnWriteList()

  internal val selectElectionByValues: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAllElections(mapper: (
    id: Int,
    region: String,
    office: String,
    year: String
  ) -> T): Query<T> = Query(1541693462, selectAllElections, driver, "Election.sq",
      "selectAllElections", "SELECT * FROM ElectionEntity") { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!
    )
  }

  override fun selectAllElections(): Query<ElectionEntity> = selectAllElections(::ElectionEntity)

  override fun <T : Any> selectElectionByValues(
    region: String,
    office: String,
    year: String,
    mapper: (
      id: Int,
      region: String,
      office: String,
      year: String
    ) -> T
  ): Query<T> = SelectElectionByValuesQuery(region, office, year) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!
    )
  }

  override fun selectElectionByValues(
    region: String,
    office: String,
    year: String
  ): Query<ElectionEntity> = selectElectionByValues(region, office, year, ::ElectionEntity)

  override fun insertElection(
    region: String,
    office: String,
    year: String
  ) {
    driver.execute(-540098413, """
    |INSERT INTO ElectionEntity(region, office, year)
    |VALUES(?, ?, ?)
    """.trimMargin(), 3) {
      bindString(1, region)
      bindString(2, office)
      bindString(3, year)
    }
    notifyQueries(-540098413, {database.electionQueries.selectAllElections +
        database.electionQueries.selectElectionByValues})
  }

  override fun deleteAllElections() {
    driver.execute(-580574651, """DELETE FROM ElectionEntity""", 0)
    notifyQueries(-580574651, {database.electionQueries.selectAllElections +
        database.electionQueries.selectElectionByValues})
  }

  override fun deleteElectionById(id: Int) {
    driver.execute(-723192073, """
    |DELETE FROM ElectionEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }
    notifyQueries(-723192073, {database.electionQueries.selectAllElections +
        database.electionQueries.selectElectionByValues})
  }

  private inner class SelectElectionByValuesQuery<out T : Any>(
    @JvmField
    val region: String,
    @JvmField
    val office: String,
    @JvmField
    val year: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectElectionByValues, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(-10213809, """
    |SELECT * FROM ElectionEntity
    |WHERE region = ? AND office = ? AND year = ?
    """.trimMargin(), 3) {
      bindString(1, region)
      bindString(2, office)
      bindString(3, year)
    }

    override fun toString(): String = "Election.sq:selectElectionByValues"
  }
}

private class SeatQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), SeatQueries {
  internal val selectAllSeats: MutableList<Query<*>> = copyOnWriteList()

  internal val selectSeatByValues: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAllSeats(mapper: (
    id: Int,
    election: Int,
    name: String,
    votesRecorded: Long
  ) -> T): Query<T> = Query(1571718550, selectAllSeats, driver, "Seat.sq", "selectAllSeats",
      "SELECT * FROM SeatEntity") { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getLong(1)!!.toInt(),
      cursor.getString(2)!!,
      cursor.getLong(3)!!
    )
  }

  override fun selectAllSeats(): Query<SeatEntity> = selectAllSeats(::SeatEntity)

  override fun <T : Any> selectSeatByValues(
    election: Int,
    name: String,
    mapper: (
      id: Int,
      election: Int,
      name: String,
      votesRecorded: Long
    ) -> T
  ): Query<T> = SelectSeatByValuesQuery(election, name) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getLong(1)!!.toInt(),
      cursor.getString(2)!!,
      cursor.getLong(3)!!
    )
  }

  override fun selectSeatByValues(election: Int, name: String): Query<SeatEntity> =
      selectSeatByValues(election, name, ::SeatEntity)

  override fun insertSeat(election: Int, name: String) {
    driver.execute(-226782869, """
    |INSERT INTO SeatEntity(election, name)
    |VALUES (?, ?)
    """.trimMargin(), 2) {
      bindLong(1, election.toLong())
      bindString(2, name)
    }
    notifyQueries(-226782869, {database.seatQueries.selectAllSeats +
        database.seatQueries.selectSeatByValues})
  }

  override fun updateSeat(votesRecorded: Long, id: Int) {
    driver.execute(-1488879749, """
    |UPDATE SeatEntity
    |SET votesRecorded = ?
    |WHERE id = ?
    """.trimMargin(), 2) {
      bindLong(1, votesRecorded)
      bindLong(2, id.toLong())
    }
    notifyQueries(-1488879749, {database.seatQueries.selectAllSeats +
        database.seatQueries.selectSeatByValues})
  }

  override fun delelteAllSeats() {
    driver.execute(-523544061, """DELETE FROM SeatEntity""", 0)
    notifyQueries(-523544061, {database.seatQueries.selectAllSeats +
        database.seatQueries.selectSeatByValues})
  }

  override fun deleteSeatById(id: Int) {
    driver.execute(1964205263, """
    |DELETE FROM SeatEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }
    notifyQueries(1964205263, {database.seatQueries.selectAllSeats +
        database.seatQueries.selectSeatByValues})
  }

  private inner class SelectSeatByValuesQuery<out T : Any>(
    @JvmField
    val election: Int,
    @JvmField
    val name: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectSeatByValues, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(-723699545, """
    |SELECT * FROM SeatEntity
    |WHERE election = ? AND name = ?
    """.trimMargin(), 2) {
      bindLong(1, election.toLong())
      bindString(2, name)
    }

    override fun toString(): String = "Seat.sq:selectSeatByValues"
  }
}

private class CandidateSeatQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), CandidateSeatQueries {
  internal val selectAllCandidateSeats: MutableList<Query<*>> = copyOnWriteList()

  internal val selectCandidateSeatsByCandidate: MutableList<Query<*>> = copyOnWriteList()

  internal val selectCandidateSeatsBySeat: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAllCandidateSeats(mapper: (
    id: Int,
    candidate: Int,
    seat: Int,
    votes: Long
  ) -> T): Query<T> = Query(-1171989708, selectAllCandidateSeats, driver, "CandidateSeat.sq",
      "selectAllCandidateSeats", "SELECT * FROM CandidateSeatEntity") { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getLong(1)!!.toInt(),
      cursor.getLong(2)!!.toInt(),
      cursor.getLong(3)!!
    )
  }

  override fun selectAllCandidateSeats(): Query<CandidateSeatEntity> =
      selectAllCandidateSeats(::CandidateSeatEntity)

  override fun <T : Any> selectCandidateSeatsByCandidate(candidate: Int, mapper: (
    id: Int,
    candidate: Int,
    seat: Int,
    votes: Long
  ) -> T): Query<T> = SelectCandidateSeatsByCandidateQuery(candidate) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getLong(1)!!.toInt(),
      cursor.getLong(2)!!.toInt(),
      cursor.getLong(3)!!
    )
  }

  override fun selectCandidateSeatsByCandidate(candidate: Int): Query<CandidateSeatEntity> =
      selectCandidateSeatsByCandidate(candidate, ::CandidateSeatEntity)

  override fun <T : Any> selectCandidateSeatsBySeat(seat: Int, mapper: (
    id: Int,
    candidate: Int,
    seat: Int,
    votes: Long
  ) -> T): Query<T> = SelectCandidateSeatsBySeatQuery(seat) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getLong(1)!!.toInt(),
      cursor.getLong(2)!!.toInt(),
      cursor.getLong(3)!!
    )
  }

  override fun selectCandidateSeatsBySeat(seat: Int): Query<CandidateSeatEntity> =
      selectCandidateSeatsBySeat(seat, ::CandidateSeatEntity)

  override fun insertCandidateSeat(candidate: Int, seat: Int) {
    driver.execute(-1123607693, """
    |INSERT INTO CandidateSeatEntity(candidate, seat)
    |VALUES (?, ?)
    """.trimMargin(), 2) {
      bindLong(1, candidate.toLong())
      bindLong(2, seat.toLong())
    }
    notifyQueries(-1123607693, {database.candidateSeatQueries.selectAllCandidateSeats +
        database.candidateSeatQueries.selectCandidateSeatsByCandidate +
        database.candidateSeatQueries.selectCandidateSeatsBySeat})
  }

  override fun insertCandidateSeatWithVotes(
    candidate: Int,
    seat: Int,
    votes: Long
  ) {
    driver.execute(605891440, """
    |INSERT INTO CandidateSeatEntity(candidate, seat, votes)
    |VALUES (?, ?, ?)
    """.trimMargin(), 3) {
      bindLong(1, candidate.toLong())
      bindLong(2, seat.toLong())
      bindLong(3, votes)
    }
    notifyQueries(605891440, {database.candidateSeatQueries.selectAllCandidateSeats +
        database.candidateSeatQueries.selectCandidateSeatsByCandidate +
        database.candidateSeatQueries.selectCandidateSeatsBySeat})
  }

  override fun upadeCandidateSeat(votes: Long, id: Int) {
    driver.execute(1464748349, """
    |UPDATE CandidateSeatEntity
    |SET votes = ?
    |WHERE id = ?
    """.trimMargin(), 2) {
      bindLong(1, votes)
      bindLong(2, id.toLong())
    }
    notifyQueries(1464748349, {database.candidateSeatQueries.selectAllCandidateSeats +
        database.candidateSeatQueries.selectCandidateSeatsByCandidate +
        database.candidateSeatQueries.selectCandidateSeatsBySeat})
  }

  override fun deleteAllCandidateSeats() {
    driver.execute(-2063524251, """DELETE FROM CandidateSeatEntity""", 0)
    notifyQueries(-2063524251, {database.candidateSeatQueries.selectAllCandidateSeats +
        database.candidateSeatQueries.selectCandidateSeatsByCandidate +
        database.candidateSeatQueries.selectCandidateSeatsBySeat})
  }

  override fun deleteCandidateSeatById(id: Int) {
    driver.execute(-177981133, """
    |DELETE FROM CandidateSeatEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }
    notifyQueries(-177981133, {database.candidateSeatQueries.selectAllCandidateSeats +
        database.candidateSeatQueries.selectCandidateSeatsByCandidate +
        database.candidateSeatQueries.selectCandidateSeatsBySeat})
  }

  private inner class SelectCandidateSeatsByCandidateQuery<out T : Any>(
    @JvmField
    val candidate: Int,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectCandidateSeatsByCandidate, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(-1821990263, """
    |SELECT * FROM CandidateSeatEntity
    |WHERE candidate = ?
    """.trimMargin(), 1) {
      bindLong(1, candidate.toLong())
    }

    override fun toString(): String = "CandidateSeat.sq:selectCandidateSeatsByCandidate"
  }

  private inner class SelectCandidateSeatsBySeatQuery<out T : Any>(
    @JvmField
    val seat: Int,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectCandidateSeatsBySeat, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(683816255, """
    |SELECT * FROM CandidateSeatEntity
    |WHERE seat = ?
    """.trimMargin(), 1) {
      bindLong(1, seat.toLong())
    }

    override fun toString(): String = "CandidateSeat.sq:selectCandidateSeatsBySeat"
  }
}

private class CandidateQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), CandidateQueries {
  internal val selectAllCandidate: MutableList<Query<*>> = copyOnWriteList()

  internal val selectCandidateById: MutableList<Query<*>> = copyOnWriteList()

  internal val selectCandidatesByName: MutableList<Query<*>> = copyOnWriteList()

  internal val selectCandidatesByParty: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAllCandidate(mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T> = Query(-382515179, selectAllCandidate, driver, "Candidate.sq",
      "selectAllCandidate", "SELECT * FROM CandidateEntity") { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt()
    )
  }

  override fun selectAllCandidate(): Query<CandidateEntity> = selectAllCandidate(::CandidateEntity)

  override fun <T : Any> selectCandidateById(id: Int, mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T> = SelectCandidateByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt()
    )
  }

  override fun selectCandidateById(id: Int): Query<CandidateEntity> = selectCandidateById(id,
      ::CandidateEntity)

  override fun <T : Any> selectCandidatesByName(name: String, mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T> = SelectCandidatesByNameQuery(name) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt()
    )
  }

  override fun selectCandidatesByName(name: String): Query<CandidateEntity> =
      selectCandidatesByName(name, ::CandidateEntity)

  override fun <T : Any> selectCandidatesByParty(party: Int, mapper: (
    id: Int,
    name: String,
    party: Int
  ) -> T): Query<T> = SelectCandidatesByPartyQuery(party) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt()
    )
  }

  override fun selectCandidatesByParty(party: Int): Query<CandidateEntity> =
      selectCandidatesByParty(party, ::CandidateEntity)

  override fun insertCandidate(name: String, party: Int) {
    driver.execute(-837293549, """
    |INSERT INTO CandidateEntity(name, party)
    |VALUES(?, ?)
    """.trimMargin(), 2) {
      bindString(1, name)
      bindLong(2, party.toLong())
    }
    notifyQueries(-837293549, {database.candidateQueries.selectAllCandidate +
        database.candidateQueries.selectCandidateById +
        database.candidateQueries.selectCandidatesByName +
        database.candidateQueries.selectCandidatesByParty +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyByIdWithCandidates})
  }

  override fun updateCandidate(
    name: String,
    party: Int,
    id: Int
  ) {
    driver.execute(-1994111485, """
    |UPDATE CandidateEntity
    |SET name = ?, party = ?
    |WHERE id = ?
    """.trimMargin(), 3) {
      bindString(1, name)
      bindLong(2, party.toLong())
      bindLong(3, id.toLong())
    }
    notifyQueries(-1994111485, {database.candidateQueries.selectAllCandidate +
        database.candidateQueries.selectCandidateById +
        database.candidateQueries.selectCandidatesByName +
        database.candidateQueries.selectCandidatesByParty +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyByIdWithCandidates})
  }

  override fun deleteAllCandidates() {
    driver.execute(-338870609, """DELETE FROM CandidateEntity""", 0)
    notifyQueries(-338870609, {database.candidateQueries.selectAllCandidate +
        database.candidateQueries.selectCandidateById +
        database.candidateQueries.selectCandidatesByName +
        database.candidateQueries.selectCandidatesByParty +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyByIdWithCandidates})
  }

  override fun deleteCandidateById(id: Int) {
    driver.execute(584879827, """
    |DELETE FROM CandidateEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }
    notifyQueries(584879827, {database.candidateQueries.selectAllCandidate +
        database.candidateQueries.selectCandidateById +
        database.candidateQueries.selectCandidatesByName +
        database.candidateQueries.selectCandidatesByParty +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyByIdWithCandidates})
  }

  private inner class SelectCandidateByIdQuery<out T : Any>(
    @JvmField
    val id: Int,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectCandidateById, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(1950681890, """
    |SELECT * FROM CandidateEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }

    override fun toString(): String = "Candidate.sq:selectCandidateById"
  }

  private inner class SelectCandidatesByNameQuery<out T : Any>(
    @JvmField
    val name: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectCandidatesByName, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(863844517, """
    |SELECT * FROM CandidateEntity
    |WHERE name = ?
    """.trimMargin(), 1) {
      bindString(1, name)
    }

    override fun toString(): String = "Candidate.sq:selectCandidatesByName"
  }

  private inner class SelectCandidatesByPartyQuery<out T : Any>(
    @JvmField
    val party: Int,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectCandidatesByParty, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(1011228684, """
    |SELECT * FROM CandidateEntity
    |WHERE party = ?
    """.trimMargin(), 1) {
      bindLong(1, party.toLong())
    }

    override fun toString(): String = "Candidate.sq:selectCandidatesByParty"
  }
}

private class PartyQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), PartyQueries {
  internal val selectAllParties: MutableList<Query<*>> = copyOnWriteList()

  internal val selectAllPartiesWithCandidate: MutableList<Query<*>> = copyOnWriteList()

  internal val selectPartyById: MutableList<Query<*>> = copyOnWriteList()

  internal val selectPartyByIdWithCandidates: MutableList<Query<*>> = copyOnWriteList()

  internal val selectPartyByName: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAllParties(mapper: (id: Int, name: String) -> T): Query<T> =
      Query(1133081081, selectAllParties, driver, "Party.sq", "selectAllParties",
      "SELECT * FROM PartyEntity") { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!
    )
  }

  override fun selectAllParties(): Query<PartyEntity> = selectAllParties(::PartyEntity)

  override fun <T : Any> selectAllPartiesWithCandidate(mapper: (
    id: Int,
    name: String,
    id_: Int,
    name_: String,
    party: Int
  ) -> T): Query<T> = Query(-1687290396, selectAllPartiesWithCandidate, driver, "Party.sq",
      "selectAllPartiesWithCandidate", """
  |SELECT *
  |FROM PartyEntity P, CandidateEntity C
  |WHERE P.id = C.party
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt(),
      cursor.getString(3)!!,
      cursor.getLong(4)!!.toInt()
    )
  }

  override fun selectAllPartiesWithCandidate(): Query<SelectAllPartiesWithCandidate> =
      selectAllPartiesWithCandidate(::SelectAllPartiesWithCandidate)

  override fun <T : Any> selectPartyById(id: Int, mapper: (id: Int, name: String) -> T): Query<T> =
      SelectPartyByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!
    )
  }

  override fun selectPartyById(id: Int): Query<PartyEntity> = selectPartyById(id, ::PartyEntity)

  override fun <T : Any> selectPartyByIdWithCandidates(id: Int, mapper: (
    id: Int,
    name: String,
    id_: Int,
    name_: String,
    party: Int
  ) -> T): Query<T> = SelectPartyByIdWithCandidatesQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt(),
      cursor.getString(3)!!,
      cursor.getLong(4)!!.toInt()
    )
  }

  override fun selectPartyByIdWithCandidates(id: Int): Query<SelectPartyByIdWithCandidates> =
      selectPartyByIdWithCandidates(id, ::SelectPartyByIdWithCandidates)

  override fun <T : Any> selectPartyByName(name: String, mapper: (id: Int, name: String) -> T):
      Query<T> = SelectPartyByNameQuery(name) { cursor ->
    mapper(
      cursor.getLong(0)!!.toInt(),
      cursor.getString(1)!!
    )
  }

  override fun selectPartyByName(name: String): Query<PartyEntity> = selectPartyByName(name,
      ::PartyEntity)

  override fun insertParty(name: String) {
    driver.execute(-862028621, """
    |INSERT INTO PartyEntity(name)
    |VALUES (?)
    """.trimMargin(), 1) {
      bindString(1, name)
    }
    notifyQueries(-862028621, {database.partyQueries.selectAllParties +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyById +
        database.partyQueries.selectPartyByIdWithCandidates +
        database.partyQueries.selectPartyByName})
  }

  override fun updateParty(name: String, id: Int) {
    driver.execute(-1332326237, """
    |UPDATE PartyEntity
    |SET name = ?
    |WHERE id = ?
    """.trimMargin(), 2) {
      bindString(1, name)
      bindLong(2, id.toLong())
    }
    notifyQueries(-1332326237, {database.partyQueries.selectAllParties +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyById +
        database.partyQueries.selectPartyByIdWithCandidates +
        database.partyQueries.selectPartyByName})
  }

  override fun deleteAllParties() {
    driver.execute(-714935320, """DELETE FROM PartyEntity""", 0)
    notifyQueries(-714935320, {database.partyQueries.selectAllParties +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyById +
        database.partyQueries.selectPartyByIdWithCandidates +
        database.partyQueries.selectPartyByName})
  }

  override fun deletePartyById(id: Int) {
    driver.execute(-859035533, """
    |DELETE FROM PartyEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }
    notifyQueries(-859035533, {database.partyQueries.selectAllParties +
        database.partyQueries.selectAllPartiesWithCandidate +
        database.partyQueries.selectPartyById +
        database.partyQueries.selectPartyByIdWithCandidates +
        database.partyQueries.selectPartyByName})
  }

  private inner class SelectPartyByIdQuery<out T : Any>(
    @JvmField
    val id: Int,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectPartyById, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(2110071874, """
    |SELECT * FROM PartyEntity
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }

    override fun toString(): String = "Party.sq:selectPartyById"
  }

  private inner class SelectPartyByIdWithCandidatesQuery<out T : Any>(
    @JvmField
    val id: Int,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectPartyByIdWithCandidates, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(952057240, """
    |SELECT *
    |FROM PartyEntity P, CandidateEntity C
    |WHERE P.id = ? AND C.party = P.id
    """.trimMargin(), 1) {
      bindLong(1, id.toLong())
    }

    override fun toString(): String = "Party.sq:selectPartyByIdWithCandidates"
  }

  private inner class SelectPartyByNameQuery<out T : Any>(
    @JvmField
    val name: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectPartyByName, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(554656754, """
    |SELECT * FROM PartyEntity
    |WHERE name = ?
    """.trimMargin(), 1) {
      bindString(1, name)
    }

    override fun toString(): String = "Party.sq:selectPartyByName"
  }
}
