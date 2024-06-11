package domain

import DomainError
import IndexModel
import PetModel
import PetRepository
import TaskModel
import TaskRepository
import arrow.core.Either
import arrow.core.raise.either

fun getIndexModel(petRepository: PetRepository, taskRepository: TaskRepository): Either<DomainError, IndexModel> =
    either {
        IndexModel(
            PetModel.from(petRepository.getPet().bind()),
            taskRepository.getTasks().bind().map { TaskModel.from(it) },
            "Hello World",
        )
    }