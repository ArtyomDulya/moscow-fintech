package fintech.services

import cats.effect.IO
import fintech.core.SummaryHistoriesSecurities
import fintech.domain.securityHistory.SecurityHistory

class SecuritiesHistoriesService(summaryHistoriesSecurities: SummaryHistoriesSecurities) {
    def getAll(): IO[List[SecurityHistory]] = {
        summaryHistoriesSecurities.getAll()
    }
}

object SecuritiesHistoriesService {
    def apply(summaryHistoriesSecurities: SummaryHistoriesSecurities): SecuritiesHistoriesService =
        new SecuritiesHistoriesService(summaryHistoriesSecurities)
}
