package table

import Priority
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.enum
import org.ktorm.schema.text
import org.ktorm.schema.uuid

internal object EventTable : Table<Nothing>("event") {
    val id = uuid("id").primaryKey()
    val name = text("name")
    val description = text("description")
    val priority = enum<Priority>("priority")
    val deadline = datetime("deadline")
    val isCompleted = boolean("isCompleted")
}
