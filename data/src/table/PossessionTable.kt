package table

import org.ktorm.schema.Table
import org.ktorm.schema.uuid

internal object PossessionTable : Table<Nothing>("possession") {
    val id = uuid("id").primaryKey()
    val ownerId = uuid("owner_id")
    val petId = uuid("pet_id")
}