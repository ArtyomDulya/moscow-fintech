package fintech.core

import cats.effect.IO
import doobie.implicits._
import cats.implicits._
import doobie.util.transactor.Transactor
import fintech.domain.securityHistory.SecurityHistory
import doobie.postgres.implicits._

trait SummaryHistoriesSecurities {
    def getAll(): IO[List[SecurityHistory]]
}

class LiveSummaryHistoriesSecurities private (xa: Transactor[IO]) extends SummaryHistoriesSecurities {
    override def getAll(): IO[List[SecurityHistory]] = {
        sql"""
             SELECT
             s.id,
             s.secid,
             s.regnumber,
             s.name,
             s.emitenttitle,
             h.tradedate,
             h.open,
             h.close
             FROM securities s
             LEFT JOIN histories h
             ON s.secid = h.secid
             """
            .query[SecurityHistory]
            .to[List]
            .transact(xa)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }
}

object LiveSummaryHistoriesSecurities {
    def apply(xa: Transactor[IO]): LiveSummaryHistoriesSecurities = new LiveSummaryHistoriesSecurities(xa)
}

