package fintech.services

import fintech.domain.history.History
import fintech.domain.security.Security

import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.reflect.runtime.universe.Try

class XmlService private {

    def parseXmlSecurities(xmlData: String): List[Security] = {
        val xml = scala.xml.XML.loadString(xmlData)
        (xml \ "data" \ "rows" \ "row").map { row =>
            val id           = (row \ "@id").text
            val secId        = (row \ "@secid").text
            val regNumber    = (row \ "@regnumber").headOption.map(_.text).filter(_.nonEmpty)
            val name         = (row \ "@name").text
            val emitentTitle = (row \ "@emitent_title").headOption.map(_.text).filter(_.nonEmpty)

            Security(id.toLong, secId, regNumber, name, emitentTitle)
        }
    }.toList

    def parseXmlHistories(xmlData: String): List[History] = {
        val xml = scala.xml.XML.loadString(xmlData)
        (xml \ "data" \ "rows" \ "row").map { row =>
            val secId = (row \ "@SECID").text

            val tradeDateString = (row \ "@TRADEDATE").text
            val formatter       = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val tradeDate =
                if (tradeDateString.isEmpty) None
                else Some(LocalDate.parse(tradeDateString, formatter))
            val openString = (row \ "@OPEN").text
            val open       = if (openString.nonEmpty) openString.toDouble else 0.0

            val closeString = (row \ "@CLOSE").text
            val close       = if (closeString.nonEmpty) closeString.toDouble else 0.0

            History(secId, tradeDate, open, close)
        }
    }.toList

}

object XmlService {
    def apply(): XmlService = new XmlService
}
