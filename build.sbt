ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

val catsVersion       = "2.9.0"
val http4sVersion     = "1.0.0-M44"
val catsEffectVersion = "3.5.7"
val circeVersion      = "0.14.10"
val doobieVersion     = "1.0.0-RC4"
val pureConfigVersion = "0.17.1"
val slf4jVersion      = "2.7.0"
val scalaXmlVersion   = "2.2.0"

lazy val root = (project in file("."))
    .settings(
        name := "moscow-fintech-project",
        libraryDependencies ++= Seq(
            "org.typelevel"          %% "cats-core"           % catsVersion,
            "org.typelevel"          %% "cats-effect"         % catsEffectVersion,
            "org.http4s"             %% "http4s-ember-client" % http4sVersion,
            "org.http4s"             %% "http4s-ember-server" % http4sVersion,
            "org.http4s"             %% "http4s-dsl"          % http4sVersion,
            "org.http4s"             %% "http4s-circe"        % http4sVersion,
            "io.circe"               %% "circe-core"          % circeVersion,
            "io.circe"               %% "circe-generic"       % circeVersion,
            "io.circe"               %% "circe-parser"        % circeVersion,
            "org.tpolecat"           %% "doobie-core"         % doobieVersion,
            "org.tpolecat"           %% "doobie-hikari"       % doobieVersion,
            "org.tpolecat"           %% "doobie-postgres"     % doobieVersion,
            "org.typelevel"          %% "log4cats-slf4j"      % slf4jVersion,
            "org.scala-lang.modules" %% "scala-xml"           % scalaXmlVersion,
            "org.scalactic"          %% "scalactic"           % "3.2.19",
            "org.scalatest"          %% "scalatest"           % "3.2.19" % "test",
            "org.mockito"             % "mockito-scala_2.13"  % "1.17.37",
            "org.tpolecat"           %% "doobie-scalatest"    % "0.9.2"  % Test
        )
    )
