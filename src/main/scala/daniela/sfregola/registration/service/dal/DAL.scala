package daniela.sfregola.registration.service.dal

import scala.slick.driver.ExtendedProfile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import daniela.sfregola.registration.service.domain.UserComponent

trait Profile {
  val profile: ExtendedProfile
}

class DAL(override val profile: ExtendedProfile) extends UserComponent with Profile {
  import profile.simple._

  def ddl = (Users.ddl)

  def create(implicit session: Session): Unit = {
    try {
      ddl.create
    } catch {
      case e: Exception => logger.warn("Could not create database... assuming it already exists")
    }
  }

  def drop(implicit session: Session): Unit = {
    try {
      ddl.drop
    } catch {
      case e: Exception => logger.error("Could not drop database")
    }
  }

  def purge(implicit session: Session) = { drop; create }
}