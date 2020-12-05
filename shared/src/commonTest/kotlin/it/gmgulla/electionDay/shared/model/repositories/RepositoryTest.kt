package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Party
import it.gmgulla.electionDay.shared.model.entities.isPersisted
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class RepositoryTest {

    private val candidateRepository = CandidateRepository()
    private val partyRepository = PartyRepository()

    @BeforeTest fun deleteDatabase() {
        candidateRepository.deleteAll()
        partyRepository.deleteAll()
    }

    @Test fun `checkPersistedEntity`() {
        val partyName = "Republican"
        val partyToAdd = Party(name = partyName)
        partyRepository.add(partyToAdd)
        val persistedParty = partyRepository.getByName(partyName)
        assertTrue { persistedParty.isPersisted }
    }

    @Test fun `delete All Instances With Cascade Mode`() {
        val partyRepositoryTest = PartyRepositoryTest()
        val partyName = partyRepositoryTest.addAndGetSomePartyByName()
        val party = partyRepository.getByName(partyName)
        partyRepositoryTest.addAndGetCandidates(party)
        partyRepository.delete(party)
        val candidates = candidateRepository.getAll()
        println(candidates)
        assertTrue { candidates.isEmpty() }
    }

    @Test fun `delete Entity Related With Others`() {
        TODO("Implements when all repos are implemented")
    }

    @Test fun `entity Not Found`() {
        val anId = Random.Default.nextInt(Int.MIN_VALUE, 0)
        isEntityNotFoundExceptionThrown(anId)
    }

    private fun isEntityNotFoundExceptionThrown(anId: Int) {
        assertFailsWith<EntityNotFoundException> {
            partyRepository.getById(anId)
        }.let {
            println(it.message)
        }
    }

}