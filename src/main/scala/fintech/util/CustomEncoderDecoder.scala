package fintech.util

import fintech.domain.history.History
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.syntax._
import fintech.domain.security.Security
import fintech.domain.securityHistory.SecurityHistory
import io.circe.Decoder.Result

import java.time.LocalDate


object CustomEncoderDecoder {

    implicit val encodeSecurity: Encoder[Security] = new Encoder[Security] {
        final def apply(a: Security): Json = Json.obj(
            ("id", a.id.asJson),
            ("secid", a.secId.asJson),
            ("regnumber", a.regNumber.asJson),
            ("name", a.name.asJson),
            ("emitenttitle", a.emitentTitle.asJson)
        )
    }

    implicit val decodeSecurity: Decoder[Security] = new Decoder[Security] {
        final def apply(c: HCursor): Decoder.Result[Security] =
            for {
                id           <- c.downField("id").as[Long]
                secId        <- c.downField("secid").as[String]
                regNumber    <- c.downField("regnumber").as[Option[String]]
                name         <- c.downField("name").as[String]
                emitentTitle <- c.downField("emitenttitle").as[Option[String]]
            } yield {
                Security(id, secId, regNumber, name, emitentTitle)
            }
    }

    implicit val encodeHistory: Encoder[History] = new Encoder[History] {
        override def apply(a: History): Json = Json.obj(
            ("secid", a.secId.asJson),
            ("tradedate", a.tradeDate.asJson),
            ("open", a.open.asJson),
            ("close", a.close.asJson)
        )
    }

    implicit val decodeHistory: Decoder[History] = new Decoder[History] {
        override def apply(c: HCursor): Result[History] =
            for {
                secId     <- c.downField("secid").as[String]
                tradeDate <- c.downField("tradedate").as[Option[LocalDate]]
                open      <- c.downField("open").as[Double]
                close     <- c.downField("close").as[Double]
            } yield {
                History(secId, tradeDate, open, close)
            }
    }

    implicit val encodeSecurityHistory: Encoder[SecurityHistory] = new Encoder[SecurityHistory] {
        override def apply(a: SecurityHistory): Json = Json.obj(
            ("id", a.id.asJson),
            ("secid", a.secId.asJson),
            ("regnumber", a.regNumber.asJson),
            ("name", a.name.asJson),
            ("emitenttitle", a.emitentTitle.asJson),
            ("tradedate", a.tradeDate.asJson),
            ("open", a.open.asJson),
            ("close", a.close.asJson)
        )
    }


}
