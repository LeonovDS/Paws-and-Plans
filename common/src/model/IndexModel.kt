package model

import data.TaskData
import data.UserData

data class IndexModel(
    val userData: UserData,
    val petImage: String,
    val petQuote: String,
    val tasks: List<TaskData>,
)
