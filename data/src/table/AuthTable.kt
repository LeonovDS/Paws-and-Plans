package table

import org.ktorm.schema.Table
import org.ktorm.schema.text
import org.ktorm.schema.uuid

internal object AuthTable : Table<Nothing>("Auth") {
    val id = uuid("id").primaryKey()
    val login = text("login")
    val password = text("password")
}
