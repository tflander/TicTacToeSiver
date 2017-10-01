package ticTacToe.ai.learningAi
import org.scalatest._
import ticTacToe.ai.learningAi.BoardStateToString._
import ticTacToe.Board
import Outcomes._
import ticTacToe.CellState._

class BoardPositionTest extends FunSpec with ShouldMatchers {

  describe("BoardPosition creation tests") {
    val pos = new BoardPositionMyTurn("AXXAOAAAA", O, None)

    it("should construct candidate moves") {
      pos.candidateMoves should be(Seq(
        "OXXAOAAAA",
        "AXXOOAAAA",
        "AXXAOOAAA",
        "AXXAOAOAA",
        "AXXAOAAOA",
        "AXXAOAAAO"))
    }

    it("should default everthing else") {
      pos.parentPosition should be(None)
      pos.childPositions.size should be(0)
      pos.score should be(75)
    }
  }

  describe("first unexplored move tests") {
    it("should move in the first cell when no moves explored") {
      val pos = new BoardPositionMyTurn("AXXAOAAAA", O, None)
      pos.bestMove should be("OXXAOAAAA")
    }

    it("should take the 2nd available cell when the first move lost") {
      val pos = new BoardPositionMyTurn("AXXAOAAAA", O, None)
      val firstMove = pos.bestMove
      pos.bestMove should be("OXXAOAAAA")
      val opponentsResponse = new BoardPositionOpponentsTurn(firstMove, O, Some(pos))
      opponentsResponse.score = 50 // assume loss
      pos.childPositions.put(firstMove, opponentsResponse)
      pos.bestMove should be("AXXOOAAAA") 
    }
  }
  
  describe("boardToString tests") {
    val board = Board(
      (Clear, X, X),
      (Clear, O, Clear),
      (Clear, Clear, Clear))
    it("serializes a board to a string of three rows") {

      boardToString(board) should be("AXXAOAAAA")
    }
  }
  
  describe("Calc Score As Average Of Children Tests") {

	val loserGrandParent = new BoardPositionMyTurn("AXXAOAAAA", O, None)
    val loserParent =      new BoardPositionOpponentsTurn("AXXAOAOAA", O, Some(loserGrandParent))
    val loser =            new BoardPositionMyTurn("XXXAOAOAA", O, Some(loserParent))
	
	loserParent.addChildIfNecessary(loser)
	loserGrandParent.addChildIfNecessary(loserParent)
	loser.score = 0
	
    it("adjusts scores after a game") {
      loser.score should be(0)
      loserParent.score should be (75)
      loserGrandParent.score should be (75)
      
      loserParent.calcScoreAsAverageOfChildren()
      
      loser.score should be(0)
      loserParent.score should be (0)  
      loserGrandParent.score should be (41)
      
      loser.candidateMoves.size should be(4)
      loserParent.childPositions.size should be(1)
      loserGrandParent.candidateMoves.size should be(6)
    }
  }
}