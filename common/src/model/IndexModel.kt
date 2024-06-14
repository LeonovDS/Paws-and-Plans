package model

import data.PetImage
import data.PetQuote
import data.TaskData
import data.UserData
import java.util.UUID

data class IndexModel(
    val userData: UserData,
    val petImage: PetImage,
    val petQuote: PetQuote,
    val tasks: List<TaskData>,
)
