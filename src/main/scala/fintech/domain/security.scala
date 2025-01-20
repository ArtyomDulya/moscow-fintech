package fintech.domain


object security {
    case class Security(
        id: Long,
        securityInfo: SecurityInfo
    )

    case class SecurityInfo(
        secId: String,
        regNumber: Option[String],
        name: String,
        emitentTitle: Option[String]
    )
}
