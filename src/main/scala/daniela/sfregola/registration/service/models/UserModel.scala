package daniela.sfregola.registration.service.models

import scala.slick.session.Database
import daniela.sfregola.registration.service.dal.DAL
import daniela.sfregola.registration.service.domain.User
import org.slf4j.{LoggerFactory, Logger}

class UserModel(name: String, dal: DAL, db: Database) {

  import dal._

  val logger: Logger = LoggerFactory.getLogger(this.getClass);

  implicit val implicitSession = db.createSession

  def createDB = dal.create

  def dropDB = dal.drop

  def purgeDB = dal.purge

  def getAllUsers(): List[User] = {
    val result = Users.findAll
    logger.info("Got users: " + result)
    result
  }

  def getUserById(id: Long): Option[User] = {
    val result = Users.findOneById(id)
    result match {
      case user: Some[User] => logger.info("Found user " + user.get)
      case _ => logger.info("No user found with id " + id)
    }
    result
  }

  def getUserByEmail(email: String): Option[User] = {
    val result = Users.findOneByEmail(email)
    result match {
      case user: Some[User] => logger.info("Found user " + user.get)
      case _ => logger.info("No user found with email " + email)
    }
    result
  }

  def addUser(email: String, password: String): User = {
    val result = Users.insert(email, password)
    logger.info("Inserted user: " + result)
    result
  }

  def removeUser(user: User) {
    Users.delete(user)
    logger.info("Deleted user: " + user)
  }
}
