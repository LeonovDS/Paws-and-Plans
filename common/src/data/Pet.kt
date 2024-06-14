package data

import java.util.*

data class Pet(
    val id: UUID,
    val kind: String,
    val price: Int,
    val image: String,
    val isSelected: Boolean,
    val isOwned: Boolean,
)