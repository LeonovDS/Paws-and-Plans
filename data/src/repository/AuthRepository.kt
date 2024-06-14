package repository

import DomainError
import NotAuthentificated
import SignInData
import SignUpData
import SqlError
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import org.ktorm.database.Database
import org.ktorm.dsl.*
import table.AuthTable
import java.util.*

class AuthRepository(private val db: Database) : IAuthRepository {
    override fun createAuthData(data: SignUpData): Either<DomainError, UUID> = catch({
        (db.insertAndGenerateKey(AuthTable) {
            set(AuthTable.login, data.login)
            set(AuthTable.password, data.password)
        } as UUID).right()
    }) {
        SqlError(it).left()
    }

    override fun getAuthId(data: SignInData): Either<DomainError, UUID> = catch({
        db
            .from(AuthTable)
            .select(AuthTable.id)
            .where { (AuthTable.login eq data.login) and (AuthTable.password eq data.password) }
            .map { it[AuthTable.id]!! }
            .firstOrNull()
            ?.right()
            ?: NotAuthentificated.left()
    }) {
        SqlError(it).left()
    }
}
