package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api.Html
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class XssController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  def vulnerable1(value: String) = Action { implicit request: Request[AnyContent] =>
    // ruleid: tainted-html-response
    Ok(s"Hello $value !").as("text/html")
  }

  def vulnerable2(value: String) = Action.async { implicit request: Request[AnyContent] =>
    // ruleid: tainted-html-response
    Future.successful(Ok("Hello " + value + " !").as("tExT/HtML"))
  }

  def vulnerable3(value: String, contentType: String) = Action { implicit request: Request[AnyContent] =>
    val bodyVals = request.body.asFormUrlEncoded
    val smth = bodyVals.flatMap(_.get("username")).flatMap(_.headOption).getOrElse(value)
    // ruleid: tainted-html-response
    Ok(s"Hello $smth !").as(contentType)
  }

  def vulnerable4(value: String) = Action.async(parse.json) { implicit request =>
    // ruleid: tainted-html-response
    Future.successful(Ok("Hello " + value + " !").as(HTML))
  }

  def vulnerable5(value: String) = Action(parse.json) { implicit request =>
    // ruleid: tainted-html-response
    Ok(s"Hello $value !").as(HTML)
  }

  def vulnerable6(value: String) = Action { implicit request: Request[AnyContent] =>
    // ruleid: tainted-html-response
    Ok(views.html.xssHtml.render(Html.apply("Hello " + value + " !")))
  }
  
  def vulnerable7(value: String) = Action {
    // ruleid: tainted-html-response
    Ok(views.html.xssHtml.render(Html.apply("Hello " + value + " !")))
  }

  def safeJson(value: String) = Action.async { implicit request: Request[AnyContent] =>
    // ok: tainted-html-response
    Future.successful(Ok("Hello " + value + " !").as("text/json"))
  }

  def safeTemplate(value: String) = Action {
    // ok: tainted-html-response
    Ok(views.html.template.render(value))
  }

  def variousSafe(value: String) = Action { implicit request: Request[AnyContent] =>
    // This method demonstrates various safe practices
    // In a real implementation, you would choose one approach
    
    val escapedValue = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(value)
    val owaspEscapedValue = org.owasp.encoder.Encode.forHtml(value)
    
    // Return safely escaped content as HTML
    // ok: tainted-html-response
    Ok("Hello " + owaspEscapedValue + " !").as("text/html")
  }

  def safeJsonOnly(value: String) = Action { implicit request: Request[AnyContent] =>
    // ok: tainted-html-response
    Ok(s"Hello $value !").as("text/json")
  }

  def safeStaticHtml() = Action { implicit request: Request[AnyContent] =>
    // ok: tainted-html-response
    Ok("<b>Hello !</b>").as("text/html")
  }

  def safeWithEscaping(value: String) = Action { implicit request: Request[AnyContent] =>
    val escapedValue = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(value)
    // ok: tainted-html-response
    Ok("Hello " + escapedValue + " !").as("text/html")
  }

  // Constants for content types
  val HTML = "text/html"
}