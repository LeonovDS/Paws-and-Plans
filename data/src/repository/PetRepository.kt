package repository

import DomainError
import NotFound
import SqlError
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import data.PetData
import data.PetImage
import data.PetQuote
import model.Pet
import org.ktorm.database.Database
import org.ktorm.dsl.*
import table.PetImageTable
import table.PetQuoteTable
import table.PetTable
import table.PossessionTable
import java.util.*

fun toPetData(query: QueryRowSet): PetData =
    PetData(
        id = query[PetTable.id]!!,
        kind = query[PetTable.kind]!!,
        price = query[PetTable.price]!!,
        kindTranslation = query[PetTable.kindTranslation]!!
    )


class PetRepository(private val db: Database) : IPetRepository {
    override fun getAllPets(): Either<DomainError, List<PetData>> = catch({
        db.from(PetTable).select().map(::toPetData).toList().right()
    }) {
        SqlError(it).left()
    }

    override fun getPet(petId: UUID): Either<DomainError, PetData> = catch({
        db.from(PetTable).select().where(PetTable.id eq petId).map(::toPetData).firstOrNull()?.right()
            ?: NotFound.left()
    }) {
        SqlError(it).left()
    }


    override fun getQuotes(petId: UUID): Either<DomainError, List<PetQuote>> = catch({
        db.from(PetQuoteTable).select().where(PetQuoteTable.petId eq petId).map {
            PetQuote(
                quote = it[PetQuoteTable.quote]!!,
                minHappiness = it[PetQuoteTable.minHappiness]!!,
                maxHappiness = it[PetQuoteTable.maxHappiness]!!,
            )
        }.toList().right()
    }) {
        SqlError(it).left()
    }

    override fun getImages(petId: UUID): Either<DomainError, List<PetImage>> = catch({
        db.from(PetImageTable).select().where(PetImageTable.petId eq petId).map {
            PetImage(
                url = it[PetImageTable.url]!!,
                minHappiness = it[PetImageTable.minHappiness]!!,
                maxHappiness = it[PetImageTable.maxHappiness]!!,
            )
        }.toList().right()
    }) {
        SqlError(it).left()
    }

    override fun getPetByKind(petKind: String): Either<DomainError, PetData> = catch({
        db.from(PetTable).select().where(PetTable.kind eq petKind).map(::toPetData).firstOrNull()?.right()
            ?: NotFound.left()
    }) {
        SqlError(it).left()
    }

    override fun addPetInPossession(userId: UUID, petId: UUID): Either<DomainError, Unit> = catch({
        db.insert(PossessionTable) {
            set(PossessionTable.ownerId, userId)
            set(PossessionTable.petId, petId)
        }
        Unit.right()
    }) {
        SqlError(it).left()
    }

    override fun getPetsInPossession(userId: UUID): Either<DomainError, List<UUID>> = catch({
        db.from(PossessionTable).select().where(PossessionTable.ownerId eq userId).map {
            it[PossessionTable.petId]!!
        }.right()
    }) {
        SqlError(it).left()
    }
}