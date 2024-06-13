import org.ktorm.database.Database
import repository.*

class DbRepository(
    private val db: Database,
    private val authRepository: IAuthRepository = AuthRepository(db),
    private val userRepository: IUserRepository = UserRepository(db),
    private val petRepository: IPetRepository = PetRepository(db),
    private val taskRepository: ITaskRepository = TaskRepository(db),
) : IAuthRepository by authRepository, IUserRepository by userRepository, IPetRepository by petRepository,
    ITaskRepository by taskRepository, IMultiRepository