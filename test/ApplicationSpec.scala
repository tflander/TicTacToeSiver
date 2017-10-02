import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._


class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "TieTacToe" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      // TODO: legit UI testing
      //contentAsString(home) must include ("Your new application is ready.")
    }

  }

}
