import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play21-jackrabbit-plugin"
  val appVersion      = "1.3.4"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "javax.jcr" % "jcr" % "2.0",
    "org.apache.jackrabbit" % "jackrabbit-core" % "2.7.0",
    "org.apache.jackrabbit" % "jackrabbit-jcr-rmi" % "2.7.0",
    "org.jcrom" % "jcrom" % "2.1.0",    
    "org.apache" % "jackrabbit-ocm" % "2.0.0",
    javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    publishArtifact in(Compile, packageDoc) := false,
    organization := "com.wingnest.play2",
    resolvers += "temporary repo" at "https://oss.sonatype.org/content/repositories/releases/"
  )

}
