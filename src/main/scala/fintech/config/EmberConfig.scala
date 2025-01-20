package fintech.config

import com.comcast.ip4s.{Host, IpLiteralSyntax, Port}


final case class EmberConfig(host: Host = host"0.0.0.0", port: Port = port"4041")

