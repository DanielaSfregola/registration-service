package daniela.sfregola.registration.service.dal

import daniela.sfregola.registration.service.models.UserModel

trait DBConfig {
  def userManager: UserModel
}

import scala.slick.driver.H2Driver
import scala.slick.driver.PostgresDriver
import scala.slick.session.Database

trait TestDB extends DBConfig {
  val userManager = new UserModel("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:servicetestdb", driver = "org.h2.Driver"))
  userManager.createDB
}

trait ProductionDB extends DBConfig {
  val userManager = new UserModel("PostgreSQL", new DAL(PostgresDriver),
    Database.forURL("jdbc:postgresql:test:daniela:sfregola",
      driver="org.postgresql.Driver",
      user="daniela",
      password="daniela"))
  userManager.createDB
}



