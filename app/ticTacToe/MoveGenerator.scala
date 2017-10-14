package ticTacToe

import controllers.support.BoardState
import ticTacToe.CellState.CellState

// TODO: test
class MoveGenerator {

  def moveUsingAi(aiDescr: String, setup: String): (Option[CellState], Board) = {
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
    val ai = getAi(aiDescr, board)
    val updatedBoard = if(board.gameOver) board else ai.takeSquare(board)
    val winnerOrNot = updatedBoard.gameOver match {
      case false => None
      case true => Some(updatedBoard.winner)
    }
    (winnerOrNot, updatedBoard)
  }

  private def getAi(aiDescr: String, board: Board) = {
    ticTacToe.ai.dsl.AiBuilder.buildAi(board.nextPlayer, aiDescr)
  }


}
