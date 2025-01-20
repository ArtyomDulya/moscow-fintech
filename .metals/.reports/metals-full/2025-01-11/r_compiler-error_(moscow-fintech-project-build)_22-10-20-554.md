file://<WORKSPACE>/build.sbt
### scala.reflect.internal.MissingRequirementError: class scala.Serializable in compiler mirror not found.

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.20
Classpath:

Options:
-deprecation -Wconf:cat=unused-nowarn:s -Wconf:cat=unused-nowarn:s -Xsource:3 -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 827
uri: file://<WORKSPACE>/build.sbt
text:
```scala
import _root_.scala.xml.{TopScope=>$scope}
import _root_.sbt._
import _root_.sbt.Keys._
import _root_.sbt.nio.Keys._
import _root_.sbt.ScriptedPlugin.autoImport._, _root_.sbt.plugins.JUnitXmlReportPlugin.autoImport._, _root_.sbt.plugins.MiniDependencyTreePlugin.autoImport._, _root_.bloop.integrations.sbt.BloopPlugin.autoImport._
import _root_.sbt.plugins.IvyPlugin, _root_.sbt.plugins.JvmPlugin, _root_.sbt.plugins.CorePlugin, _root_.sbt.ScriptedPlugin, _root_.sbt.plugins.SbtPlugin, _root_.sbt.plugins.SemanticdbPlugin, _root_.sbt.plugins.JUnitXmlReportPlugin, _root_.sbt.plugins.Giter8TemplatePlugin, _root_.sbt.plugins.MiniDependencyTreePlugin, _root_.bloop.integrations.sbt.BloopPlugin
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

val catsVersion       = "2.9.0"
val http4sVersion     = "@@1.0.0-M38"
val catsEffectVersion = "3.5.7"
val circeVersion      = "0.14.10"
val doobieVersion     = "1.0.0-RC4"
val pureConfigVersion = "0.17.1"
val slf4jVersion      = "2.0.9"


lazy val root = (project in file("."))
    .settings(
        name := "moscow-fintech-project",
        libraryDependencies ++= Seq(
            "org.typelevel" %% "cats-core"           % catsVersion,
            "org.typelevel" %% "cats-effect"         % catsEffectVersion,
            "org.http4s"    %% "http4s-ember-client" % http4sVersion,
            "org.http4s"    %% "http4s-ember-server" % http4sVersion,
            "org.http4s"    %% "http4s-dsl"          % http4sVersion,
            "io.circe"      %% "circe-core"          % circeVersion,
            "io.circe"      %% "circe-generic"       % circeVersion,
            "io.circe"      %% "circe-parser"        % circeVersion,
            "org.tpolecat"  %% "doobie-core"         % doobieVersion,
            "org.tpolecat"  %% "doobie-hikari"       % doobieVersion,
            "org.tpolecat"  %% "doobie-postgres"     % doobieVersion,
            "org.slf4j"      % "slf4j-simple"        % slf4jVersion,
            "org.http4s"    %% "http4s-scala-xml"    % http4sVersion
        )
    )

```



#### Error stacktrace:

```
scala.reflect.internal.MissingRequirementError$.signal(MissingRequirementError.scala:24)
	scala.reflect.internal.MissingRequirementError$.notFound(MissingRequirementError.scala:25)
	scala.reflect.internal.Mirrors$RootsBase.$anonfun$getModuleOrClass$5(Mirrors.scala:61)
	scala.reflect.internal.Mirrors$RootsBase.getRequiredClass(Mirrors.scala:61)
	scala.reflect.internal.Mirrors$RootsBase.requiredClass(Mirrors.scala:121)
	scala.reflect.internal.Definitions$DefinitionsClass.SerializableClass$lzycompute(Definitions.scala:409)
	scala.reflect.internal.Definitions$DefinitionsClass.SerializableClass(Definitions.scala:409)
	scala.reflect.internal.Definitions$DefinitionsClass.isPossibleSyntheticParent$lzycompute(Definitions.scala:1460)
	scala.reflect.internal.Definitions$DefinitionsClass.isPossibleSyntheticParent(Definitions.scala:1460)
	scala.tools.nsc.typechecker.Typers$Typer.fixDuplicateSyntheticParents(Typers.scala:1703)
	scala.tools.nsc.typechecker.Typers$Typer.typedParentTypes(Typers.scala:1713)
	scala.tools.nsc.typechecker.Namers$Namer.templateSig(Namers.scala:1150)
	scala.tools.nsc.typechecker.Namers$Namer.moduleSig(Namers.scala:1251)
	scala.tools.nsc.typechecker.Namers$Namer.memberSig(Namers.scala:1922)
	scala.tools.nsc.typechecker.Namers$Namer.typeSig(Namers.scala:1870)
	scala.tools.nsc.typechecker.Namers$Namer$MonoTypeCompleter.completeImpl(Namers.scala:877)
	scala.tools.nsc.typechecker.Namers$LockingTypeCompleter.complete(Namers.scala:2082)
	scala.tools.nsc.typechecker.Namers$LockingTypeCompleter.complete$(Namers.scala:2080)
	scala.tools.nsc.typechecker.Namers$TypeCompleterBase.complete(Namers.scala:2075)
	scala.reflect.internal.Symbols$Symbol.completeInfo(Symbols.scala:1542)
	scala.reflect.internal.Symbols$Symbol.info(Symbols.scala:1514)
	scala.reflect.internal.Symbols$Symbol.initialize(Symbols.scala:1698)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5442)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5817)
	scala.tools.nsc.typechecker.Typers$Typer.typedStat$1(Typers.scala:5881)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedStats$10(Typers.scala:3356)
	scala.tools.nsc.typechecker.Typers$Typer.typedStats(Typers.scala:3356)
	scala.tools.nsc.typechecker.Typers$Typer.typedPackageDef$1(Typers.scala:5449)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5741)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5817)
	scala.tools.nsc.typechecker.Analyzer$typerFactory$TyperPhase.apply(Analyzer.scala:114)
	scala.tools.nsc.Global$GlobalPhase.applyPhase(Global.scala:465)
	scala.tools.nsc.interactive.Global$TyperRun.$anonfun$applyPhase$1(Global.scala:1341)
	scala.tools.nsc.interactive.Global$TyperRun.applyPhase(Global.scala:1341)
	scala.tools.nsc.interactive.Global$TyperRun.typeCheck(Global.scala:1334)
	scala.tools.nsc.interactive.Global.typeCheck(Global.scala:666)
	scala.meta.internal.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:22)
	scala.meta.internal.pc.WithSymbolSearchCollector.<init>(PcCollector.scala:348)
	scala.meta.internal.pc.PcDocumentHighlightProvider.<init>(PcDocumentHighlightProvider.scala:12)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$documentHighlight$1(ScalaPresentationCompiler.scala:510)
```
#### Short summary: 

scala.reflect.internal.MissingRequirementError: class scala.Serializable in compiler mirror not found.