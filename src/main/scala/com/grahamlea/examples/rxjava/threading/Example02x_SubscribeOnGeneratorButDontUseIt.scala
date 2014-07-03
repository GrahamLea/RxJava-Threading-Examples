// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.IOScheduler

/**
 * Shows how subscribeOn() doesn't change the Observable, but
 * creates a new Observable that will subscribe using the specified
 * Scheduler.
 *
 * See that the Observable returned by subscribeOn() is never used
 * here, and consequently everything runs on the main thread.
 */
object Example02x_SubscribeOnGeneratorButDontUseIt extends App {
  val aGenerator = generator

  val neverUsed = aGenerator.subscribeOn(IOScheduler())

  val shiftedUp = aGenerator.map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))
}
