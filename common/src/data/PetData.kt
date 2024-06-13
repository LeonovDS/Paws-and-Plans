package data

import java.util.UUID

data class PetData(
    val id: UUID? = null,
    val kind: String,
    val price: Int,
)