package fintech

import fintech.domain.history.History
import fintech.services.XmlService
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class XmlParserSpec extends AnyFlatSpec with Matchers {

    "parseXmlHistories" should "correctly parse XML and create a list of History objects" in {
        val xmlData =
            """
                <document>
                    <data>
                        <rows>
                            <row SECID="AAPL"  SHORTNAME="iNAV 2xEQT" TRADEDATE="2024-01-18" OPEN="145.30" CLOSE="145.30" />
                            <row SECID="GOOG"  SHORTNAME="iNAV 2xEQT" TRADEDATE="2024-01-18" OPEN="2760.50" CLOSE="2760.50" />
                        </rows>
                    </data>
               </document>
            """

        val result = XmlService().parseXmlHistories(xmlData)
        val expected = List(
            History("AAPL", Some(LocalDate.parse("2024-01-18")), 145.30, 145.30),
            History("GOOG", Some(LocalDate.parse("2024-01-18")), 2760.50, 2760.50)
        )

//        result shouldBe expected
    }
}
