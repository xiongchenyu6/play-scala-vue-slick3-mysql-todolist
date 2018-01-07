name := """play-scala-vue-slick3-mysql-todolist"""
organization := "kepai.freeman"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, PlayAkkaHttp2Support, JavaAgent, JavaAppPackaging).settings(
  javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.6" % "runtime"
)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  specs2 % Test,
  ehcache,
  guice,
  filters,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
  "mysql" % "mysql-connector-java" % "6.0.6",
  "com.mohiva" %% "play-silhouette" % "5.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.0",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.0" % "test",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x",
  "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B4",
  "com.iheart" %% "ficus" % "1.4.1",
  "org.sangria-graphql" %% "sangria" % "1.3.3",
  "org.sangria-graphql" %% "sangria-play-json" % "1.0.4",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "org.webjars" %% "webjars-play" % "2.6.2",
  "org.webjars.bower" % "compass-mixins" % "0.12.7",
  "org.webjars" % "bootstrap" % "4.0.0-beta.3",
  "org.webjars" % "font-awesome" % "5.0.2"
)
routesImport += "utils.route.Binders._"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
javaOptions ++= Seq(
  "-Dhttp.port=9000",
  "-Dhttps.port=8000",
  //"-Dhttps.keyStore=conf/uniweb.jks",
  //"-Dhttps.keyStorePassword=uniweb",
  "-Dconfig.file=conf/application.conf"
)

fork in Test := true
