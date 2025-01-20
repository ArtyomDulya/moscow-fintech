package fintech.http.routes

import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.dsl.io._
import cats.effect.IO
import fintech.services.{HistoriesService, XmlService}

class HistoriesRoutes(client: Client[IO], historiesService: HistoriesService) {
    val url =
        uri"https://iss.moex.com/iss/history/engines/stock/markets/index/securities.xml?date=2024-11-22"

    val xmlService: XmlService = XmlService()
    private val findHistoriesAllForDateRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
        case GET -> Root / "all" =>
            client
                .expect[String](url)
                .flatMap { data =>
                    for {
                        failureHistory <- historiesService.importAllHistories(data)
                        historyList    <- historiesService.getAllHistories()
                        response       <- Ok(historyList.asJson + "   " + failureHistory.mkString(", "))
                    } yield response
                }
                .handleErrorWith { error =>
                    InternalServerError(s"Error occurred: ${error.getMessage}")
                }
    }

    val routes: HttpRoutes[IO] = Router(
        "/histories/" -> findHistoriesAllForDateRoute
    )
}

object HistoriesRoutes {
    def apply(client: Client[IO], historiesService: HistoriesService): HistoriesRoutes =
        new HistoriesRoutes(client, historiesService)
}
