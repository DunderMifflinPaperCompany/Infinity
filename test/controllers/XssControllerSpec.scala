package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

/**
 * Test spec for the XssController demonstrating XSS vulnerabilities.
 */
class XssControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "XssController vulnerable methods" should {

    "vulnerable1 should return HTML content type" in {
      val controller = inject[XssController]
      val result = controller.vulnerable1("testValue").apply(FakeRequest(GET, "/xss/vulnerable1/testValue"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Hello testValue !")
    }

    "vulnerable2 should return mixed case HTML content type asynchronously" in {
      val controller = inject[XssController]
      val result = controller.vulnerable2("testValue").apply(FakeRequest(GET, "/xss/vulnerable2/testValue"))

      status(result) mustBe OK
      contentType(result) mustBe Some("tExT/HtML")
      contentAsString(result) must include("Hello testValue !")
    }

    "vulnerable6 should render through xssHtml template" in {
      val controller = inject[XssController]
      val result = controller.vulnerable6("testValue").apply(FakeRequest(GET, "/xss/vulnerable6/testValue"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Hello testValue !")
    }

    "vulnerable7 should render through xssHtml template" in {
      val controller = inject[XssController]
      val result = controller.vulnerable7("testValue").apply(FakeRequest(GET, "/xss/vulnerable7/testValue"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Hello testValue !")
    }
  }

  "XssController safe methods" should {

    "safeJson should return JSON content type" in {
      val controller = inject[XssController]
      val result = controller.safeJson("testValue").apply(FakeRequest(GET, "/xss/safe/json/testValue"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/json")
      contentAsString(result) must include("Hello testValue !")
    }

    "safeTemplate should render through safe template" in {
      val controller = inject[XssController]
      val result = controller.safeTemplate("testValue").apply(FakeRequest(GET, "/xss/safe/template/testValue"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("testValue")
    }
  }

  "XssController routing" should {

    "handle vulnerable1 route correctly" in {
      val request = FakeRequest(GET, "/xss/vulnerable1/testInput")
      val result = route(app, request).get

      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Hello testInput !")
    }

    "handle safe JSON route correctly" in {
      val request = FakeRequest(GET, "/xss/safe/json/testInput")
      val result = route(app, request).get

      status(result) mustBe OK
      contentType(result) mustBe Some("text/json")
      contentAsString(result) must include("Hello testInput !")
    }
  }
}