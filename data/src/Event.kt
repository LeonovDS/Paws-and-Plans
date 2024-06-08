import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import table.EventTable
import java.util.*

context(Database)
private fun insertEvent(eventData: EventData): UUID? =
    insertAndGenerateKey(EventTable) {
        set(it.name, eventData.name)
        set(it.description, eventData.description)
        set(it.priority, eventData.priority)
        set(it.deadline, eventData.deadline)
        set(it.isCompleted, eventData.isCompleted)
    } as? UUID

context(Database)
fun createEvent(eventData: EventData): Either<DomainError, UUID> =
    insertEvent(eventData)
        ?.right() ?: NotFound.left()
