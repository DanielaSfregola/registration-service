package daniela.sfregola.registration.service.domain

import daniela.sfregola.registration.service.dal.Profile
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.{LoggerFactory, Logger}

case class User(email: String, hashedPassword: String, id: Option[Long] = None)

trait UserComponent { this: Profile =>
  import profile.simple._

  val logger: Logger = LoggerFactory.getLogger(this.getClass);

  object Users extends Table[(String, String, Option[Long])]("USER") {

    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def email = column[String]("EMAIL", O.NotNull)
    def hashedPassword = column[String]("HASHED_PASSWORD", O.NotNull)
    def * = email ~ hashedPassword ~ id
    def idx = index("idx_user", (id, email), unique = true)
    def mapped = email ~ hashedPassword ~ id <> ({ (email, password, id) => User(email, password, id) },
                                           { u: User => Some((u.email, u.hashedPassword, u.id)) })

    val autoInc = email ~ hashedPassword returning id into {
                                case (credentials, id) => User(credentials._1, credentials._2, id)
                                }

    def queryFindAll = for (u <- Users) yield u

    def queryFindOneById(id: Long) = for (u <- Users if u.id === id) yield u mapped

    def queryFindOneByEmail(email: String) = for (u <- Users if u.email === email) yield u mapped

    def insert = email ~ hashedPassword <> ({ (email, password) => User(email, password, None) },
                                        { u: User => Some((u.email, u.hashedPassword)) })

    def deleteById(id: Long)(implicit session: Session) = (for (u <- Users if u.id === id) yield u).delete

    def findAll(implicit session: Session): List[User] = {
      queryFindAll.list map { u => User(email = u._1, hashedPassword = u._2, id = u._3) }
    }

    def findOneById(id: Long)(implicit session: Session): Option[User] = {
      queryFindOneById(id).firstOption
    }

    def findOneByEmail(email: String)(implicit session: Session): Option[User] = {
      queryFindOneByEmail(email).firstOption
    }

    def insert(email: String, password: String)(implicit session: Session): User = {
      findOneByEmail(email) match {
        case user: Some[User] => {
          logger.error(email + ": a user with this email already exists")
          throw new IllegalArgumentException(email + ": a user with this email already exists")
        }
        case _ => {
            def hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            autoInc.insert(email, hashedPassword)
        }
      }
    }

    def delete(user: User)(implicit session: Session) =  {
      user.id match {
        case id: Some[Long] => deleteById(id.get)
        case _ => logger.warn("Requested to delete unexisting user: doing nothing...")
      }
    }

  }
}