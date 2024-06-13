package repository

import DomainError
import NotFound
import SqlError
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import data.Id
import data.PetData
import data.UserData
import org.ktorm.database.Database
import org.ktorm.dsl.*
import table.AppUserTable
import table.PetTable

class UserRepository(private val db: Database) : IUserRepository {
    context(Id)
    override fun getUserData(): Either<DomainError, UserData> = catch({
        db.from(AppUserTable)
            .leftJoin(PetTable, on = AppUserTable.currentPet eq PetTable.id)
            .select()
            .where { AppUserTable.id eq id }
            .map {
                UserData(
                    petName = it[AppUserTable.petName]!!,
                    happiness = it[AppUserTable.happiness]!!,
                    coins = it[AppUserTable.coins]!!,
                    currentPet = PetData(
                        id = it[PetTable.id]!!,
                        kind = it[PetTable.kind]!!,
                        price = it[PetTable.price]!!,
                    )
                )
            }.firstOrNull()
            ?.right()
            ?: NotFound.left()
    }) {
        SqlError(it).left()
    }

    context(Id) override fun createUser(userData: UserData): Either<DomainError, Unit> = catch({
        db.insert(AppUserTable) {
            set(AppUserTable.id, id)
            set(AppUserTable.petName, userData.petName)
            set(AppUserTable.happiness, userData.happiness)
            set(AppUserTable.coins, userData.coins)
            set(AppUserTable.currentPet, userData.currentPet.id)
        }
        Unit.right()
    }) {
        SqlError(it).left()
    }

}