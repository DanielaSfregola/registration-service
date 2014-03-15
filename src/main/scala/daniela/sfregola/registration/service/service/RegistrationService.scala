package daniela.sfregola.registration.service.service

import scala.slick.session.Database
import daniela.sfregola.registration.service.dal.DAL
import daniela.sfregola.registration.service.domain.User

class RegistrationService(name: String, dal: DAL, db: Database) {

  import dal._

  implicit val implicitSession = db.createSession

  def createDB = dal.create

  def dropDB = dal.drop

  def purgeDB = dal.purge

  def getAllUsers(): List[User] = {
    val result = Users.findAll
    println("Got users: " + result)
    result
  }

  def getUserById(id: Long): Option[User] = {
    val result = Users.findOneById(id)
    result match {
      case Some(User) => println("Found user " + result)
      case _ => println("No user found with id " + id)
    }
    result
  }

  def getUserByEmail(email: String): Option[User] = {
    val result = Users.findOneByEmail(email)
    result match {
      case Some(User) => println("Found user " + result)
      case _ => println("No user found with email " + email)
    }
    result
  }

  def addUser(user: User): User = {
    val result = Users.insert(user)
    println("Inserted user: " + result)
    result
  }

  def removeUser(user: User) {
    Users.delete(user)
    println("Deleted user: " + user)
  }
}
