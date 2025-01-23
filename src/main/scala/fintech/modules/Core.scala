package fintech.modules

import cats.effect.IO
import cats.effect.kernel.Resource
import doobie.util.transactor.Transactor
import fintech.core.{Histories, LiveHistories, LiveSecurities, LiveSummaryHistoriesSecurities, Securities, SummaryHistoriesSecurities}

class Core private (
    val liveHistories: Histories,
    val liveSecurities: Securities,
    val liveSummaryHistoriesSecurities: SummaryHistoriesSecurities
)

object Core {
    def apply(xa: Transactor[IO]): Core = {
        val histories  = LiveHistories(xa)
        val securities = LiveSecurities(xa)
        val summaryHistoriesSecurities = LiveSummaryHistoriesSecurities(xa)

        new Core(histories, securities, summaryHistoriesSecurities)
    }
}
