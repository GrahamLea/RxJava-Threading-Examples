// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

/**
 * Shows that simply calling parallel() somewhere in the chain doesn't
 * necessarily result that downstream operations occurring in parallel,
 * or even on different threads.
 */
object Example08x_Parallel_NoWorkInsideParallel extends App {

  val shiftedUp = generator.parallel(f => f).map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
