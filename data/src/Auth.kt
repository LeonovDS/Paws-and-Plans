import arrow.core.Either
import arrow.core.right
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.asIterable
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import table.AuthTable
import java.util.*

context(Database)
fun insertAuthData(data: AuthData) = insert(AuthTable) {
    set(it.login, data.login)
    set(it.password, data.password)
}

context(Database)
fun getAuthId(login: String, password: String): Either<DomainError, UUID> =
    from(AuthTable)
        .select(AuthTable.id)
        .where {
            (AuthTable.login eq login) and (AuthTable.password eq password)
        }.asIterable()
        .firstOrNull()
        ?.get(AuthTable.id)
        ?.right() ?: Either.Left(NotFound)
