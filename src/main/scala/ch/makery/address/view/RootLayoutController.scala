package ch.makery.address.view

import ch.makery.address.MainApp
import scalafxml.core.macros.sfxml
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

@sfxml
class RootLayoutController(){

  def showAbout(): Unit = {
    val alert = new Alert(AlertType.Information) {
      initOwner(MainApp.stage)
      title = "About Math Mayhem"
      headerText = "MATH MAYHEM 2023"
      contentText = "Math Mayhem is a fun and engaging math training game that helps you " +
        "improve your math skills while having fun. The game is designed to help you practice simple addition, " +
        "subtraction, multiplication, and division problems in a fun and interactive way\n" +
        "\nHOW TO PLAY?\n" +
        "\n1. To play the game, you will need to solve 20 math problems within a given time limit. " +
        "The game will present you with a series of simple math problems, and you will need to " +
        "solve them as quickly and accurately as possible\n" +
        "\n2. To solve a math problem, you will need to select the correct answer from a list of possible answers. " +
        "The game will provide you with four possible answers, and you will need to select the correct one.\n" +
        "\n3. To make the game more challenging. We will decrease your time and mark if you select the wrong answer. But" +
        " no worries we will also add your timer if you select the correct answer!\n" +
        "\n4. The game will keep track of your score and the time remaining. The game will end if your timer run out" +
        " or if you answer all 20 question. All the best!"

    }.showAndWait()
  }

  def showMenu(): Unit = {
    MainApp.showWelcome()
  }

  def startGame(): Unit = {
    MainApp.showGame()
  }

  def quitGame(): Unit = {
    System.exit(0)
  }

}