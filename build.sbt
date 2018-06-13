name := "play2-handlebars"

organization := "jp.co.bizreach"

version := "0.4.1"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.11", "2.12.4")

libraryDependencies ++= Seq(
  "com.typesafe.play"  %% "play"                 % "2.6.7"    % "provided",
  "com.github.jknack"   % "handlebars"           % "4.0.6",
  "com.github.jknack"   % "handlebars-jackson2"  % "4.0.6",
  "org.scalatest"      %% "scalatest"            % "3.0.5"    % "test",
  "org.mockito"         % "mockito-core"         % "2.16.0"   % "test",
  "com.typesafe.play"  %% "play-test"            % "2.6.7"    % "test"
)

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
