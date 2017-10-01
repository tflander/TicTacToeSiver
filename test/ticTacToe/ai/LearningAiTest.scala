package ticTacToe.ai
import org.scalatest._
import ticTacToe.CellState._
import ticTacToe.Board

class LearningAiTest extends FunSpec with ShouldMatchers with BeforeAndAfter {

  var ai: LearningAi = _

  before {
    ai = new LearningAi(X)
  }

  it("takes the first square on an empty board") {
    val board = Board(
      (Clear, Clear, Clear),
      (Clear, Clear, Clear),
      (Clear, Clear, Clear))
    ai.takeSquare(board).cellState(0, 0) should be(X)
    ai.opponentMoves.keys should be (Set("AAAAAAAAA"))
    ai.myMoves.keys should be (Set("XAAAAAAAA"))
    val emptyBoardPosition = ai.opponentMoves.get("AAAAAAAAA").get
    emptyBoardPosition.parentPosition should be (None)
    emptyBoardPosition.childPositions.keys should be (Set("XAAAAAAAA"))
    val newBoardPosition = emptyBoardPosition.childPositions.get("XAAAAAAAA").get
    newBoardPosition.parentPosition.get.position should be("AAAAAAAAA")
  }

}