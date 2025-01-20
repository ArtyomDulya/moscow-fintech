package fintech.modules

import cats.implicits._
import cats.effect.IO
import fintech.http.routes.{HistoriesRoutes, SecuritiesRouters}
import org.http4s.client.Client
import org.http4s.server.Router


class HttpApi private (client: Client[IO], service: Service) {
    private val historiesRoutes = HistoriesRoutes(client, service.historiesService).routes
    private val securitiesRouters = SecuritiesRouters(client, service.securitiesService).routes

    val endpoints = Router(
       "/api/v1/" ->  (historiesRoutes <+> securitiesRouters)
    )
}

object HttpApi {
    def apply(client: Client[IO], service: Service): HttpApi = new HttpApi(client, service)
}
