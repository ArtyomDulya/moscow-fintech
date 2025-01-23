package fintech.http.routes

import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec._
import fintech.util.CustomEncoderDecoder._
import org.http4s.dsl.io._
import cats.effect.IO
import fintech.services.SecuritiesHistoriesService
import org.http4s.HttpRoutes
import org.http4s.server.Router

class SecuritiesHistoriesRoutes(securitiesAndHistoriesService: SecuritiesHistoriesService) {

    private val getAllSecuritiesAndHistoriesRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
        case GET -> Root / "getall" =>
            securitiesAndHistoriesService.getAll().flatMap { summary =>
                Ok(summary)
            }
    }

    val routes = Router(
        "/summary/" -> getAllSecuritiesAndHistoriesRoute
    )
}

object SecuritiesHistoriesRoutes {
    def apply(securitiesAndHistoriesService: SecuritiesHistoriesService): SecuritiesHistoriesRoutes =
        new SecuritiesHistoriesRoutes(securitiesAndHistoriesService)
}
