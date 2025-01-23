package fintech.http.routes

import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._
import fintech.util.CustomEncoderDecoder._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.dsl.io._
import cats.effect.IO
import fintech.services.{HistoriesService, XmlService, SecuritiesService}

class HistoriesRoutes(
    client: Client[IO],
    historiesService: HistoriesService,
    securitiesService: SecuritiesService
) {

    private val getAllHistoriesRoute: HttpRoutes[IO] = {
        import org.http4s.circe.CirceEntityCodec._
        HttpRoutes.of[IO] { case GET -> Root / "getall" =>
            historiesService.getAllHistories().flatMap { histories =>
                Ok(histories)
            }
        }
    }

    object DateQueryParamMatcher extends QueryParamDecoderMatcher[String]("date")

    val xmlService: XmlService = XmlService()
    private val findHistoriesAllForDateRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
        case GET -> Root / "import" :? DateQueryParamMatcher(date) =>
            val url =
                uri"https://iss.moex.com/iss/history/engines/stock/markets/index/securities.xml"
                    .withQueryParam("date", date)
            client
                .expect[String](url)
                .flatMap { data =>
                    for {
                        failureHistory <- historiesService.importHistories(data)
                        results <- failureHistory.traverse { history =>
                            val secUrl = uri"http://iss.moex.com/iss/securities.xml"
                                .withQueryParam("q", history.secId)
                            client
                                .expect[String](secUrl)
                                .flatMap { secData =>
                                    val securities = xmlService.parseXmlSecurities(secData)

                                    securities match {
                                        case Nil =>
                                            IO.pure((Nil, Some(history)))
                                        case found =>
                                            found.traverse { security =>
                                                securitiesService
                                                    .create(security)
                                                    .void
                                            }
                                    }
                                }
                                .handleErrorWith { error =>
                                    IO.println(
                                        s"Error fetching security for secid=${history.secId}: ${error.getMessage}"
                                    )
                                }
                        }
//                        foundSecurities = results.collect { case (Some(securities), _) => securities}
                        notFoundHistories = results.collect { case (_, Some(history)) => history }
                        historyList <- historiesService.getAllHistories()
                        response <- Ok(
                            historyList.asJson + "\n" + failureHistory.mkString(", ") +
                                "\n" + s"Not Found: ${notFoundHistories.mkString(", ")}"
                        )
                    } yield response
                }
                .handleErrorWith { error =>
                    InternalServerError(s"Error occurred: ${error.getMessage}")
                }
    }

    val routes: HttpRoutes[IO] = Router(
        "/histories/" -> (findHistoriesAllForDateRoute <+> getAllHistoriesRoute)
    )
}

object HistoriesRoutes {
    def apply(
        client: Client[IO],
        historiesService: HistoriesService,
        securitiesService: SecuritiesService
    ): HistoriesRoutes =
        new HistoriesRoutes(client, historiesService, securitiesService)
}
