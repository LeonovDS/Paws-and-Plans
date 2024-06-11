package table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.uuid

internal object PetImageTable : Table<Nothing>("pet_image") {
    val id = uuid("id").primaryKey()
    val pet_id = uuid("pet_id")
    val url = text("url")
    val minHappiness = int("min_happiness")
    val maxHappiness = int("max_happiness")
}