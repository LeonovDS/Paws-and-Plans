import arrow.core.andThen
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import java.sql.DriverManager
import javax.sql.DataSource

private data class Config(
    val dbUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val dbDriver: String,
)

private fun getConfigFromEnv() = Config(
    dbUrl = System.getenv("DB_URL"),
    dbUser = System.getenv("DB_USER"),
    dbPassword = System.getenv("DB_PASSWORD"),
    dbDriver = "org.postgresql.Driver",
)

private fun getFlyway(config: Config): Flyway = Flyway
    .configure()
    .dataSource(config.dbUrl, config.dbUser, config.dbPassword)
    .load()

fun initDB(): Database {
    val config = getConfigFromEnv()
//    getFlyway(config).migrate()
    return Database.connect(
        url = config.dbUrl,
        driver = config.dbDriver,
        user = config.dbUser,
        password = config.dbPassword
    )
}
