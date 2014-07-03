// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.ImplicitFunctionConversions.scalaFunction0ToRxFunc0
import rx.lang.scala.JavaConversions.toScalaObservable
import rx.util.async.Async
import rx.lang.scala.Observable

/**
 * Shows using Async.start() to execute a function asynchronously
 * whose return value is then emitted by an Observable.
 *
 * I find this example doesn't do what I think it should. If the
 * function returns a value immediately, before the subscriber
 * subscribes, then the debug("Generated") is executed on the main
 * thread and the rest of the chain works as expected.
 *
 * However, if the function completes after the subscriber has
 * subscribed, the debug("Generated") is executed on the Async
 * thread, and I find that the value from the function is never
 * delivered to the rest of the chain.
 */
object Example11_UsingAsyncStart extends App {

  def sleepThenGenerate(n: Int)() = {
      // When this sleep is present, and the subscriber attaches
      // before the values are emitted, I see nothing delivered by
      // the Observables from Async.
      debug("Generating", n)
      Thread.sleep(800)
      debug("Sending", n)
      n
    }

  val asyncGenerator1: Observable[Int] = Async.start(sleepThenGenerate(1) _)
  val asyncGenerator2: Observable[Int] = Async.start(sleepThenGenerate(2) _)

  val shiftedUp1 = asyncGenerator1.debug("Generated").map(shiftUp).debug("Shifted Up #1")
  val shiftedUp2 = asyncGenerator2.debug("Generated").map(shiftUp).debug("Shifted Up #2")

  val zipped = shiftedUp1.zip(shiftedUp2).map(_._1).debug("Zipped")

  val shiftedDown = zipped.map(shiftDown).debug("Shifted Down")

  Thread.sleep(100)
  println("Subscribing...")
  shiftedDown.subscribe(
    onNext = debug("Received", _),
    onCompleted = () => {println("Completed")},
    onError = (t) => {}
  )

  // The built in Schedulers use daemon threads, so you need to make the main thread stick around or you won't see anything
  Thread.sleep(2000)
}
