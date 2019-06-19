name := "play2-handlebars"

organization := "jp.co.bizreach"

version := "0.5.0-SNAPSHOT"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.11.12", scalaVersion.value, "2.13.0-M5")

val playVersion = "2.7.2"
val handlebarsVersion = "4.1.2"

libraryDependencies ++= Seq(
  "com.typesafe.play"  %% "play"                 % playVersion   % "provided",
  "com.github.jknack"   % "handlebars"           % handlebarsVersion,
  "com.github.jknack"   % "handlebars-jackson2"  % handlebarsVersion,
  "org.scalatest"      %% "scalatest"            % "3.0.7"       % "test",
  "com.typesafe.play"  %% "play-test"            % playVersion   % "test"
)

parallelExecution in Test := false

publishMavenStyle := true

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

scalacOptions := Seq("-deprecation")

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

sonatypeProfileName := "jp.co.bizreach"

pomExtra := (
  <url>https://github.com/bizreach/play2-handlebars</url>
    <licenses>
      <license>
        <name>The Apache Software License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <url>https://github.com/bizreach/play2-handlebars</url>
      <connection>scm:git:https://github.com/bizreach/play2-handlebars.git</connection>
    </scm>
    <developers>
      <developer>
        <id>scova0731</id>
        <name>Satoshi Kobayashi</name>
        <email>satoshi.kobayashi_at_bizreach.co.jp</email>
        <timezone>+9</timezone>
      </developer>
    </developers>)
