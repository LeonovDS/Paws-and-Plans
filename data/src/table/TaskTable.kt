package table

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.text
import org.ktorm.schema.uuid

internal object TaskTable : Table<Nothing>("task") {
    val id = uuid("id").primaryKey()
    val userId = uuid("user_id")
    val name = text("name")
    val description = text("description")
    val deadline = datetime("deadline")
    val priority = text("priority")
    val isCompleted = boolean("is_completed")
}