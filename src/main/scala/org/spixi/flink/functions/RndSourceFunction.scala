package org.spixi.flink.functions

import java.util.concurrent.atomic.AtomicBoolean

import org.apache.flink.streaming.api.functions.source.{
  ParallelSourceFunction,
  SourceFunction
}
import org.spixi.flink.models.Numbers

class RndSourceFunction extends ParallelSourceFunction[Numbers] {

  val keys = Map(0 -> "A", 1 -> "B", 2 -> "C", 3 -> "D")

  private lazy val rnd = scala.util.Random

  val isRunning: AtomicBoolean = new AtomicBoolean(true)

  override def run(ctx: SourceFunction.SourceContext[Numbers]): Unit = {

    while (isRunning.get()) {
      val nextKey = rnd.nextInt(4)
      val nextInt = rnd.nextInt(1000)
      val waitUntil = rnd.nextInt(4000) + 1000

      ctx.getCheckpointLock.synchronized {
        val toEmit =
          Numbers(keys(nextKey), nextInt, System.currentTimeMillis())
        ctx.collect(toEmit)
        Thread.sleep(waitUntil)
      }
    }
  }

  override def cancel(): Unit = {
    isRunning.set(false)
  }

}
