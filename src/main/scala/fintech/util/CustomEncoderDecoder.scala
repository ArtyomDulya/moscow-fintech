package fintech.util

import cats.Contravariant.ops.toAllContravariantOps
import cats.ContravariantMonoidal.ops.toAllContravariantMonoidalOps
import cats.ContravariantSemigroupal.ops.toAllContravariantSemigroupalOps
import cats.effect.IO
import cats.implicits.{catsSyntaxApplicativeId, toContravariantOps}
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor, Json, KeyDecoder}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.semiauto._
import fintech.domain.security.{Security, SecurityInfo}
import org.http4s.{EntityDecoder, EntityEncoder, MediaType}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.headers.`Content-Type`

object CustomEncoderDecoder {

    implicit val decodeSecurity: Decoder[Security] = new Decoder[Security] {
        final def apply(c: HCursor): Decoder.Result[Security] =
            for {
                id           <- c.downField("id").as[Long]
                securityInfo <- c.downField("securityInfo").as[SecurityInfo]
            } yield {
                Security(id, securityInfo)
            }
    }

    implicit val encodeSecurity: Encoder[Security] = new Encoder[Security] {
        final def apply(a: Security): Json = Json.obj(
            ("id", a.id.asJson),
            ("securityInfo", a.securityInfo.asJson)
        )
    }

    implicit val encodeSecurityInfo: Encoder[SecurityInfo] = new Encoder[SecurityInfo] {
        final def apply(a: SecurityInfo): Json = Json.obj(
            ("secid", a.secId.asJson),
            ("regnumber", a.regNumber.asJson),
            ("name", a.name.asJson),
            ("emitenttitle", a.emitentTitle.asJson)
        )
    }

    implicit val decodeSecurityInfo: Decoder[SecurityInfo] = new Decoder[SecurityInfo] {
        final def apply(c: HCursor): Decoder.Result[SecurityInfo] =
            for {
                secid <- c.downField("secid").as[String]
                regnumber <- c.downField("regnumber").as[Option[String]]
                name <- c.downField("name").as[String]
                emitenttitle <- c.downField("emitenttitle").as[Option[String]]
            } yield {
                SecurityInfo(secid, regnumber, name, emitenttitle)
            }
    }
}
