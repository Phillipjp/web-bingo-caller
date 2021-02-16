
name := "web-bingo-caller"

version := "0.0.1"

//scalaVersion := "2.12.8"

//enablePlugins(ScalaJSPlugin)



//libraryDependencies ++= Seq(
//  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
//  "org.scala-js" %%% "scalajs-dom" % "1.1.0"
//)

//scalaJSUseMainModuleInitializer := true

lazy val root = project.in(file(".")).
  aggregate(app.js, app.jvm).
  settings(
    publish := {},
    publishLocal := {},
  )

lazy val app = crossProject(JSPlatform, JVMPlatform).in(file(".")).
  enablePlugins(JavaAppPackaging)
  .
  settings(
    name := "foo",
    version := "0.1-SNAPSHOT"
  ).
  jvmSettings(
    // Add JVM-specific settings here
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.8",
      "com.typesafe.akka" %% "akka-stream" % "2.6.8",
      "com.typesafe.akka" %% "akka-http" % "10.2.3"
    )

  ).
  jsSettings(
    // Add JS-specific settings here
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0",
    scalaJSUseMainModuleInitializer := true,
  )

lazy val appJS = app.js
lazy val appJVM = app.jvm.settings(
  (resources in Compile) += (fastOptJS in (appJS, Compile)).value.data
)

//mainClass in Compile := Some("webbingocaller.Server")