name := "ramble"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.21"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.github.Ma27" %% "rediscala" % "1.8.4",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe" % "config" % "1.3.3",
  "com.typesafe.play" %% "play-json" % "2.7.2",
)
