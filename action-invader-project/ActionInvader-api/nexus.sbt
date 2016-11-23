externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

resolvers += "Devsupport Nexus Central Proxy" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/central/"

resolvers += "Devsupport Nexus Snapshots" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/snapshots"

resolvers += "Devsupport Nexus Internal" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/releases"

publishTo := {
  val nexus = "http://devsupport.informatik.haw-hamburg.de/nexus/content/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "repositories/snapshots")
  else
    Some("releases"  at nexus + "repositories/releases")
}

credentials += Credentials(Path.userHome / ".ivy2" / "devsupport-internal.credentials")
