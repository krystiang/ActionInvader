name := "ActionInvader-agent"

organization := "net.devsupport.actioninvader.project"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature")
libraryDependencies += "net.devsupport.gesturedetection.api" %% "gesturedetection-api" % "0.0.2-SNAPSHOT"
libraryDependencies += "net.devsupport.actioninvader.api" %% "actioninvader-api" % "0.0.2-SNAPSHOT"
libraryDependencies += "net.devsupport.hci.keyboard" %% "keyboard-api" % "0.0.3-SNAPSHOT"
libraryDependencies += "net.devsupport.framework" %% "framework" % "2.0.0-SNAPSHOT"
