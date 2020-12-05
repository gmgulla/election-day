package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Candidate
import it.gmgulla.electionDay.shared.model.entities.Party
import kotlin.test.*

class CandidateRepositoryTest {

    private val candidateRepository = CandidateRepository()
    private val partyRepository = PartyRepository()

    @BeforeTest fun deleteDatabse() {
        candidateRepository.deleteAll()
        partyRepository.deleteAll()
    }

    @Test fun `add One Instance And Get It`() {
        val party = addRepublicanPartyAndGetIt()
        val candidateName = "Trump"
        val candidateToAdd = addAndGetCandidate(candidateName, party)
        val candidate = candidateRepository.getByName(candidateName).first()
        isCandidateAdded(candidate, candidateName, candidateToAdd)
    }

    private fun addRepublicanPartyAndGetIt(): Party {
        val partyName = "Republican"
        return addPartyByNameAndGetIt(partyName)
    }

    private fun addPartyByNameAndGetIt(partyName: String): Party {
        val partyToAdd = Party(name = partyName)
        partyRepository.add(partyToAdd)
        return partyRepository.getByName(partyName)
    }

    private fun addAndGetCandidate(candidateName: String, party: Party): Candidate {
        val candidateToAdd = Candidate(name = candidateName, party = party)
        candidateRepository.add(candidateToAdd)
        return candidateToAdd
    }

    private fun isCandidateAdded(
        candidate: Candidate,
        candidateName: String,
        candidateToAdd: Candidate
    ) {
        assertTrue {
            candidate.name == candidateName && candidate.party == candidateToAdd.party
        }
        println(
            """
                candidateToAdd =  $candidateToAdd
                candidate = $candidate
            """.trimIndent()
        )
    }

    @Test fun `add More Instances And Get Them`() {
        val democraticParty = addDemoctraticPartyAndGetIt()
        val republicanParty = addRepublicanPartyAndGetIt()
        val obama = addAndGetCandidate("Obama", democraticParty)
        val biden = addAndGetCandidate("Biden", democraticParty)
        val trump = addAndGetCandidate("Trump", republicanParty)
        val candidatesToAdd = listOf(obama, biden, trump)
        val candidates = candidateRepository.getAll()
        areCandidatesAdded(candidatesToAdd, candidates)
    }

    private fun addDemoctraticPartyAndGetIt(): Party {
        val partyName = "Democratic"
        return addPartyByNameAndGetIt(partyName)
    }

    private fun areCandidatesAdded(candidatesToAdd: List<Candidate>, candidates: List<Candidate>) {
        assertTrue {
            candidatesToAdd.all { candidateToAdd ->
                candidates.any {
                    (it.name == candidateToAdd.name) and (it.party == candidateToAdd.party)
                }
            }
        }
    }

    @Test fun `update One`() {
        val candidate = addOneAndGetIt()
        val updatedCandidate = updateOneAndGetIt(candidate)
        isCandidateUpdated(candidate, updatedCandidate)

    }

    private fun addOneAndGetIt(): Candidate {
        val democraticParty = addDemoctraticPartyAndGetIt()
        addAndGetCandidate("Obama", democraticParty)
        return candidateRepository.getAll().first()
    }

    private fun updateOneAndGetIt(candidate: Candidate): Candidate {
        val republicanParty = addRepublicanPartyAndGetIt()
        val modifiedCandidate = candidate.copy(name = "Trump", party = republicanParty)
        candidateRepository.update(modifiedCandidate)
        return candidateRepository.getAll().first()
    }

    private fun isCandidateUpdated(
        candidate: Candidate,
        updatedCandidate: Candidate
    ) {
        assertTrue { candidate.id == updatedCandidate.id }
    }

    @Test fun `delete All Instances`() {

    }


    @Test fun `add And Get Seats`() {
        TODO("Implememts SeatRepository before")
    }
}
