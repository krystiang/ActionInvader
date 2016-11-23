enablePlugins(ScalaJSPlugin)

name := "js-framework"

organization := "net.devsupport.framework"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

testFrameworks += new TestFramework("utest.runner.Framework")

libraryDependencies ++= Seq(
    "net.devsupport.messaging" %%% "message-format" % "1.0.1-SNAPSHOT",
    "net.devsupport.messaging" %%% "message-serialization" % "2.1.0-SNAPSHOT",
    "net.devsupport.messaging" %%% "json-parser" % "0.1.0-SNAPSHOT",
    "net.devsupport.middleware.groups.websocket" %%% "middleware-groups-websocket-api" % "0.1.0-SNAPSHOT",
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "com.lihaoyi" %%% "utest" % "0.3.0" % "test"
)
