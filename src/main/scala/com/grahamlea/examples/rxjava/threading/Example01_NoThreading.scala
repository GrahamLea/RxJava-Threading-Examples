// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava.threading

/**
 * All this code runs on the 'main' thread.
 * Though RxJava is all about "asynchronous sequences", by default
 * everything just happens on the thread that subscribes.
 */
object Example01_NoThreading extends App {
  val shiftedUp = generator.map(shiftUp).debug("Shifted Up")

  val shiftedDown = shiftedUp.map(shiftDown).debug("Shifted Down")

  shiftedDown.subscribe(debug("Received", _))
}
