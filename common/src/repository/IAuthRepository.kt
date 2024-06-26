package repository

import DomainError
import SignInData
import SignUpData
import arrow.core.Either
import java.util.UUID

interface IAuthRepository {
    fun createAuthData(data: SignUpData): Either<DomainError, UUID>
    fun getAuthId(data: SignInData): Either<DomainError, UUID>
}