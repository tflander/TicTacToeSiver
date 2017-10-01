package ticTacToe.ai

import ticTacToe.Board
import learningAi.Outcomes._
import scala.collection._
import ticTacToe.ai.learningAi._
import ticTacToe.ai.learningAi.BoardStateToString._
import ticTacToe.CellState._
import scala.annotation.tailrec

class LearningAi(val icon: CellState) extends ComputerPlayer {

  val opponentMoves = mutable.Map[String, BoardPositionMyTurn]()
  val myMoves = mutable.Map[String, BoardPositionOpponentsTurn]()

  var message: String = _

  var lastMove: Option[BoardPositionOpponentsTurn] = None

  override def takeSquare(implicit board: Board): Board = {
    require(!board.gameOver)

    message = ""
    val positionKey = boardToString(board)
    val currentBoardPosition = getCurrentBoardPosition(positionKey, lastMove)

    println("exploredPositions:")
    println(opponentMoves.keys)

    lastMove match {
      case Some(pos) => pos.addChildIfNecessary(currentBoardPosition)
      case None =>
    }

    println("currentBoardPosition")
    println(currentBoardPosition)
    println("lastMove")
    println(lastMove)

    val move = currentBoardPosition.bestMove

    val myMove = getMove(move, currentBoardPosition)
    //    message += "best score = " + myMove.score
    return board.setCellState(myMove, icon)
  }

  def moveForNewPosition(oldPos: String, newPos: String): (Int, Int) = {
    findChangedCell(oldPos, newPos, 0) match {
      case 0 => (0, 0)
      case 1 => (1, 0)
      case 2 => (2, 0)
      case 3 => (0, 1)
      case 4 => (1, 1)
      case 5 => (2, 1)
      case 6 => (0, 2)
      case 7 => (1, 2)
      case 8 => (2, 2)
    }
  }

  def createBoardPositionMyTurn(key: String, parent: Option[BoardPositionOpponentsTurn]) = {
    val pos = new BoardPositionMyTurn(key, icon, parent)
    opponentMoves.put(key, pos)
    pos
  }

  def createBoardPositionOpponentsTurn(key: String, parent: Option[BoardPositionMyTurn]) = {
    val pos = new BoardPositionOpponentsTurn(key, icon, parent)
    myMoves.put(key, pos)
    pos
  }

  def getCurrentBoardPosition(positionKey: String, parent: Option[BoardPositionOpponentsTurn]) = {
    opponentMoves.getOrElse(positionKey, {
      message += "hmmm...I haven't seen this before.  "
      createBoardPositionMyTurn(positionKey, parent)
    })
  }

  def getMove(move: String, currentBoardPosition: BoardPositionMyTurn): (Int, Int) = {
    val newPos = myMoves.get(move) match {
      case Some(pos) => pos
      case None => createBoardPositionOpponentsTurn(move, Some(currentBoardPosition))
    }
    currentBoardPosition.addChildIfNecessary(newPos)
    lastMove = Some(newPos)

//    println("newPosition = " + newPos)
//    currentBoardPosition.showPotentialMoves()

    return moveForNewPosition(currentBoardPosition.position, newPos.position)
  }

  def handleGameOverAfterMyTurn(outcome: CellState) = {
    outcome match {
      case `icon` => lastMove.get.registerWin()
      case Clear => lastMove.get.registerDraw()
    }
    lastMove = None
  }

  def handleGameOverAfterOpponentsTurn(board: Board) = {
    val positionKey = boardToString(board)
    print("looking for position " + positionKey + " in " + opponentMoves)
    
    val boardPosition = createBoardPositionMyTurn(positionKey, lastMove)

    board.winner match {
      case Clear => boardPosition.registerDraw
      case _ => boardPosition.registerLoss
    }
    lastMove = None
  }

  @tailrec
  private def findChangedCell(currPos: String, newPos: String, cell: Int): Int = {
    if (currPos.charAt(cell) != newPos.charAt(cell)) {
      return cell
    } else {
      return findChangedCell(currPos, newPos, cell + 1)
    }
  }

}