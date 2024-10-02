package ch.makery.address.view

import ch.makery.address.MainApp
import ch.makery.address.model.Score
import ch.makery.address.model.Timer
import scalafx.Includes.handle
import scalafx.application.Platform
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, ProgressBar}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, promise}
import scala.concurrent.duration.{Duration, DurationInt, SECONDS}
import scala.util.Random
import scala.util.control.Breaks.break

@sfxml
class GameController(private val timer: Text,
                     private val score: Text,
                     private val question: Text,
                     private val ans1: Button,
                     private val ans2: Button,
                     private val ans3: Button,
                     private val ans4: Button,
                     private val wrongIcon: ImageView,
                     private val correctIcon: ImageView,
                     private val progressBar: ProgressBar) {

  private val random = new Random()
  var currentScore = new Score(0)
  var remainingTime = new Timer(30.seconds)
  var totalTime: Double = remainingTime.duration.toUnit(SECONDS)

  /** *************************************************************************************
   * Title: Blossoms of Summer Night Music
   * Author: HOYO-MIX
   * Date: 22/1/2022
   * Availability: https://www.youtube.com/watch?v=bmzmDeh2eAQ&ab_channel=HOYO-MiX-Topic
   * ************************************************************************************* */

  val music = new Media(getClass.getResource("/ch/makery/address/images/Blossoms of Summer Night.mp3").toString)
  val player = new MediaPlayer(music)
  player.volume = 0.3
  player.autoPlay = true

  /** *************************************************************************************
   * Title: Correct Answer sound effect
   * Author: Sound Effects
   * Date: 5/9/2023
   * Availability: https://www.youtube.com/watch?v=mhgOQmwaic4&ab_channel=SoundEffects
   * ************************************************************************************* */

  val correctSound = new Media(getClass.getResource("/ch/makery/address/images/correct Answer sound.mp3").toString)
  val playerCorrect = new MediaPlayer(correctSound)

  /** *************************************************************************************
   * Title: Wrong Answer Sound effect
   * Author: VS Mode
   * Date: 2/1/2021
   * Availability: https://www.youtube.com/watch?v=4GSXyo3euR4&ab_channel=VSMode
   * ************************************************************************************* */

  val wrongSoundSound = new Media(getClass.getResource("/ch/makery/address/images/Wrong Answer Sound effect.mp3").toString)
  val playerWrong = new MediaPlayer(wrongSoundSound)

  score.text = currentScore.score.toString
  timer.text = remainingTime.duration.toString

  startTimer()

  startRound()

  /** *************************************************************************************
   * Title: CHATGPT
   * Author: CHATGPT
   * Date: 22/12/2023
   * Code version: NONE
   * Availability: https://chat.openai.com/
   * Code provided/inspired by the source: Line 87 -89
   * ************************************************************************************* */

  def startTimer(): Unit = {
    var future = Future {
      while (remainingTime.duration > 0.seconds) {
        Thread.sleep(1.second.toMillis)
        remainingTime.duration -= 1.second
        updateUI(remainingTime.duration)
        var durationSeconds: Double = remainingTime.duration.toUnit(SECONDS)
        decreaseProgress(durationSeconds / totalTime)

        if(currentScore.score == 20){
          break()
        }

      }

      endGame()

    }

  }

  /** *************************************************************************************
   * Title: CHATGPT
   * Author: CHATGPT
   * Date: 22/12/2023
   * Code version: NONE
   * Availability: https://chat.openai.com/
   * Code provided/inspired by the source: Line 117
   * ************************************************************************************* */

  def decreaseProgress(value: Double): Unit = {
    Platform.runLater {
      progressBar.progress = 1.0 - value
    }
  }

  def updateUI(timeRemaining: Duration): Unit = {
    timer.text = timeRemaining.toString
  }

  def endGame(): Unit = {
    Platform.runLater{
      val alert = new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "Times Up!"
        headerText = "You unable to answer all question in the given time"
        contentText = "Keep it up and try again!"
      }.showAndWait()

      player.stop()

      MainApp.showWelcome()
    }
  }

  /** *************************************************************************************
   * Title: CHATGPT
   * Author: CHATGPT
   * Date: 22/12/2023
   * Code version: NONE
   * Availability: https://chat.openai.com/
   * Code provided/inspired by the source: Line 154 - 155
   * ************************************************************************************* */

  def showIcon(answer: String): Unit = {

    if(answer == "correct") {
      correctIcon.visible = true
      Future {
        Thread.sleep(1000)
        correctIcon.visible = false
      }
    }
    else{
      wrongIcon.visible = true
      Future {
        Thread.sleep(1000)
        wrongIcon.visible = false
      }
    }

  }

  def correctAnswer(): Unit = {
    playerCorrect.stop()
    playerCorrect.play()
    showIcon("correct")
    currentScore.increaseScore()
    remainingTime.increaseTime(5.seconds)
    timer.text = remainingTime.duration.toString
    score.text = currentScore.score.toString
    var durationSeconds: Double = remainingTime.duration.toUnit(SECONDS)
    decreaseProgress(durationSeconds / totalTime)
    startRound()
  }

  def wrongAnswer(): Unit = {
    playerWrong.stop()
    playerWrong.play()
    showIcon("wrong")
    currentScore.decreaseScore()
    remainingTime.decreaseTime(5.seconds)
    timer.text = remainingTime.duration.toString
    score.text = currentScore.score.toString
    var durationSeconds: Double = remainingTime.duration.toUnit(SECONDS)
    decreaseProgress(durationSeconds / totalTime)
    startRound()
  }

  /** *************************************************************************************
   * Title: CHATGPT
   * Author: CHATGPT
   * Date: 22/12/2023
   * Code version: NONE
   * Availability: https://chat.openai.com/
   * Code provided or inspired by the source: Line 204 - 228
   * ************************************************************************************* */

  def generateProblem(): (String, Int) = {

    val a = random.nextInt(100)
    val b = random.nextInt(100)
    val operator = random.nextInt(4)

    val problem = operator match {
      case 0 => s"$a + $b = ?"
      case 1 => s"$a - $b = ?"
      case 2 => s"$a * $b = ?"
      case 3 => s"$a / $b = ?"
    }

    val answer = operator match {
      case 0 => a + b
      case 1 => a - b
      case 2 => a * b
      case 3 => a / b
    }

    println(problem, answer)

    (problem, answer)

  }

  def startRound() : Unit = {

    var (problem, answer) = generateProblem()
    val problemOnly = problem
    question.text = problemOnly

    val wrongAnswer1 = answer + random.nextInt(5) + 9
    val wrongAnswer2 = answer + random.nextInt(10) - 7
    val wrongAnswer3 = answer + random.nextInt(8) + 8

    val listOfAnswer = List(answer, wrongAnswer1, wrongAnswer2, wrongAnswer3)
    val randomAnswer = Random.shuffle(listOfAnswer)

    ans1.setText(randomAnswer(0).toString)
    ans2.setText(randomAnswer(1).toString)
    ans3.setText(randomAnswer(2).toString)
    ans4.setText(randomAnswer(3).toString)

    ans1.onAction = handle {
      if (ans1.getText.toInt == answer) {
        correctAnswer()
      } else {
        wrongAnswer()
      }
    }

    ans2.onAction = handle {
      if (ans2.getText.toInt == answer) {
        correctAnswer()
      } else {
        wrongAnswer()
      }
    }

    ans3.onAction = handle {
      if (ans3.getText.toInt == answer) {
        correctAnswer()
      } else {
        wrongAnswer()
      }
    }

    ans4.onAction = handle {
      if (ans4.getText.toInt == answer) {
        correctAnswer()
      } else {
        wrongAnswer()
      }
    }

    if (currentScore.score == 20) {
      val alert = new Alert(AlertType.Information) {
        initOwner(MainApp.stage)
        title = "You Win!"
        headerText = "You have answered all the question in the given time!"
        contentText = "Keep it up to be good at math"
      }.showAndWait()

      player.stop()

      MainApp.showWelcome()

    }

  }

}
