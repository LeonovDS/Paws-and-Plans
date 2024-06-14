package model

import data.Pet
import data.PetData
import data.TaskData
import data.UserData

sealed class Model(
    val template: String,
)

data class IndexModel(
    val userData: UserData,
    val petImage: String,
    val petQuote: String,
    val tasks: List<TaskData>,
) : Model("Index.kte")

sealed class LoginModel : Model("Login.kte")

data object SignInModel : LoginModel()
data class SignUpModel(
    val pets: List<PetData>,
) : LoginModel()


data class ShopModel(
    val pets: List<Pet>
) : Model("Shop.kte")

data class TasksModel(
    val tasks: List<TaskData>,
    val currentTask: TaskData?,
    val pet: String,
) : Model("Tasks.kte")

data class TaskModel(
    val task: TaskData,
) : Model("Task.kte")
