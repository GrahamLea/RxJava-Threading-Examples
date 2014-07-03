// Copyright (c) 2014 Belmont Technology Pty Ltd. All rights reserved.
package com.grahamlea.examples.rxjava

import rx.lang.scala.Observable

package object threading {

  val maxNumber = 5
  
  val generator: Observable[Int] = Observable.from(1 to maxNumber).debug("Generated")

  implicit class ObservableWithDebug(observable: Observable[Int]) {
    def debug(message: String) = {
      observable.doOnNext(value => {
        threading.debug(message, value, if (value > maxNumber) shiftDown(value) else value)
      })
    }
  }

  val shiftUp: (Int) => Int = (i: Int) => {Thread.sleep(25); i + (maxNumber * 2) }

  val shiftDown: (Int) => Int = (i: Int) => {Thread.sleep(25); i - (maxNumber * 2) }

  def debug(message: String, value: Int): Unit = debug(message, value, value)

  def debug(message: String, value: Int, indent: Int): Unit = {
    printf("%30s %-14s %" + (indent * 4) + "s%n", "[" + Thread.currentThread().getName + "]", message, value)
  }
}
