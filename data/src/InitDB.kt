import arrow.core.andThen
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
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

private fun getDataSource(config: Config): DataSource =
    HikariConfig().apply {
        jdbcUrl = config.dbUrl
        username = config.dbUser
        password = config.dbPassword
        driverClassName = config.dbDriver
        connectionTimeout = 30000
        idleTimeout = 60000
        maxLifetime = 90000
        maximumPoolSize = 10
    }.let {
        HikariDataSource(it)
    }

private val createDataSource = ::getConfigFromEnv andThen ::getDataSource

private fun getFlyway(dataSource: DataSource): Flyway = Flyway.configure().dataSource(dataSource).load()

fun initDB(): Database = createDataSource().let { dataSource ->
    getFlyway(dataSource).migrate()
    Database.connect(dataSource)
}
