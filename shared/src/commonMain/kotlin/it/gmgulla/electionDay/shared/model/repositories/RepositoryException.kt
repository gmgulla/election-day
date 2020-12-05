package it.gmgulla.electionDay.shared.model.repositories

internal open class RepositoryException : RuntimeException()

internal class EntityNotFoundException(
    private val dataTypeName: String,
    private val memberTypeName: String,
    private val id: Any
) : RepositoryException() {

    override val message: String? = """
            Entity not found. $dataTypeName with ($memberTypeName $id) does not exist.
        """.trimIndent()
}

internal class ExistingUniqueValueException(
    private val entity: Any
) : RepositoryException() {
    override val message: String? = """
        Unable to add this instance. Some "UNIQUE" attribute is already present in another instance.
        $entity
    """.trimIndent()
}