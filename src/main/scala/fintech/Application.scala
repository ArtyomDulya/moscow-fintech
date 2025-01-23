package fintech

import cats.effect.{Async, ExitCode, IO, IOApp, Resource}
import fintech.config.{EmberConfig, PostgresConfig}
import fintech.core.{LiveHistories, LiveSecurities}
import fintech.http.routes.{HistoriesRoutes, SecuritiesRouters}
import fintech.modules.{Core, DataBase, HttpApi, Service}
import fintech.services.{HistoriesService, SecuritiesService, XmlService}
import cats.implicits._
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.typelevel.log4cats.{Logger, LoggerFactory}
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger}

import scala.concurrent.duration.DurationInt

object Application extends IOApp.Simple {

    private val emberConfig    = EmberConfig()
    private val postgresConfig = PostgresConfig()

    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
    private val clientResource                    = EmberClientBuilder.default[IO].build

    private val serverResource: Resource[IO, Server] = clientResource.flatMap { client =>
        for {
            xa <- DataBase.makePostgresResource(postgresConfig)
            core    = Core(xa)
            service = Service(core)
            httpApi = HttpApi(client, service)
            server <- EmberServerBuilder
                .default[IO]
                .withHost(emberConfig.host)
                .withPort(emberConfig.port)
                .withHttpApp(httpApi.endpoints.orNotFound)
                .build
        } yield server
    }

    override def run: IO[Unit] = {
        serverResource.use(_ => IO.println("Start server!") *> IO.never)
    }
}
