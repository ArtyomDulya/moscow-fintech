package fintech.domain

import java.time.LocalDate

object securityHistory {
    case class SecurityHistory(
        id: Long,
        secId: String,
        regNumber: Option[String],
        name: String,
        emitentTitle: Option[String],
        tradeDate: Option[LocalDate],
        open: Option[Double],
        close: Option[Double]
    )
}
