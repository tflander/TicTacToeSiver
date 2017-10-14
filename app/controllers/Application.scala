package controllers

import javax.inject._

import play.api.mvc._
import ticTacToe.Board
import ticTacToe.CellState._
import controllers.support.BoardState
import play.api.Logger

@Singleton
class Application @Inject() extends Controller {

  def index = Action {
    val board = Board()
    val ai = getAi(4, board)
    Ok(views.html.index("", ai.takeSquare(board)))
  }

  def move(setup: String) = Action {
    val messageAndBoard = moveUsingAiImpl(4, setup)
    Ok(views.html.index(messageAndBoard._1, messageAndBoard._2))
  }

  def moveUsingAi(level: Int, setup: String) = Action {
    val messageAndBoard = moveUsingAiImpl(level, setup)
    Ok(views.html.index(messageAndBoard._1, messageAndBoard._2))
  }

  def moveUsingAiImpl(level: Int, setup: String): (String, Board) = {
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
    val ai = getAi(level, board)
    val updatedBoard = if(board.gameOver) board else ai.takeSquare(board)
    val message = updatedBoard.gameOver match {
      case false => ""
      case true => updatedBoard.winner match {
        case Clear => "Tie (Cat Game)"
        case X => "I Win!!!"
        case O => "You Win??  You must have cheated!!!"
      }
    }
    Logger.info(setup)
    if(!message.isEmpty) Logger.info(message)
    (message, updatedBoard)
  }

  private def getAi(level: Int, board: Board) = {
    val ai = level match {
      case 4 => "is unbeatable"
    }
    ticTacToe.ai.dsl.AiBuilder.buildAi(board.nextPlayer, ai)
  }

}
