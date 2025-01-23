package fintech.core

import cats.effect.IO
import cats.free.Free
import doobie.implicits.toSqlInterpolator
import doobie.implicits._
import doobie.util.transactor.Transactor
import fintech.domain.security.Security
import cats.implicits._


trait Securities {
    def create(security: Security): IO[Int]
    def importSecurities(list: List[Security]): IO[Int]
    def getAllSecurities(): IO[List[Security]]
    def find(secId: String): IO[Option[Security]]
    def update(secId: String, security: Security): IO[Option[Security]]
    def delete(secId: String): IO[Int]

}

class LiveSecurities private (xa: Transactor[IO]) extends Securities {

    override def create(security: Security): IO[Int] = {
        sql"""
              INSERT INTO securities(id, secid, regnumber, name, emitenttitle)
              VALUES (
              ${security.id},
              ${security.secId},
              ${security.regNumber},
              ${security.name},
              ${security.emitentTitle})
             """
            .update
            .withUniqueGeneratedKeys[Int]("id")
            .transact(xa)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def find(secId: String): IO[Option[Security]] = {
        sql"""
            SELECT id, secid, regnumber, name, emitenttitle
            FROM securities
            WHERE secid = $secId
             """
            .query[Security]
            .option
            .transact(xa)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def update(secId: String, security: Security): IO[Option[Security]] = {
        sql"""
              UPDATE securities
              SET
              regnumber = ${security.regNumber},
              name = ${security.name},
              emitenttitle = ${security.emitentTitle}
              WHERE secid = $secId
             """
            .update
            .run
            .transact(xa)
            .flatMap(_ => find(secId))
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def delete(secId: String): IO[Int] = {
        sql"""
            DELETE FROM securities WHERE secid = $secId
             """
            .update
            .run
            .transact(xa)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def importSecurities(security: List[Security]): IO[Int] = {
        security
            .traverse { security =>
               sql"SELECT COUNT(*) FROM securities WHERE secid = ${security.secId}"
                   .query[Int]
                   .unique
                   .flatMap {
                       case 0 =>
                           sql"""
                            INSERT INTO securities(
                            id,
                            secid,
                            regnumber,
                            name,
                            emitenttitle
                            ) VALUES (
                            ${security.id},
                            ${security.secId},
                            ${security.regNumber},
                            ${security.name},
                            ${security.emitentTitle}
                            )
                            """.update.run
                       case _ => Free.pure(0)
                   }
            }
            .transact(xa)
            .map(_.sum)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

    override def getAllSecurities(): IO[List[Security]] = {
        sql"SELECT id, secid, regnumber, name, emitenttitle FROM securities"
            .query[Security]
            .to[List]
            .transact(xa)
            .handleErrorWith { error =>
                IO.println(s"Database error: ${error.getMessage}") *> IO.raiseError(error)
            }
    }

}

object LiveSecurities {
    def apply(xa: Transactor[IO]): LiveSecurities = new LiveSecurities(xa)
}
