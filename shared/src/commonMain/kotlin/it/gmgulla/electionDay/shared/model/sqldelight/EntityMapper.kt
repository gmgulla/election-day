package it.gmgulla.electionDay.shared.model.sqldelight

import it.gmgulla.electionDay.shared.model.entities.Election

internal object EntityMapper {

    fun mapElectionEntity(
        id: Int,
        region: String,
        office: String,
        year: String
    ): Election {
        return Election(
            id = id,
            region = region,
            office = office,
            year = year
        )
    }
}