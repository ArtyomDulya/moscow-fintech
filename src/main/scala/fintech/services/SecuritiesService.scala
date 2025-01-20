package fintech.services

import cats.effect.IO
import fintech.core.{LiveSecurities, Securities}
import fintech.domain.security.{Security, SecurityInfo}

class SecuritiesService private (securities: Securities, xmlService: XmlService) {

    def createSecurity(securityInfo: SecurityInfo): IO[Int] = {
        securities.create(securityInfo)
    }

    def findSecurity(secId: String): IO[Option[Security]] = {
        securities.find(secId)
    }

    def updateSecurity(secId: String, security: Security): IO[Option[Security]] = {
        securities.update(secId, security)
    }

    def deleteSecurity(secId: String): IO[Int] = {
        securities.delete(secId)
    }

    def importAllSecurities(data: String): IO[Int] = {
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
