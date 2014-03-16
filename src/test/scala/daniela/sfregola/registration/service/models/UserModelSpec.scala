package daniela.sfregola.registration.service.models

import org.scalatest._
import daniela.sfregola.registration.service.dal.DAL
import org.scalatest.matchers.MustMatchers
import org.mindrot.jbcrypt.BCrypt
import daniela.sfregola.registration.service.domain.User

class UserModelSpec extends FlatSpec with MustMatchers with BeforeAndAfter {

  import scala.slick.driver.H2Driver
  import scala.slick.session.Database
  val userManager = new UserModel("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:testdb", driver = "org.h2.Driver"))

  val email = "daniela.sfregola@gmail.com"
  val password = "password"

  before {
    userManager.createDB
  }

  after {
    userManager.dropDB
  }

  "A user model" should "add a user" in {
      userManager.addUser(email, password)
      def allUsers = userManager.getAllUsers()
      allUsers.size must be (1)
  }

  it should "hash the user password when adding the user" in {
    userManager.addUser(email, password)
    def user = userManager.getUserByEmail(email)
    def hashedPassword = user.get.hashedPassword
    BCrypt.checkpw(password, hashedPassword) must be (true)
  }

  it should "retrieve all the users" in {
    userManager.addUser(email, password)
    userManager.addUser("test@test.test", "test")
    userManager.getAllUsers().size must be (2)
  }

  it should "retrieve no users when empty" in {
    userManager.getAllUsers().size must be (0)
  }

  it should "retrieve a user by email" in {
    userManager.addUser(email, password)
    userManager.getUserByEmail(email) must not be (None)
  }

  it should "not retrieve a user with an email that doesn't exist" in {
    userManager.addUser(email, password)
    userManager.getUserByEmail("notthere@test.test") must be (None)
  }

  it should "retrieve a user by id" in {
    userManager.addUser(email, password)
    def user = userManager.getUserByEmail(email).get
    def id = user.id.get
    userManager.getUserById(id) must not be (None)
  }

  it should "not retrieve a user with an id that doesn't exist" in {
    userManager.addUser(email, password)
    def user = userManager.getUserByEmail(email).get
    def idNotThere = user.id.get +1
    userManager.getUserById(idNotThere) must be (None)
  }

  it should "remove a user" in {
    userManager.addUser(email, password)
    userManager.getAllUsers().size must be (1)
    def user = userManager.getUserByEmail(email).get
    userManager.removeUser(user)
    userManager.getAllUsers().size must be (0)
  }

  it should "remove a user that doesn't exist" in {
    userManager.addUser(email, password)
    userManager.getAllUsers().size must be (1)
    def userNotThere = new User("notthere@test.test", "test", Some(2))
    userManager.removeUser(userNotThere)
    userManager.getAllUsers().size must be (1)
  }

  it should "make sure users have unique email" in {
    userManager.addUser(email, password)
    intercept[IllegalArgumentException] {
      userManager.addUser(email, password)
    }
    userManager.getAllUsers().size must be (1)
  }

}