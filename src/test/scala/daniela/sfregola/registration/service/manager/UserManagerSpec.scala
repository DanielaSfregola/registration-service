package daniela.sfregola.registration.service.manager

import org.scalatest._
import daniela.sfregola.registration.service.dal.DAL
import org.scalatest.matchers.ClassicMatchers

class UserManagerSpec extends FlatSpec with ClassicMatchers {

  import scala.slick.driver.H2Driver
  import scala.slick.session.{ Database, Session }
  val userManager = new UserManager("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:testdb", driver = "org.h2.Driver"))

  val email = "daniela.sfregola@gmail.com"
  val password = "test"

  "A user manager" should "add a user" in {
      createDb
      addUser
      userManager.getAllUsers().size === 1
      dropDb
  }

  it should "hash the user password when adding the user" in {}
  it should "retrieve all the users" in {}
  it should "retrieve no users when empty" in {}
  it should "retrieve a user by email" in {}
  it should "not retrieve a user with an email that doesn't exist" in {}
  it should "retrieve a user by id" in {}
  it should "not retrieve a user with an id that doesn't exist" in {}
  it should "remove a user that doesn't exist" in {}
  it should "remove a user" in {}
  it should "make sure users are unique for id and email" in {}

  def addUser = userManager.addUser(email, password)
  def createDb = userManager.createDB
  def dropDb = userManager.dropDB
}