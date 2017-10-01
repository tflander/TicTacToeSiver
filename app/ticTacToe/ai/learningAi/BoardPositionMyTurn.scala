package ticTacToe.ai.learningAi

import scala.collection.mutable.ListBuffer
import ticTacToe.Board
import ticTacToe.CellState._

import Outcomes._
import scala.annotation.tailrec
import scala.collection._

class BoardPositionMyTurn(val position: String, icon: CellState, val parentPosition: Option[BoardPositionOpponentsTurn]) {
  val childPositions = mutable.Map[String, BoardPositionOpponentsTurn]()
  var score = ScoreSystem.unknown

  val candidateMoves = {

    def candidateMoveNumber(i: Int): String = {
      def charToReplace(): Int = {
        var candidatePos = -1
        for (j <- 0 to i) {
          candidatePos = position.indexOf("A", candidatePos + 1)
        }
        return candidatePos
      }

      val posToReplace = charToReplace()
      val c = 0 until position.length map (c => {
        if (c == posToReplace) icon.toString.charAt(0) else position.charAt(c)
      })
      c.toList.mkString
    }

    val numCandidateMoves = position.count(_ == 'A')
    0 until numCandidateMoves map (i => {
      candidateMoveNumber(i)
    })
  }

  def bestMove: String = {
    var candidate = ""
    var bestScore = -100
    for (move <- candidateMoves) {
        childPositions.get(move) match {
        case None => {
          if (75 > bestScore) {
            candidate = move
            bestScore = 75
          }
        }
        case Some(position) =>
          if (position.score > bestScore) {
            candidate = move
            bestScore = position.score
          }
      }
    }
    return candidate
  }

  override def toString() = {

//    @tailrec
//    def ultimateParent(pos: BoardPosition): BoardPosition = {
//      parentPosition match {
//        case Some(parent) => return ultimateParent(parent)
//        case None => return pos
//      }
//    }
//
//    def grandpa: String = {
//      parentPosition match {
//        case Some(parent) => {
//          parent.parentPosition match {
//            case Some(grandpa) => grandpa.position
//            case None => return "no grandpa"
//          }
//        }
//        case None => return "no parent"
//      }
//
//      return "no grandpa"
//    }

    "BoardPosition " + position + " " + score + "; " + candidateMoves.size + " potential moves; " + childPositions.size + " explored moves."
  }
  
  def showPotentialMoves() = {
    println("potential moves:")
    
    candidateMoves.foreach(move => {
      childPositions.get(move) match {
        case Some(exploredPostion) => println(" " + exploredPostion)
        case None => println(move + " is unexplored")
      }
    })
    println("")
  }

  def addChildIfNecessary(boardPosition: BoardPositionOpponentsTurn) = {
    childPositions.put(boardPosition.position, boardPosition)
  }
  
  def calcScoreAsAverageOfChildren(): Unit = {
    
    val totalSum =  candidateMoves.foldLeft(0)((sum, childMove) => sum + {
      childPositions.get(childMove) match {
        case Some(move) => move.score
        case None => ScoreSystem.draw 
      }
    })
    score = totalSum / candidateMoves.size
    
    parentPosition.foreach(_.calcScoreAsAverageOfChildren())
  }
  
  def registerGameEnd(newScore: Int) = {
    score = newScore
    parentPosition.get.calcScoreAsAverageOfChildren()
  }
  
  def registerLoss() = {
    registerGameEnd(ScoreSystem.loss)
  }

  def registerDraw() = {
    registerGameEnd(ScoreSystem.draw)
  } 
}
