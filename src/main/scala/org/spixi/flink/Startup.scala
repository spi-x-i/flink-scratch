package org.spixi.flink

import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.spixi.flink.functions.{MaxFlatMapFunction, RndSourceFunction}
import org.spixi.flink.models.Numbers

object Startup extends App {

  lazy val rnd = new scala.util.Random()

  println(s"Starting App")

  val see: StreamExecutionEnvironment =
    StreamExecutionEnvironment.getExecutionEnvironment
  see.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
  see.setParallelism(1)
  see.getCheckpointConfig.setCheckpointInterval(10000l)

  val sourceOfNumbersStream = see.addSource(new RndSourceFunction())

  val currentMaxStream: DataStream[Numbers] =
    sourceOfNumbersStream.keyBy(_.key).flatMap(new MaxFlatMapFunction())

  currentMaxStream.writeAsText("out/outcome.txt", WriteMode.OVERWRITE)

  see.execute("checkpoints_test-job")
}
