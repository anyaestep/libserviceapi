package test.controllers

import org.specs2.mutable._
import org.specs2.mock._
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.LibraryService
import play.api.mvc._

/**
 * Specs2 tests
 */
object LibrarySpec extends Specification with Mockito {
  import controllers._
  

  "Library Controller" should {
    
    "checkout should return JSON" in new WithApplication {
      val libraryService = mock[LibraryService]
      val libraryController = new Library() {
        override def library = libraryService
        override def getFile(path: String) = new java.io.File(getClass.getResource("/test/controllers/LibrarySpec.class").toURI)
      }
      // We have student 1 and book 1, we don't have student 2 or book 2
      libraryService.getStudentExists("1") returns Future({Some(1L)})
      libraryService.getBookExists("1") returns Future({Some(1L)})
      libraryService.getBookExists("2") returns Future({Some(0L)})
      libraryService.getStudentExists("2") returns Future({Some(0L)})
            
      // there is no student 2 but there is book 1 
      val resultNotFoundStudent = libraryController.checkout("2", "1")(FakeRequest())
      status(resultNotFoundStudent) must equalTo(play.api.test.Helpers.NOT_FOUND)

      // there is no student 2 and no book 2 
      val resultNotFoundStudentBook = libraryController.checkout("2", "2")(FakeRequest())
      status(resultNotFoundStudentBook) must equalTo(play.api.test.Helpers.NOT_FOUND)
      
      libraryService.getBookPath("1", "1") returns Future({Some("aaa")})
     
      // there is a book 1 and a student 1 and student can see book
      val resultOk = libraryController.checkout("1", "1")(FakeRequest())

      status(resultOk) must equalTo(OK)
      contentType(resultOk) must beSome("application/java")
      contentAsString(resultOk) must contain("test/controllers/LibrarySpec")
      
      libraryService.getBookExists("3") returns Future({Some(1L)})
      
      // studnet 1 can't see book 3
      val resultForbidden = libraryController.checkout("1", "3")(FakeRequest())
      status(resultForbidden) must equalTo(play.api.test.Helpers.FORBIDDEN)
    }

  }
}