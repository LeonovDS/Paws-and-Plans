package table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text

internal object PriorityTable : Table<Nothing>("priority") {
    val name = text("name").primaryKey()
    val award = int("award")
}