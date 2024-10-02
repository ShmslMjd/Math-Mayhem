package ch.makery.address.model

class Score(var score: Int) {
  def increaseScore(): Unit = {
    score += 1
  }

  def decreaseScore(): Unit = {
    if (score != 0)
      score -= 1
  }

}