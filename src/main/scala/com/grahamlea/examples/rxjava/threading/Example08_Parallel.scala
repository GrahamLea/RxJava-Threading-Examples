// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.Observable

/**
 * Shows the use of parallel() to process multiple values concurrently.
 *
 * Assuming you have more than 1 core, the output should show that operations
 * on a value are interleaved with operations on other values.
 *
 * Interestingly, I see that the subscriber may be called by any of the
 * threads in the thread pool, but not necessarily by the one that performed
 * the processing of that value.
 */
object Example08_Parallel extends App {

  val shiftedUpAndDownInParallel =
    generator.parallel((f: Observable[Int]) => {
      val shiftedUp = f.map(shiftUp).debug("Shifted Up")
      val shiftedDown = shiftedUp.map(shiftDown).debug("Shifted Down")
      shiftedDown
    })

  shiftedUpAndDownInParallel.subscribe(debug("Received", _))

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(1000)
}
