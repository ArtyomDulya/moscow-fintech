error id: file://<WORKSPACE>/src/main/scala/fintech/modules/Core.scala:[186..193) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/fintech/modules/Core.scala", "package fintech.modules

import cats.effect.{Async, MonadCancelThrow, Resource, Sync}
import cats.implicits._
import doobie.util.transactor.Transactor
import fintech.core._

final class private (
    val securities: LiveSecurities
)
// postgres -> security -> core -> httpApi -> App

object Core {
    def apply(xa: Transactor[F]): Resource[IO, Core] = {
        val coreF = for {
            securities <- LiveSecurities[F](xa)

        } yield new Core(securities)

        Resource.eval(coreF)

    }

}
")
file://<WORKSPACE>/file:<WORKSPACE>/src/main/scala/fintech/modules/Core.scala
file://<WORKSPACE>/src/main/scala/fintech/modules/Core.scala:8: error: expected identifier; obtained private
final class private (
            ^
#### Short summary: 

expected identifier; obtained private