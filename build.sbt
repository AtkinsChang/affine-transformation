lazy val default = Seq(
  organization := "edu.nccu.plsm.geo",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8",
    "-explaintypes",
    "-feature",
    "-optimise",
    "-encoding", "UTF-8",
    "-Yrangepos",
    "-Yinline-warnings",
    "-g:vars",
    "-Xcheckinit",
    "-Xlint",
    "-Xlog-reflective-calls",
    "-Xprint-types",
    //"-Xstrict-inference",
    "-Xverify"
  ),
  javacOptions ++= Seq(
    "-source", "1.8",
    "-target", "1.8",
    "-encoding", "UTF-8",
    "-Xlint",
    //"-J-Xmx512m",
    "-g:vars",
    "-deprecation"
  ),
  javacOptions in doc := Seq(
    "-source", "1.8"
  ),
  scalacOptions in Test ++= Seq(
    "-Yrangepos"
  ),
  fork in Test := true
)

lazy val root = (project in file("."))
  .settings(default: _*)
  .settings(
    name := "affine-transformation",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "ch.qos.logback" % "logback-core" % "1.1.3",
      "org.specs2" %% "specs2-core" % "3.6" % "test"
    ),
    logLevel := Level.Debug
  )