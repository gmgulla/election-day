package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Election
import it.gmgulla.electionDay.shared.model.entities.isPersisted
import kotlin.test.Test
import kotlin.test.assertTrue

class ElectionRepositoryTest {

    private val electionRepository = ElectionRepository()

    @Test fun `add One And Get It`() {
        val region = "USA"
        val office = "President"
        val year = 2020
        val electionToAdd = Election(region = region, office = office, year = year)
        electionRepository.add(electionToAdd)
        val election = electionRepository.getByValues(region, office, year)
        isdEqualToPersistedElection(election, electionToAdd)
    }

    private fun isdEqualToPersistedElection(election: Election, electionToAdd: Election) {
        assertTrue {
            election.isPersisted
                && election.region == electionToAdd.region
                && election.office == electionToAdd.office
                && election.year == electionToAdd.year
        }.let {
            println("electionToAdd = $electionToAdd")
            println("election = $election")
        }
    }
}