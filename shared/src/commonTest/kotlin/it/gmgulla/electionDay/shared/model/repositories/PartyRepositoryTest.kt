package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Candidate
import it.gmgulla.electionDay.shared.model.entities.Party
import kotlin.test.*

class PartyRepositoryTest {

    private val partyRepository = PartyRepository()
    private val candidateRepository = CandidateRepository()

    @BeforeTest fun deleteDatabase() {
        candidateRepository.deleteAll()
        partyRepository.deleteAll()
    }

    @Test fun `add One Instance And Get It`() {
        val partyName = addAndGetSomePartyByName()
        val party = partyRepository.getByName(partyName)
        assertNotNull(party) { println(it) }
    }

    internal fun addAndGetSomePartyByName(): String {
        val firstPartyName = "Democratic"
        addParty(firstPartyName)
        return firstPartyName
    }


    private fun addParty(firstPartyName: String) {
        val partyToAdd = Party(name = firstPartyName)
        partyRepository.add(partyToAdd)
    }

    @Test fun `add More Instances And Get Them`() {
        val firstPartyName = addAndGetSomePartyByName()
        val secondPartyName = addByAndGetAnotherPartyName()
        val parties = partyRepository.getAll()
        isPartiesSizeEqualToTwo(parties)
        partiesContainTheseNames(parties, firstPartyName, secondPartyName)
        println(parties)

    }

    private fun addByAndGetAnotherPartyName(): String {
        val secondPartyName = "Republican"
        addParty(secondPartyName)
        return secondPartyName
    }

    private fun isPartiesSizeEqualToTwo(parties: List<Party>) {
        val expectedPartiesAmount = 2
        val partiesAmount = parties.size
        assertEquals(expectedPartiesAmount, partiesAmount)
    }

    private fun partiesContainTheseNames(
        parties: List<Party>,
        firstPartyName: String,
        secondPartyName: String
    ) {
        val partiesNames = parties.map { it.name }
        assertTrue {
            firstPartyName != secondPartyName
            && firstPartyName in partiesNames
            && secondPartyName in partiesNames
        }
        assertTrue { firstPartyName in partiesNames }
        assertTrue { secondPartyName in partiesNames }
    }

    @Test fun `add With Existing Name`() {
        val name = addAndGetSomePartyByName()
        val partyWithExistingName = Party(name = name)
        isExistingUniqueValueExceptionThrown(partyWithExistingName)
    }

    private fun isExistingUniqueValueExceptionThrown(partyWithExistingName: Party) {
        assertFailsWith<ExistingUniqueValueException> {
            partyRepository.add(partyWithExistingName)
        }.let {
            println(it.message)
        }
    }



    @Test fun `update One Instance`() {
        val partyName = addAndGetSomePartyByName()
        val party = partyRepository.getByName(partyName)
        val newPartyName = updateParty(party)
        val updatedParty = partyRepository.getByName(newPartyName)
        isPartyUpdated(updatedParty, party, newPartyName)
    }

    private fun updateParty(party: Party): String {
        val newPartyName = "PD"
        val modifiedParty = party.copy(name = newPartyName)
        partyRepository.update(modifiedParty)
        return newPartyName
    }

    private fun isPartyUpdated(
        updatedParty: Party,
        party: Party,
        newPartyName: String
    ) {
        assertTrue {
            updatedParty.id == party.id
                    && updatedParty.name == newPartyName
        }.let {
            println("party = $party, updatedParty = $updatedParty")
            println(partyRepository.getAll())
        }
    }

    @Test fun `delete All Instances`() {
        addTwoAndDeleteAll()
        isAnyPartyDeleted()
    }

    private fun addTwoAndDeleteAll() {
        addByAndGetAnotherPartyName()
        addAndGetSomePartyByName()
        partyRepository.deleteAll()
    }

    private fun isAnyPartyDeleted() {
        assertTrue { partyRepository.getAll().isEmpty() }
    }

    @Test fun `get Candidate List`() {
        //TODO("Implement CandidateRepository before")
        val partyName = addAndGetSomePartyByName()
        val party = partyRepository.getByName(partyName)
        val candidates = addAndGetCandidates(party)
        val partyCandidates = party.candidates
        partyCandidatesContainsAllItsCandidates(partyCandidates, candidates)
    }

    internal fun addAndGetCandidates(party: Party): List<Candidate> {
        val candidatesToAdd = listOf(
            Candidate(name = "Obama", party = party),
            Candidate(name = "Biden", party = party)
        )
        candidatesToAdd.forEach { candidateRepository.add(it) }
        return candidateRepository.getAll()
    }

    private fun partyCandidatesContainsAllItsCandidates(
        partyCandidates: List<Candidate>,
        candidates: List<Candidate>
    ) {
        assertTrue { partyCandidates.containsAll(candidates) }
        println(candidates)
        println(partyCandidates)
    }

}