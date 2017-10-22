import org.scalatestplus.play._
import play.api.mvc.Result
import play.api.test._
import play.api.test.Helpers._

import scala.collection.immutable
import scala.concurrent.Future
import scala.xml.{Elem, Node, NodeSeq}


class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "TieTacToe" should {

    "render a board for a new game" in {
      val newGame = route(app, FakeRequest(GET, "/")).get

      status(newGame) mustBe OK
      contentType(newGame) mustBe Some("text/html")
      val page = pageAsXml(newGame)
      pageHeading(page) mustBe("Unbeatable Tic Tac Toe")

    }

    "render buttons to create a new game" in {
      val newGame = route(app, FakeRequest(GET, "/")).get

      status(newGame) mustBe OK
      contentType(newGame) mustBe Some("text/html")
      implicit val page = pageAsXml(newGame)
      buttonLabels mustBe(Seq("New Game (You First)", "New Game (Me First)"))

    }

    "render a board with the computer going first for a new game" in {
      val newGame = route(app, FakeRequest(GET, "/ttt/")).get

      implicit val page = pageAsXml(newGame)
      println (board)
      board.contains("X") mustBe true
    }

    "X blocks the win for a game in progress" in {
      val newGame = route(app, FakeRequest(GET, "/ttt/AAOXOXAAA")).get

      implicit val page = pageAsXml(newGame)
      board mustBe("..OXOXX..")
    }

  }

  private def pageHeading(implicit page: Elem) = {
    (page \\ "h1" text)
  }

  private def buttonLabels(implicit page: Elem) = {
    val buttons = (page \\ "input")
    buttons.map(button => button.attribute("value")).flatten.map(node => node.text)
  }

  private def board(implicit page: Elem) = {

    def classNodeToCellState(classNode: Node) = {
       val stateClass = classNode.text.split(" ")(1)
       if (stateClass == "Clear") "." else stateClass
    }

    (page \\ "table" \ "tr" \ "td")
      .map(_.attribute("class")).flatten.flatten.map(classNodeToCellState).mkString

  }

  private def pageAsXml(eventualResult: Future[Result]) = {
    scala.xml.XML.loadString(contentAsString(eventualResult).replaceFirst("<!DOCTYPE html>", ""))
  }
}



