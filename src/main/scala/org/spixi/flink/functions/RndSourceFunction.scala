package org.spixi.flink.functions

import java.util.concurrent.atomic.AtomicBoolean

import org.apache.flink.streaming.api.functions.source.{
  ParallelSourceFunction,
  SourceFunction
}
import org.spixi.flink.models.Numbers

class RndSourceFunction extends ParallelSourceFunction[Numbers] {

  private lazy val rnd = scala.util.Random

  val isRunning: AtomicBoolean = new AtomicBoolean(true)

  override def run(ctx: SourceFunction.SourceContext[Numbers]): Unit = {

    while (isRunning.get()) {
      val nextInt = rnd.nextInt(10000)
      val waitUntil = rnd.nextInt(20000) + 1000

      println(waitUntil)

      ctx.getCheckpointLock.synchronized {
        val toEmit = Numbers(nextInt, System.currentTimeMillis())
        ctx.collect(toEmit)
        Thread.sleep(2000l)
      }
    }
  }

  override def cancel(): Unit = {
    isRunning.set(false)
  }
}
