package controllers.support

import ticTacToe.CellState._

object BoardState {
  
  def get(states: String): Seq[CellState] = {
    
    def stringToStates(states: String): Seq[CellState] = {
      require(states.length == 9, "is " + states + " a legit board?")
      states.toCharArray().toList.map(char => {
        char match {
          case 'A' => Clear
          case 'X' => X
          case 'O' => O
        }
      })
    }
    
    stringToStates(states);
  }

}