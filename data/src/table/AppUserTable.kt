package table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.uuid

internal object AppUserTable : Table<Nothing>("app_user") {
    val id = uuid("id").primaryKey()
    val pet_name = text("pet_name")
    val happiness = int("happiness")
    val coins = int("coins")
    val currentPet = uuid("current_pet")
}