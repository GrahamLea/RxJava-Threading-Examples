// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.schedulers.IOScheduler

/**
 * Shows how observeOn() works, notably that the source Observable
 * still runs on the thread that calls subscribe(), but all code
 * from the observeOn() call onwards executes using the specified
 * Scheduler.
 *
 * On my machine, this example generates all values on the main
 * thread before all further processing of the values is performed
 * sequentially on thread "RxComputationThreadPool-1". So observeOn()
 * doesn't result in each value being processed in parallel, but just
 * on a single different thread.
 */
object Example03_ObserveOnGenerator extends App {

  val shiftedUp = generator.observeOn(IOScheduler()).map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
