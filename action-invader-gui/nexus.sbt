externalResolvers in ThisBuild := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

resolvers in ThisBuild += "Devsupport Nexus Central Proxy" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/central"

resolvers in ThisBuild += "Devsupport Nexus Snapshots" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/snapshots"

resolvers in ThisBuild += "Devsupport Nexus Internal" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/releases"

publishTo in ThisBuild := {
  val nexus = "http://devsupport.informatik.haw-hamburg.de/nexus/content/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "repositories/snapshots")
  else
    Some("releases"  at nexus + "repositories/releases")
}

credentials in ThisBuild += Credentials(Path.userHome / ".ivy2" / "devsupport-internal.credentials")
