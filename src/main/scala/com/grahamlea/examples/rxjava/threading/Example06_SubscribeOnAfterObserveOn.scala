// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.{IOScheduler, ComputationScheduler}

/**
 * Demonstrates subscribeOn() and observeOn() being used together,
 * but this time with subscribeOn() being specified later in the flow
 * than observeOn().
 *
 * This example operates exactly the same as the previous one where
 * subscribeOn() appeared earlier in the flow. This shows that in a
 * non-forked flow, the position of a subscribeOn() call has no
 * effect - it always dictates the thread used to subscribe to the
 * source Observable.
 */
object Example06_SubscribeOnAfterObserveOn extends App {

  val shiftedUp = generator.map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.observeOn(IOScheduler()).map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribeOn(ComputationScheduler()).subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
