// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import scala.concurrent.duration.{Duration, MILLISECONDS}

/**
 * Shows that you don't necessarily have to call subscribeOn()
 * or observeOn() for operations to start occurring on different
 * threads. Some built-in operations, such as delay() here,
 * internally use threading to achieve their behaviour.
 */
object Example07_ImplicitThreading extends App {

  val shiftedUp = generator.map(shiftUp).debug("Shifted Up")

  val delayed = shiftedUp.delay(Duration(10, MILLISECONDS)).debug("Delayed")

  val shiftedDown = delayed.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
