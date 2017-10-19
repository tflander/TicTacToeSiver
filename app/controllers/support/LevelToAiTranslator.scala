package controllers.support

object LevelToAiTranslator {

  def aiFor(i: Int) = {
    i match {
      case 3 => "is random"
      case 2 => "opens with centerOrCorner, otherwise is random, blocks 80% of the time, and misses wins 20% of the time"
      case default => "is unbeatable"
    }
  }

}
