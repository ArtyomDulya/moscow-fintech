package fintech.services

import cats.effect.IO
import fintech.core.{LiveSecurities, Securities}
import fintech.domain.security.Security

class SecuritiesService private (securities: Securities, xmlService: XmlService) {

    def create(security: Security): IO[Int] = {
        securities.create(security)
    }

    def find(secId: String): IO[Option[Security]] = {
        securities.find(secId)
    }

    def update(secId: String, security: Security): IO[Option[Security]] = {
        securities.update(secId, security)
    }

    def delete(secId: String): IO[Int] = {
        securities.delete(secId)
    }

    def importSecurities(data: String): IO[Int] = {
        val listSecurity = xmlService.parseXmlSecurities(data)
        securities.importSecurities(listSecurity)

    }

    def getAllSecurities(): IO[List[Security]] = {
        securities.getAllSecurities()
    }
}

object SecuritiesService {
    def apply(securities: Securities, xmlService: XmlService): SecuritiesService =
        new SecuritiesService(securities, xmlService)
}
