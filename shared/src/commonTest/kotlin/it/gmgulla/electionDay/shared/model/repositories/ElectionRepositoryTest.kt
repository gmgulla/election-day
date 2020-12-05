package it.gmgulla.electionDay.shared.model.repositories

import it.gmgulla.electionDay.shared.model.entities.Election
import kotlin.test.Test

class ElectionRepositoryTest {

    private val electionRepository = ElectionRepository()

    @Test fun `add One And Get It`() {
        val region = "USA"
        val office = "President"
        val year = 2020
        val electionToAdd = Election(region = region, office = office, year = year)
        electionRepository.add(electionToAdd)
        val election = electionRepository.getByValues()

    }
}