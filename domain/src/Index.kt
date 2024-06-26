import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.right
import data.Id
import model.IndexModel
import repository.IPetRepository
import repository.ITaskRepository
import repository.IUserRepository

context(Id, IUserRepository, IPetRepository, ITaskRepository)
fun getIndex(): Either<DomainError, IndexModel> = either {
    val user = getUserData().bind()
    val tasks = getAllTasks().bind()
    val image = getRandomImage().bind()
    val quote = getRandomQuote().bind()
    IndexModel(
        userData = user,
        petImage = image,
        petQuote = quote,
        tasks = tasks,
    )
}
