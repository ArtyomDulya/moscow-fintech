package fintech.modules

import cats.effect.kernel.Async
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource, Sync}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import fintech.config.PostgresConfig

object DataBase {
    val postgresConfig = PostgresConfig()
    def makePostgresResource(config: PostgresConfig): Resource[IO, HikariTransactor[IO]] = for {
        ec <- ExecutionContexts.fixedThreadPool(config.nThreads)(Async[IO])
        xa <- HikariTransactor.newHikariTransactor[IO](
            config.driver,
            config.url,
            config.user,
            config.password,
            ec
        )
    } yield xa
}
