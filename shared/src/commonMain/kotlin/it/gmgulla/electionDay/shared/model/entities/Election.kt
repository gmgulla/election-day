package it.gmgulla.electionDay.shared.model.entities


data class Election(

        val id: Int = Int.MIN_VALUE,

        val region: String,

        val office: String,

        val year: Int
) {
        val seats: List<Seat>
                get() = TODO("Implements when SeatRepository is implemented")
}

val Election.isPersisted: Boolean
        get() = this.id >= 0