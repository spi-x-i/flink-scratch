package org.spixi.flink.functions

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.util.Collector
import org.spixi.flink.models.Numbers

class MaxFlatMapFunction extends RichFlatMapFunction[Numbers, Numbers] {
  println(s"FlatMap init.")
  private[this] lazy val _state: ValueState[Numbers] = {
    val descriptor =
      new ValueStateDescriptor[Numbers]("max-states", classOf[Numbers])
    getRuntimeContext.getState(descriptor)
  }

  private def getState = Option(_state.value()).getOrElse(Numbers("", 0, 0l))

  override def flatMap(value: Numbers, out: Collector[Numbers]): Unit = {
    val currentMax = getState
    println(s"entro map con $value")

    if (currentMax.value < value.value) {
      println(s"new max $value")
      _state.update(value)
      out.collect(value)
    } else {
      println(s"current_max $currentMax")
      out.collect(currentMax)
    }
  }

}
