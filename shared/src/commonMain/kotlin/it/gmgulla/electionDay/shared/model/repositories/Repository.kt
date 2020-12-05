package it.gmgulla.electionDay.shared.model.repositories

interface Repository<T> {

        fun add(entity: T)

        fun getById(id: Int): T

        fun getAll(): List<T>

        fun update(entity: T)

        fun delete(entity: T)

        fun deleteAll()

}