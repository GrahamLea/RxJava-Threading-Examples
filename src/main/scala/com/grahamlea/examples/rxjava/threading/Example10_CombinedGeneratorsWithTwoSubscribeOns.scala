// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.IOScheduler

/**
 * Shows how using subscribeOn() in a flow where two generators are
 * combined can achieve parallel execution of the generators
 * if the subscribeOn() calls are before the flows are combined
 * (using zip, in this case).
 *
 * On small sets with fixed delays, the subscriber always receives
 * values on the same thread. However, randomising the delays and
 * generating a larger set will show that values are delivered from
 * any thread (probably determined by whichever thread delivers the
 * last value to the zip() Observable).
 */
object Example10_CombinedGeneratorsWithTwoSubscribeOns extends App {
  val generatorSubscribed = generator.subscribeOn(IOScheduler())
  val shiftedUp1 = generatorSubscribed.map(shiftUp).debug("Shifted Up #1")
  val shiftedUp2 = generatorSubscribed.map(shiftUp).debug("Shifted Up #2")

  val zipped = shiftedUp1.zip(shiftedUp2).map(_._1).debug("Zipped")

  val shiftedDown = zipped.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
