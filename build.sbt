name := """infinity"""
organization := "com.dundermifflin"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.12.0"
libraryDependencies += "org.owasp.encoder" % "encoder" % "1.2.3"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.dundermifflin.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.dundermifflin.binders._"
