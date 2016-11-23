name := "GestureDetection-api"

organization := "net.devsupport.gesturedetection.api"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies += "net.devsupport.serialization" %% "macro-serialization" % "0.2.0-SNAPSHOT"
