package controllers.support
import org.scalatest._

class LevelToAiTranslatorTest extends FunSpec with ShouldMatchers {

  import controllers.support.LevelToAiTranslator._

  it("creates unbeatable AI for level 1") {
    aiFor(1) should be ("is unbeatable")
  }

  it("creates easy AI for level 2") {
    aiFor(2) should be ("opens with centerOrCorner, otherwise is random, blocks 80% of the time, and misses wins 20% of the time")
  }

  it("creates random AI for level 3") {
    aiFor(3) should be ("is random")
  }
}
