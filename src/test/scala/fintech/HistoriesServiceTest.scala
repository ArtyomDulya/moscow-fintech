package fintech

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.scalatest.IOChecker
import doobie.util.testing.UnsafeRun
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import fintech.core.LiveHistories
import fintech.domain.history.History
import org.mockito.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class HistoriesServiceTest {


}


