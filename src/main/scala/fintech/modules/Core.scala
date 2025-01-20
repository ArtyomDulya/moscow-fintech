package fintech.modules

import cats.effect.IO
import cats.effect.kernel.Resource
import doobie.util.transactor.Transactor
import fintech.core.{Histories, LiveHistories, LiveSecurities, Securities}

class Core private (val liveHistories: Histories, val liveSecurities: Securities)

object Core {
    def apply(xa: Transactor[IO]): Core = {
        val histories  = LiveHistories(xa)
        val securities = LiveSecurities(xa)

        new Core(histories, securities)
    }
}
