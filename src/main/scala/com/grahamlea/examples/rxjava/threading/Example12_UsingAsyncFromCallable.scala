// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

import rx.lang.scala.ImplicitFunctionConversions.scalaFunction0ToRxFunc0
import rx.lang.scala.JavaConversions.toScalaObservable
import rx.util.async.Async
import rx.lang.scala.Observable

/**
 * Shows using Async.fromCallable() to execute a function
 * asynchronously whose return value then emitted by an Observable.
 *
 * Unlike Async.start(), this example does what I expect: each
 * callable is executed on its own thread and the results are
 * delivered to the subscriber upon completion.
 *
 * In contrast to Async.start(), fromCallable() doesn't start
 * executing the source functions until the subscriber subscribes.
 *
 * Also in contrast to Async.start() which, when it works, sees
 * values delivered to the subscriber on the main thread,
 * fromCallable() ends up delivering them on one of the threads
 * that is emitting source values.
 */
object Example12_UsingAsyncFromCallable extends App {

  def sleepThenGenerate(n: Int)() = {
    debug("Generating", n)
    Thread.sleep(800)
    debug("Sending", n)
    n
  }

  val asyncGenerator1: Observable[Int] = Async.fromCallable(sleepThenGenerate(1) _)
  val asyncGenerator2: Observable[Int] = Async.fromCallable(sleepThenGenerate(2) _)

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
