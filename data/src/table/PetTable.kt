package table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar

internal object PetTable : Table<Nothing>("pet") {
    val id = uuid("id").primaryKey()
    val kind = varchar("kind")
    val kindTranslation = varchar("kind_translation")
    val price = int("price")
}