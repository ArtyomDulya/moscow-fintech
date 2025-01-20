package fintech.domain

import java.time.LocalDate


object history {
    case class History(secId: String, tradeDate: Option[LocalDate], open: Double, close: Double)
}





// Автогенерация энкодера/декодера для класса History
