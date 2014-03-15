package daniela.sfregola.registration.service.domain

import daniela.sfregola.registration.service.dal.Profile

// TODO - tests
case class User(email: String, hashedPassword: String, id: Option[Long] = None)

trait UserComponent { this: Profile =>
  import profile.simple._

  object Users extends Table[(String, String, Option[Long])]("USER") {

    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def email = column[String]("EMAIL", O.NotNull)
    def hashedPassword = column[String]("HASHED_PASSWORD", O.NotNull)
    def * = email ~ hashedPassword ~ id
    def idx = index("idx_user", (id, email), unique = true)
    def mapped = email ~ hashedPassword ~ id <> ({ (email, password, id) => User(email, password, id) },
                                           { x: User => Some((x.email, x.hashedPassword, x.id)) })

    val autoInc = email ~ hashedPassword returning id into {
                                case (credentials, id) => User(credentials._1, credentials._2, id)
                                }

    def forFindAll = for (x <- Users) yield x

    def forFindOneById(id: Long) = for (x <- User if x.id == id) yield x

    def forFindOneByEmail(email: String) = for (x <- User if x.email == email) yield x

    def insert = email ~ hashedPassword <> ({ (email, password) => User(email, password, None) },
                                        { x: User => Some((x.email, x.hashedPassword)) })

    def findAll(implicit session: Session): List[User] = {
      forFindAll.list map { x => User(email = x._1, hashedPassword = x._2, id = x._3) }
    }

    def findOneById(id: Long)(implicit session: Session): Option[User] = {
      forFindOneById(id).firstOption
    }

    def findOneByEmail(email: String)(implicit session: Session): Option[User] = {
      forFindOneByEmail(email).firstOption
    }

    def insert(email: String, password: String)(implicit session: Session): User = {
      def hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
      autoInc.insert(email, hashedPassword)
    }

    def delete(user: User)(implicit session: Session) {
      Users.where(_ === user).delete
    }

  }
}