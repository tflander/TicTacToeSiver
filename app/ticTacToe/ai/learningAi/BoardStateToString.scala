package ticTacToe.ai.learningAi

import ticTacToe.Board
import ticTacToe.CellState._


object BoardStateToString {
  def boardToString(board: Board): String = {

    return board.rows.map(row => {
      row.map(_ match {
        case X => "X"
        case O => "O"
        case Clear => "A"
      })
    }).flatten.foldLeft("")(_ + _)

  }
}