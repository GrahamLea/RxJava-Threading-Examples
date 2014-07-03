// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.{IOScheduler, ComputationScheduler}

/**
 * This BAD example shows that, if you erroneously apply two
 * subscribeOn() calls in one flow, the earliest application
 * takes precedence and later subscribeOn()s are effectively
 * ignored.
 */
object Example06x_TwoSubscribeOnCalls extends App {

  val shiftedUp = generator.subscribeOn(ComputationScheduler()).map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.subscribeOn(IOScheduler()).map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
