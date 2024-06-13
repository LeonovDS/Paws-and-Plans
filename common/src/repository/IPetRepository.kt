package repository

import DomainError
import arrow.core.Either
import data.PetData
import data.PetImage
import data.PetQuote
import java.util.*

interface IPetRepository {
    fun getAllPets(): Either<DomainError, List<PetData>>
    fun getQuotes(petId: UUID): Either<DomainError, List<PetQuote>>
    fun getImages(petId: UUID): Either<DomainError, List<PetImage>>
    fun getPetByKind(petKind: String): Either<DomainError, PetData>
    fun addPetInPossession(userId: UUID, petId: UUID): Either<DomainError, Unit>
}