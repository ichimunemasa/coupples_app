name := """sample-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  //"mysql" % "mysql-connector-java" % "5.1.38"
  //"org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.play" %% "play-slick" % "0.8.0"
)
