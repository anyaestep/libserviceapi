package services

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

case class DbMsg[T](q: anorm.SimpleSql[Row], f: (SqlRow) => T)
  
object DbMsg
  
class DbActor extends Actor {
  def receive = {
    case DbMsg(q, f) => 
      sender ! DB.withConnection { implicit conn => {
        val list = (q() map f toList)
        if (list.isEmpty) None else Some(list.head)
      }     
    }
  }
}

class LibraryService {
  val dbActor = Akka.system.actorOf(Props[DbActor], name = "dbActor")
  implicit val timeout = Timeout(2 seconds)
 /**
   * Async query to check if the book exists
   */
  def getBookExists(bookId: String) = 
    (dbActor ? DbMsg(
        SQL("select count(1) cnt from libdb.book where book_id = {bookId}").on("bookId" -> bookId), 
        {row => row[Long]("cnt")})).mapTo[Option[Long]]
  
    /**
     * Async query to check if student exists
     */
  def getStudentExists(studentId: String) = 
    (dbActor ? DbMsg(
        SQL("select count(1) cnt from libdb.student_school where student_id = {studentId}").on("studentId" -> studentId), 
        {row => row[Long]("cnt")})).mapTo[Option[Long]]
  
  /**
   * Async query to get requested book path
   */
  def getBookPath(bookId: String, studentId: String) = {
    (dbActor ? DbMsg(
        SQL(
          """select content_path
               from libdb.book b
              where book_id in (
                    select book_id 
                      from libdb.student_school ss
                      join libdb.book_school bs
                        on ss.school_id = bs.school_id
                     where ss.student_id = {studentId}
                       and bs.book_id = {bookId})""")
        .on("studentId" -> studentId, "bookId" -> bookId), {row => row[String]("content_path")})).mapTo[Option[String]]
  }
  
  def getFile(path: String) = new java.io.File(path)
}