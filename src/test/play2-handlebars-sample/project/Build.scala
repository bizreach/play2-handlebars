import sbt._
import Keys._
import play.Play
import com.typesafe.sbt.web.SbtWeb.autoImport._

object ApplicationBuild extends Build {

  // This code derives from play.PlayCommands trait
  // To skip unexpected reloading when static files and template files change
  // This unexpected phenomenon has started happening since unmanagedResourceDirectories is added.
  // Here, resources are removed from the original code
  val playMonitoredFilesTask = (thisProjectRef, state) map { (ref, state) =>
    val src = Play.inAllDependencies(ref, unmanagedSourceDirectories in Compile, Project structure state).foldLeft(Seq.empty[File])(_ ++ _)
    val assets = Play.inAllDependencies(ref, unmanagedSourceDirectories in Assets, Project structure state).foldLeft(Seq.empty[File])(_ ++ _)
    val public = Play.inAllDependencies(ref, unmanagedResourceDirectories in Assets, Project structure state).foldLeft(Seq.empty[File])(_ ++ _)
    (src ++ assets ++ public).map { f =>
      if (!f.exists) f.mkdirs(); f
    }.map(_.getCanonicalPath).distinct
  }

  lazy val main = Project("root", file("."))
    .enablePlugins(play.PlayScala)
    .settings(
      scalaVersion := "2.11.2",

      // To include in the class path
      unmanagedResourceDirectories in Compile += baseDirectory.value / "resources",

      resolvers ++= Seq(
        // When to read SNAPSHOT versions, add this line.
        "Maven Central Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
      ),

      libraryDependencies ++= Seq(
        "jp.co.bizreach" %% "play2-handlebars" % "0.2-SNAPSHOT"
      )
    )


}
