package fintech.core

import cats.Applicative
import cats.effect.IO
import cats.implicits._
import doobie.FC
import doobie.implicits._
import doobie.postgres.Text.NULL
import doobie.util.transactor.Transactor
import fintech.domain.history.History
import doobie.postgres.implicits._
import doobie.syntax.SqlInterpolator


import java.time.LocalDate

trait Histories {
    def importHistories(historyList: List[History]): IO[List[History]]
    def getAllHistories(): IO[List[History]]
    def deleteHistory(secid: String): IO[History]

}

class LiveHistories private (xa: Transactor[IO]) extends Histories {

    override def importHistories(historyList: List[History]): IO[List[History]] = {
        historyList
            .traverse { history =>

                sql"""
                      INSERT INTO histories(
                      secid,
                      tradedate,
                      open,
                      close
                      ) SELECT
                      ${history.secId },
                      ${history.tradeDate},
                      ${history.open},
                      ${history.close}
                      FROM securities
                      WHERE secid = ${history.secId}
                      ON CONFLICT (secid, tradedate) DO NOTHING
                     """
                    .update
                    .run
                    .map {
                        case 0 => Some(history)
                        case 1 => None
                    }
            }
            .transact(xa)
            .map(_.collect { case Some(history) => history })
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def getAllHistories(): IO[List[History]] = {
        sql"SELECT secid, tradedate, open, close FROM histories"
            .query[History]
            .to[List]
            .transact(xa)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def deleteHistory(secid: String): IO[History] = {
        ???
    }

}

object LiveHistories {
    def apply(xa: Transactor[IO]): LiveHistories = new LiveHistories(xa)
}
