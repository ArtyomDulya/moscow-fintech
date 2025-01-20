package fintech.core

import cats.effect.IO
import cats.free.Free
import doobie.implicits.toSqlInterpolator
import doobie.implicits._
import doobie.util.transactor.Transactor
import fintech.domain.security.{Security, SecurityInfo}
import cats.implicits._


trait Securities {
    def create(securityInfo: SecurityInfo): IO[Int]
    def importSecurities(list: List[Security]): IO[Int]
    def getAllSecurities(): IO[List[Security]]
    def find(secId: String): IO[Option[Security]]
    def update(secId: String, security: Security): IO[Option[Security]]
    def delete(secId: String): IO[Int]

}

class LiveSecurities private (xa: Transactor[IO]) extends Securities {

    override def create(securityInfo: SecurityInfo): IO[Int] = {
        sql"""
              INSERT INTO securities(secid, regnumber, name, emitenttitle)
              VALUES (${securityInfo.secId}, ${securityInfo.regNumber}, ${securityInfo.name}, ${securityInfo.emitentTitle})
             """
            .update
            .withUniqueGeneratedKeys[Int]("id")
            .transact(xa)
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
    }

    override def update(secId: String, security: Security): IO[Option[Security]] = {
        sql"""
              UPDATE securities
              SET
              regnumber = ${security.securityInfo.regNumber},
              name = ${security.securityInfo.name},
              emitenttitle = ${security.securityInfo.emitentTitle}
              WHERE secid = $secId
             """
            .update
            .run
            .transact(xa)
            .flatMap(_ => find(secId))
    }

    override def delete(secId: String): IO[Int] = {
        sql"""
            DELETE FROM securities WHERE secid = $secId
             """
            .update
            .run
            .transact(xa)
    }

    override def importSecurities(security: List[Security]): IO[Int] = {
        security
            .traverse { security =>
               sql"SELECT COUNT(*) FROM securities WHERE secid = ${security.securityInfo.secId}"
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
                            ${security.securityInfo.secId},
                            ${security.securityInfo.regNumber},
                            ${security.securityInfo.name},
                            ${security.securityInfo.emitentTitle}
                            )
                            """.update.run
                       case _ => Free.pure(0)
                   }
            }
            .transact(xa)
            .map(_.sum)
    }

    override def getAllSecurities(): IO[List[Security]] = {
        sql"SELECT id, secid, regnumber, name, emitenttitle FROM securities"
            .query[Security]
            .to[List]
            .transact(xa)
    }

}

object LiveSecurities {
    def apply(xa: Transactor[IO]): LiveSecurities = new LiveSecurities(xa)
}
