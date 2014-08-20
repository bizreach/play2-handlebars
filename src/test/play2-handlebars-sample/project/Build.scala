import sbt._
import Keys._
import play.Play.autoImport._

object ApplicationBuild extends Build {

//  val appOrganization	= "jp.furyu"
//  val appScalaVersion = "2.11.2"
//  val appScalaCrossVersions = Seq(appScalaVersion, "2.9.1")
  // version is defined in version.sbt in order to support sbt-release


  lazy val main = Project("root", file("."))
//    .dependsOn(plugin)
//
//  lazy val plugin = Project("sample-app", file("."))
    .enablePlugins(play.PlayScala)
    .settings(
      scalaVersion := "2.11.2",
//      unmanagedSourceDirectories := Seq(file("src/main/scala")),
//      unmanagedJars in Compile ++= Seq(file("../play2-handlebars/target/scala-2.11/play2-handlebars_2.11-0.0.1.jar")),
      libraryDependencies ++= Seq(
        "jp.co.bizreach" %% "play2-handlebars" % "0.2-SNAPSHOT"
      )
    )

//  lazy val scalaSample = Project("sample-app", file(".")).settings(
//    scalaVersion := "2.11.2"
//  ).dependsOn(plugin)

}
