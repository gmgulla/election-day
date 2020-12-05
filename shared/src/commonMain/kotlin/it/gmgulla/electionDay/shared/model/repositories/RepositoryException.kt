package it.gmgulla.electionDay.shared.model.repositories

internal open class RepositoryException : RuntimeException()

internal class EntityNotFoundException(
    private val dataTypeName: String,
    private val memberTypeName: String? = null,
    private val id: Any? = null
) : RepositoryException() {

    override val message: String? = """
            Entity not found. ${
                if (memberTypeName == null) ""
                else "$dataTypeName with $memberTypeName = $id does not exist"
            }.
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