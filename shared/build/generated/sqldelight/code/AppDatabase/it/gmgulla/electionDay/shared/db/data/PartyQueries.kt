package it.gmgulla.electionDay.shared.db.data

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.String

interface PartyQueries : Transacter {
  fun <T : Any> selectAllParties(mapper: (id: Int, name: String) -> T): Query<T>

  fun selectAllParties(): Query<PartyEntity>

  fun <T : Any> selectAllPartiesWithCandidate(mapper: (
    id: Int,
    name: String,
    id_: Int,
    name_: String,
    party: Int
  ) -> T): Query<T>

  fun selectAllPartiesWithCandidate(): Query<SelectAllPartiesWithCandidate>

  fun <T : Any> selectPartyById(id: Int, mapper: (id: Int, name: String) -> T): Query<T>

  fun selectPartyById(id: Int): Query<PartyEntity>

  fun <T : Any> selectPartyByIdWithCandidates(id: Int, mapper: (
    id: Int,
    name: String,
    id_: Int,
    name_: String,
    party: Int
  ) -> T): Query<T>

  fun selectPartyByIdWithCandidates(id: Int): Query<SelectPartyByIdWithCandidates>

  fun <T : Any> selectPartyByName(name: String, mapper: (id: Int, name: String) -> T): Query<T>

  fun selectPartyByName(name: String): Query<PartyEntity>

  fun insertParty(name: String)

  fun updateParty(name: String, id: Int)

  fun deleteAllParties()

  fun deletePartyById(id: Int)
}
