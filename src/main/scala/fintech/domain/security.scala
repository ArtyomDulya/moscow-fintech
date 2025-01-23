package fintech.domain


object security {
    case class Security(
        id: Long,
        secId: String,
        regNumber: Option[String],
        name: String,
        emitentTitle: Option[String]
    )
}
