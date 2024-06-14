package repository

import DomainError
import arrow.core.Either
import data.PetData
import data.PetImage
import data.PetQuote
import model.Pet
import java.util.*

interface IPetRepository {
    fun getAllPets(): Either<DomainError, List<PetData>>
    fun getPet(petId: UUID): Either<DomainError, PetData>
    fun getQuotes(petId: UUID): Either<DomainError, List<PetQuote>>
    fun getImages(petId: UUID): Either<DomainError, List<PetImage>>
    fun getPetByKind(petKind: String): Either<DomainError, PetData>
    fun addPetInPossession(userId: UUID, petId: UUID): Either<DomainError, Unit>
    fun getPetsInPossession(userId: UUID): Either<DomainError, List<UUID>>
}