package daniela.sfregola.registration.service.domain

import scala.slick.driver.H2Driver
import scala.slick.session.{ Database, Session }
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Model(name: String, dal: DAL, db: Database) {

  import dal._
  import dal.profile.simple._

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
}
