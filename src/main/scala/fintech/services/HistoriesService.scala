package fintech.services

import cats.effect.IO
import fintech.core.Histories
import fintech.domain.history.History

class HistoriesService private (histories: Histories, xmlService: XmlService) {

    def importHistories(data: String): IO[List[History]] = {
        val liveHistory = xmlService.parseXmlHistories(data)
        histories.importHistories(liveHistory)
    }

    def getAllHistories(): IO[List[History]] = {
        histories.getAllHistories()
    }
}

object HistoriesService {
    def apply(histories: Histories, xmlService: XmlService): HistoriesService =
        new HistoriesService(histories, xmlService)
}
