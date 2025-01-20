package fintech.http.routes

import io.circe.generic.auto._
import io.circe.syntax._
import fintech.util.CustomEncoderDecoder._
import cats.implicits._
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.dsl.io._
import cats.effect.IO
import cats.effect.IO.{IOCont, Uncancelable}
import fintech.domain.security.{Security, SecurityInfo}
import fintech.http.responses.FailureResponse
import fintech.services.SecuritiesService

class SecuritiesRouters(client: Client[IO], securitiesService: SecuritiesService) {

    val url = uri"https://iss.moex.com/iss/securities.xml"

    private val createSecurityRoute: HttpRoutes[IO] = {
        import org.http4s.circe.CirceEntityCodec._
        HttpRoutes.of[IO] { case req @ POST -> Root / "create" =>
            {
                for {
                    securityInfo <- req.as[SecurityInfo]
                    result       <- securitiesService.createSecurity(securityInfo)
                    response     <- Ok(s"Security created with ID: $result")
                } yield response
            }.handleErrorWith { error =>
                InternalServerError(s"Error occurred: ${error.getMessage}")
            }

        }
    }

    private val findSecurityRoute: HttpRoutes[IO] = {
        import org.http4s.circe.CirceEntityCodec._
        HttpRoutes.of[IO] { case GET -> Root / "find" / secId =>
            securitiesService
                .findSecurity(secId)
                .flatMap {
                    case Some(security) => Ok(security)
                    case None           => NotFound(FailureResponse(s"Job $secId not found"))
                }
                .handleErrorWith { error =>
                    InternalServerError(s"Error occurred: ${error.getMessage}")
                }
        }
    }

    private val updateSecurityRoute: HttpRoutes[IO] = {
        import org.http4s.circe.CirceEntityCodec._
        HttpRoutes.of[IO] { case req @ POST -> Root / "update" / secId =>
            req.as[Security].flatMap { security =>
                securitiesService
                    .findSecurity(secId)
                    .flatMap {
                        case None => NotFound(FailureResponse(s"Job $secId not found"))
                        case Some(_) =>
                            securitiesService
                                .updateSecurity(secId, security) *> Ok(
                                "Changes applied successfully"
                            )
                    }
                    .handleErrorWith { error =>
                        InternalServerError(s"Error occurred: ${error.getMessage}")
                    }
            }
        }
    }

    private val deleteSecurityRoute: HttpRoutes[IO] = {
        import org.http4s.circe.CirceEntityCodec._
        HttpRoutes.of[IO] { case DELETE -> Root / "delete" / secId =>
            securitiesService
                .findSecurity(secId)
                .flatMap {
                    case None => NotFound(FailureResponse(s"Security with secid: $secId not found"))
                    case Some(_) =>
                        securitiesService.deleteSecurity(secId) *> Ok(
                            "Security deleted successfully"
                        )
                }
                .handleErrorWith { error =>
                    InternalServerError(s"Error occurred: ${error.getMessage}")
                }
        }
    }

    private val securitiesGetAllRoute: HttpRoutes[IO] = {
        import org.http4s.circe.CirceEntityCodec._
        HttpRoutes.of[IO] {
            case GET -> Root / "getall" =>
              securitiesService.getAllSecurities().flatMap { securities =>
                  Ok(securities)
              }
        }
    }

    private val importSecuritiesGetAllRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
        case GET -> Root / "import" =>
            client
                .expect[String](url)
                .flatMap { data =>
                    for {
                        _              <- securitiesService.importAllSecurities(data)
                        securitiesList <- securitiesService.getAllSecurities()
                        response       <- Ok(securitiesList.asJson)
                    } yield response
                }
                .handleErrorWith { error =>
                    InternalServerError(s"Error occurred: ${error.getMessage}")
                }
    }

    val crudRoutes =
        createSecurityRoute <+> findSecurityRoute <+> updateSecurityRoute <+> deleteSecurityRoute
    val routes = Router(
        "/securities/" -> (importSecuritiesGetAllRoute <+> securitiesGetAllRoute <+> crudRoutes)
    )
}

object SecuritiesRouters {
    def apply(client: Client[IO], securitiesService: SecuritiesService): SecuritiesRouters =
        new SecuritiesRouters(client, securitiesService)
}
