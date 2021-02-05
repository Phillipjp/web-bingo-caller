
name := "web-bingo-caller"

version := "0.0.1"

scalaVersion := "2.12.8"

enablePlugins(ScalaJSPlugin)
enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scala-js" %%% "scalajs-dom" % "1.1.0"
)

scalaJSUseMainModuleInitializer := true
