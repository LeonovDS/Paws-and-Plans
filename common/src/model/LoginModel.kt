package model

import data.PetData

sealed interface LoginModel

data object SignInModel : LoginModel
data class SignUpModel(
    val pets: List<PetData>,
) : LoginModel
