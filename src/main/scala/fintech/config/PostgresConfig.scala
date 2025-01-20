package fintech.config

case class PostgresConfig(
    nThreads: Int = 8,
    driver: String = "org.postgresql.Driver",
    url: String = "jdbc:postgresql://localhost:5433/moscow_fintech",
    user: String = "postgres",
    password: String = "12345"
)
