package repository

import DomainError
import arrow.core.Either
import data.Id
import data.UserData

interface IUserRepository {
    context(Id)
    fun getUserData(): Either<DomainError, UserData>

    context(Id)
    fun createUser(userData: UserData): Either<DomainError, Unit>

    context(Id)
    fun updateCoins(newCoins: Int): Either<DomainError, Unit>

    context(Id)
    fun updateHappiness(newHappiness: Int): Either<DomainError, Unit>
}