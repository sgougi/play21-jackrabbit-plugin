import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play21-jackrabbit-sample-jcrom-app"
  val appVersion      = "1.3.5"

  val appDependencies = Seq(
    	"com.wingnest.play2" % "play21-jackrabbit-plugin_2.10" % "1.3.5",
    	"com.google.inject" % "guice" % "3.0",
//    	"org.jcrom" % "jcrom" % "2.0.1",
    	javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
