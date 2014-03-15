package daniela.sfregola.registration.service.domain

// TODO - password to be hashed with random salt
// TODO - email as unique
// TODO - removeUser
case class User(email: String, password: String, id: Option[Long] = None)

trait UserComponent { this: Profile =>
  import profile.simple._

  object Users extends Table[(String, String, Option[Long])]("USER") {

    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def email = column[String]("EMAIL", O.NotNull)
    def password = column[String]("PASSWORD", O.NotNull)
    def * = email ~ password ~ id
    def mapped = email ~ password ~ id <> ({ (email, password, id) => User(email, password, id) },
                                           { x: User => Some((x.email, x.password, x.id)) })

    val autoInc = email ~ password returning id into {
                                case (credentials, id) => User(credentials._1, credentials._2, id)
                                }

    def forFindAll = for (x <- Users) yield x

    def forFindOneById(id: Long) = for (x <- User if x.id == id) yield x

    def forFindOneByEmail(email: String) = for (x <- User if x.email == email) yield x

    def insert = email ~ password <> ({ (email, password) => User(email, password, None) },
                                        { x: User => Some((x.email, x.password)) })


    def findAll(implicit session: Session): List[User] = {
      forFindAll.list map { x => User(email = x._1, password = x._2, id = x._3) }
    }

    def findOneById(id: Long)(implicit session: Session): Option[User] = {
      forFindOneById(id).firstOption
    }

    def findOneByEmail(email: String)(implicit session: Session): Option[User] = {
      forFindOneByEmail(email).firstOption
    }

    def insert(user: User)(implicit session: Session): User = {
      autoInc.insert(user.email, user.password)
    }
  }
}