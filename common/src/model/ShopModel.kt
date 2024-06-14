package model

import java.util.*

data class Pet(
    val id: UUID,
    val kind: String,
    val price: Int,
    val image: String,
    val isSelected: Boolean,
    val isOwned: Boolean,
)

data class ShopModel(
    val pets: List<Pet>
)
