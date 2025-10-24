name := """jac"""
organization := "com.alk"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
libraryDependencies += ("org.mongodb.scala" %% "mongo-scala-driver" % "5.6.1").cross(CrossVersion.for3Use2_13)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.alk.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.alk.binders._"
