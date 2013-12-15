package utils

import play.api._
import play.api.Play.current
import play.api.mvc._
import play.api.mvc.Results._
import play.api.{GlobalSettings, Application}
import scala.concurrent.Future

object Global extends GlobalSettings {
  
  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(
     {ex.getCause match {
      case _: NoSuchElementException  => NotFound(ex.getMessage())
      case ex => Logger.info(ex.getMessage()); InternalServerError}})
  }
}