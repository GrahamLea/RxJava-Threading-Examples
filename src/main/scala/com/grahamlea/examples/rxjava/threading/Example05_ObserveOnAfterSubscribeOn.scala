// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.{IOScheduler, ComputationScheduler}

/**
 * Demonstrates subscribeOn() and observeOn() being used together,
 * showing how the subscribeOn() Scheduler is used for all operations
 * from the source Observable to the point where observeOn is called.
 *
 * On my machine, this combination results in operations on different
 * values being processed in parallel. This suggests that, contrary to
 * the double observeOn() example, a thread scheduled with
 * subscribeOn() does not block waiting on the work in the downstream
 * scheduler before generating and processing the next value.
 */
object Example05_ObserveOnAfterSubscribeOn extends App {

  val shiftedUp = generator.subscribeOn(ComputationScheduler()).map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.observeOn(IOScheduler()).map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
