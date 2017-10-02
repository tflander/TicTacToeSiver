package controllers

import javax.inject.{Inject, Singleton}

import play.api._
import play.api.mvc._
import ticTacToe.ai.SmartestAi
import ticTacToe.CellState._
import ticTacToe.Board
import controllers.support.BoardState
import ticTacToe.ai.LearningAi

@Singleton
class AiTrainer @Inject() extends Controller {

  val ai = new LearningAi(X)

  def index = Action {
    val board = Board()
    Ok(views.html.train("", ai.takeSquare(board), ai))
  }

  def move(setup: String) = Action {
    val cellStates = BoardState get setup
    val board = Board()
      .setCellState(0, 0, cellStates(0))
      .setCellState(1, 0, cellStates(1))
      .setCellState(2, 0, cellStates(2))
      .setCellState(0, 1, cellStates(3))
      .setCellState(1, 1, cellStates(4))
      .setCellState(2, 1, cellStates(5))
      .setCellState(0, 2, cellStates(6))
      .setCellState(1, 2, cellStates(7))
      .setCellState(2, 2, cellStates(8))
      
    println("=== temp debug")
    println(board)
    println("game over = " + board.gameOver)
    println("=== temp debug")
      
    val updatedBoard = if (board.gameOver) {
      ai.handleGameOverAfterOpponentsTurn(board)
      board
    } else {
      val update = ai.takeSquare(board)
      ai.handleGameOverAfterMyTurn(update.winner)      
      update
    }
    
    val message = updatedBoard.gameOver match {
      case false => ""
      case true => {
        updatedBoard.winner match {
          case Clear => "Tie (Cat Game)"
          case X => "I Win!!!"
          case O => "You Win??  Darn it!!!"
        }
      }
    }
    Logger.info(setup)
    if (!message.isEmpty) Logger.info(message)
    Ok(views.html.train(message, updatedBoard, ai))
  }

}