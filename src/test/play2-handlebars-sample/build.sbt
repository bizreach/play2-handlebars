name := """play2-handlebars-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  ehcache,
  ws,

  "jp.co.bizreach" %% "play2-handlebars" % "0.5.0-SNAPSHOT",

  specs2 % Test
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
