package ch.makery.address.model

import scala.concurrent.duration._

class Timer(var duration: Duration) {

  def increaseTime(amount: Duration): Unit = {
    duration += amount
  }

  def decreaseTime(amount: Duration): Unit = {
    duration -= amount
  }

}
