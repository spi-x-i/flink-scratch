import sbt._

object Dependencies {

  private object flink {
    lazy val namespace = "org.apache.flink"
    lazy val version   = "1.6.2"

    lazy val scala          = namespace %% "flink-scala"           % version % "provided"
    lazy val streamingScala = namespace %% "flink-streaming-scala" % version % "provided"
  }

  lazy val flinkDependencies = Seq(flink.scala, flink.streamingScala)
  
}