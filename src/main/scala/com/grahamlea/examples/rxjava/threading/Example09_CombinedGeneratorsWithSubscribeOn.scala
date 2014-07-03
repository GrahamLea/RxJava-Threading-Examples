// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.IOScheduler

/**
 * Shows how using subscribeOn() in a flow where two generators are
 * combined doesn't result in parallel execution of the generators
 * if the subscribeOn() call is after the flows are combined
 * (using zip, in this case).
 */
object Example09_CombinedGeneratorsWithSubscribeOn extends App {
  val generator1 = generator.map(shiftUp).debug("Shifted Up #1")
  val generator2 = generator.map(shiftUp).debug("Shifted Up #2")

  val zipped = generator1.zip(generator2).map(_._1).debug("Zipped")

  val shiftedDown = zipped.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribeOn(IOScheduler()).subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
