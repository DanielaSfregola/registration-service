package daniela.sfregola.registration.service.dal

import daniela.sfregola.registration.service.service.Model

trait DBConfig {
  def model: Model
}

import scala.slick.driver.H2Driver
import scala.slick.driver.PostgresDriver
import scala.slick.session.Database

trait TestDB extends DBConfig {
  val model = new Model("H2", new DAL(H2Driver),
    Database.forURL("jdbc:h2:mem:servicetestdb", driver = "org.h2.Driver"))
  model.createDB
}

trait ProductionDB extends DBConfig {
  val model = new Model("PostgreSQL", new DAL(PostgresDriver),
    Database.forURL("jdbc:postgresql:test:daniela:sfregola",
      driver="org.postgresql.Driver",
      user="daniela",
      password="daniela"))
  model.createDB
}



