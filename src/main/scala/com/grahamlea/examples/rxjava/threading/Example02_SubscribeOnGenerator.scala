// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.IOScheduler

/**
 * Shows how using subscribeOn(), at any point in the chain, makes
 * the source Observable run using the specified scheduler
 */
object Example02_SubscribeOnGenerator extends App {
  val shiftedUp = generator.subscribeOn(IOScheduler()).map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
