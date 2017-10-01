package ticTacToe.ai.learningAi

import ticTacToe.CellState._
import scala.collection._

class BoardPositionOpponentsTurn(val position: String, icon: CellState, val parentPosition: Option[BoardPositionMyTurn]) {
  var score = ScoreSystem.unknown
  val childPositions = mutable.Map[String, BoardPositionMyTurn]()

  def calcScoreAsAverageOfChildren(): Unit = {

    val a = childPositions.values

    val totalSum = childPositions.values.foldLeft(0)((sum, childMove) => sum + childMove.score)
    
    // TODO:  which is correct?
    score = totalSum / childPositions.size
//    score = childPositions.size match {
//      case 0 => ScoreSystem.loss
//      case _ => totalSum / childPositions.size
//    }

    parentPosition.foreach(_.calcScoreAsAverageOfChildren())
  }

  def addChildIfNecessary(boardPosition: BoardPositionMyTurn) = {
    childPositions.put(boardPosition.position, boardPosition)
  }

  def registerGameEnd(newScore: Int) = {
    score = newScore
    parentPosition.get.calcScoreAsAverageOfChildren()
  }
  
  def registerWin() = {
    registerGameEnd(ScoreSystem.win)
  }

  def registerDraw() = {
    registerGameEnd(ScoreSystem.draw)
  }

}