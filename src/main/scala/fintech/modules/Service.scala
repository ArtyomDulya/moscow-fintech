package fintech.modules

import cats.effect.IO
import cats.effect.kernel.Resource
import fintech.services.{
    HistoriesService,
    SecuritiesHistoriesService,
    SecuritiesService,
    XmlService
}

class Service(
    val historiesService: HistoriesService,
    val securitiesService: SecuritiesService,
    val securitiesHistoriesService: SecuritiesHistoriesService
)

object Service {
    def apply(core: Core): Service = {
        val xmlService        = XmlService()
        val historiesService  = HistoriesService(core.liveHistories, xmlService)
        val securitiesService = SecuritiesService(core.liveSecurities, xmlService)
        val securitiesAndHistoriesService = SecuritiesHistoriesService(
            core.liveSummaryHistoriesSecurities
        )

        new Service(historiesService, securitiesService, securitiesAndHistoriesService)
    }
}
