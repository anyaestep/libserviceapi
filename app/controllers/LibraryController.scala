package controllers

import play.api.mvc._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.libs.concurrent.Akka
import akka.actor.Props
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Actor
import akka.util.Timeout
import scala.concurrent.duration._
import akka.actor.actorRef2Scala
import akka.pattern.ask
import services.LibraryService

object LibraryController extends Library

class Library extends Controller {
  
  val libraryService = new LibraryService 
  
  def library = libraryService
  
  /**
   * Check if the book is available to the requesting user
   */
  def checkout(studentId: String, bookId: String) = Action.async { implicit request => 
    val bookExists = library.getBookExists(bookId)
    val studentExists = library.getStudentExists(studentId)
    val contentPath = library.getBookPath(bookId, studentId)
    for {
      be <- bookExists
      se <- studentExists
      contentPath <- contentPath
    } yield {
      // if one of the does not, return 404
      if (be.get == 0) 
        NotFound("book")
      if (se.get == 0)
        NotFound("student")
      else // if content found, return content, if not, we know it's forbidden from the previous checks
        if (contentPath.isDefined) Ok.sendFile(getFile(contentPath.get)) else Forbidden
    }
  }
  
  def getFile(path: String) = new java.io.File(path)
}