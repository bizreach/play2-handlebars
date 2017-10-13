name := "play2-handlebars"

organization := "jp.co.bizreach"

version := "0.3.1"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.5", "2.11.7")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play"  %% "play"                 % "2.4.2"    % "provided",
  "com.github.jknack"   % "handlebars"           % "1.3.2",
  "com.github.jknack"   % "handlebars-jackson2"  % "1.3.2",
  "org.scalatest"      %% "scalatest"            % "2.2.4"    % "test",
  "org.mockito"         % "mockito-all"          % "1.9.5"    % "test",
  "com.typesafe.play"  %% "play-test"            % "2.4.2"    % "test"
)

publishMavenStyle := true

publishTo := {
  if (isSnapshot.value)
    Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
  else
    Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

scalacOptions := Seq("-deprecation")

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

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
