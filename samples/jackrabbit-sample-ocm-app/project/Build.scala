import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play21-jackrabbit-sample-ocm-app"
  val appVersion      = "1.3.3"

  val appDependencies = Seq(
    	"com.wingnest.play2" % "play21-jackrabbit-plugin_2.10" % "1.3.3",
    	"com.google.inject" % "guice" % "3.0", 
    	javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
