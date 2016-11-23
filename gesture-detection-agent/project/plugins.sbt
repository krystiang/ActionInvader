resolvers += "Devsupport Nexus Snapshots" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/snapshots"

resolvers += "Devsupport Nexus Internal" at "http://devsupport.informatik.haw-hamburg.de/nexus/content/repositories/releases"

credentials += Credentials(Path.userHome / ".ivy2" / "devsupport-internal.credentials")

addSbtPlugin("net.devsupport.sbt" % "sbt-framework-plugin" % "0.0.2-SNAPSHOT")
