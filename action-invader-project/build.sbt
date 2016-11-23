scalaVersion in ThisBuild := "2.11.6"

lazy val root = project
	.in(file("."))
	.settings(publish := {}, publishLocal := {})
	.aggregate(api, agent)

lazy val api = project in file("ActionInvader-api")

lazy val agent = project in file("ActionInvader-agent") dependsOn api
