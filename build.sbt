import sbt._
import sbt.Keys._

name := "flink-scratch"

scalaVersion in ThisBuild := "2.11.12"
organization := "org.spixi"

resolvers in ThisBuild ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

parallelExecution in Test := false

libraryDependencies ++= Dependencies.flinkDependencies

// make run command include the provided dependencies
run in Compile := Defaults
  .runTask(fullClasspath in Compile,
           mainClass in (Compile, run),
           runner in (Compile, run))
  .evaluated

assemblyOption in assembly := (assemblyOption in assembly).value
  .copy(cacheUnzip = false)
assemblyOption in assembly := (assemblyOption in assembly).value
  .copy(includeScala = false)
assemblyOption in assembly := (assemblyOption in assembly).value
  .copy(cacheOutput = false)

fork in (Compile, run) := true

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") =>
    MergeStrategy.filterDistinctLines
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("org", "slf4j", xs @ _ *) => MergeStrategy.first
  case x => MergeStrategy.first
}

scalafmtOnCompile in ThisBuild := true
