name := "Corgi"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.27",
  "org.scalaz" %% "scalaz-ioeffect" % "2.10.1",
  "org.json4s" %% "json4s-native" % "3.6.6",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "io.shaka" %% "naive-http" % "104"
)
