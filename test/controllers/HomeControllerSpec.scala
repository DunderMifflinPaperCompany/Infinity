package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

/**
 * Test spec for the Dunder Mifflin Infinity website HomeController.
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents())
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("DUNDER MIFFLIN")
      contentAsString(home) must include ("Ryan Howard Announces Revolutionary Digital Initiative")
    }

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("DUNDER MIFFLIN")
      contentAsString(home) must include ("Latest News")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("DUNDER MIFFLIN")
      contentAsString(home) must include ("Employee Spotlight")
    }
  }

  "HomeController Welcome Endpoint" should {
    
    "render the welcome page with user input" in {
      val controller = new HomeController(stubControllerComponents())
      val welcome = controller.welcome("TestUser").apply(FakeRequest(GET, "/welcome"))

      status(welcome) mustBe OK
      contentType(welcome) mustBe Some("text/html")
      contentAsString(welcome) must include ("Hello TestUser!")
      contentAsString(welcome) must include ("Welcome to our revolutionary digital platform")
    }

    "demonstrate XSS vulnerability by outputting unescaped HTML" in {
      val controller = new HomeController(stubControllerComponents())
      val xssPayload = "<script>alert('XSS')</script>"
      val welcome = controller.welcome(xssPayload).apply(FakeRequest(GET, "/welcome"))

      status(welcome) mustBe OK
      contentType(welcome) mustBe Some("text/html")
      // The XSS payload should be present in the response unescaped, demonstrating the vulnerability
      contentAsString(welcome) must include ("<script>alert('XSS')</script>")
      contentAsString(welcome) must include ("XSS vulnerability by outputting user input without sanitization")
    }

    "accept HTML injection demonstrating the vulnerability" in {
      val controller = new HomeController(stubControllerComponents())
      val htmlPayload = "<h1>INJECTED</h1>"
      val welcome = controller.welcome(htmlPayload).apply(FakeRequest(GET, "/welcome"))

      status(welcome) mustBe OK
      contentType(welcome) mustBe Some("text/html")
      // The HTML should be injected directly without escaping
      contentAsString(welcome) must include ("<h1>INJECTED</h1>")
    }
  }
}
