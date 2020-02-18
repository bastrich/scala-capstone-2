name := "scala-capstone-2"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.2"
libraryDependencies += "io.vertx" % "vertx-core" % "4.0.0-milestone4"
libraryDependencies += "io.vertx" % "vertx-codegen" % "4.0.0-milestone4"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.9"
libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

libraryDependencies += "io.monix" %% "monix" % "3.1.0"