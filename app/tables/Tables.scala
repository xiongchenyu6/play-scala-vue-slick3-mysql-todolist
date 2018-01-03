package tables
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[UserRow] = GR {
    prs =>
      import prs._
      UserRow.tupled((<<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** GetResult implicit for fetching UserlogininfoRow objects using plain SQL queries */
  implicit def GetResultUserlogininfoRow(implicit e0: GR[String], e1: GR[Long]): GR[UserlogininfoRow] = GR {
    prs =>
      import prs._
      UserlogininfoRow.tupled((<<[String], <<[Long]))
  }

  /** GetResult implicit for fetching LogininfoRow objects using plain SQL queries */
  implicit def GetResultLogininfoRow(implicit e0: GR[Long], e1: GR[String]): GR[LogininfoRow] = GR {
    prs =>
      import prs._
      LogininfoRow.tupled((<<[Long], <<[String], <<[String]))
  }

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Logininfo.schema, Oauth1info.schema, Oauth2info.schema, Openidattributes.schema, Openidinfo.schema, Passwordinfo.schema, PlayEvolutions.schema, Project.schema, Task.schema, User.schema, Userlogininfo.schema).reduceLeft(_ ++ _)
  /** Collection-like TableQuery object for table Logininfo */
  lazy val Logininfo = new TableQuery(tag => new Logininfo(tag))
  /** Collection-like TableQuery object for table Oauth1info */
  lazy val Oauth1info = new TableQuery(tag => new Oauth1info(tag))

  /** GetResult implicit for fetching Oauth1infoRow objects using plain SQL queries */
  implicit def GetResultOauth1infoRow(implicit e0: GR[Long], e1: GR[String]): GR[Oauth1infoRow] = GR {
    prs =>
      import prs._
      Oauth1infoRow.tupled((<<[Long], <<[String], <<[String], <<[Long]))
  }

  /** Collection-like TableQuery object for table Oauth2info */
  lazy val Oauth2info = new TableQuery(tag => new Oauth2info(tag))
  /** Collection-like TableQuery object for table Openidattributes */
  lazy val Openidattributes = new TableQuery(tag => new Openidattributes(tag))
  /** Collection-like TableQuery object for table Openidinfo */
  lazy val Openidinfo = new TableQuery(tag => new Openidinfo(tag))

  /** GetResult implicit for fetching Oauth2infoRow objects using plain SQL queries */
  implicit def GetResultOauth2infoRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Int]]): GR[Oauth2infoRow] = GR {
    prs =>
      import prs._
      Oauth2infoRow.tupled((<<[Long], <<[String], <<?[String], <<?[Int], <<?[String], <<[Long]))
  }

  /** Collection-like TableQuery object for table Passwordinfo */
  lazy val Passwordinfo = new TableQuery(tag => new Passwordinfo(tag))
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
  /** Collection-like TableQuery object for table Userlogininfo */
  lazy val Userlogininfo = new TableQuery(tag => new Userlogininfo(tag))

  /** GetResult implicit for fetching OpenidattributesRow objects using plain SQL queries */
  implicit def GetResultOpenidattributesRow(implicit e0: GR[String]): GR[OpenidattributesRow] = GR {
    prs =>
      import prs._
      OpenidattributesRow.tupled((<<[String], <<[String], <<[String]))
  }

  /** Entity class storing rows of table Logininfo
    *
    * @param id          Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    * @param providerid  Database column providerID SqlType(VARCHAR), Length(255,true)
    * @param providerkey Database column providerKey SqlType(VARCHAR), Length(255,true) */
  case class LogininfoRow(id: Long, providerid: String, providerkey: String)

  /** Table description of table logininfo. Objects of this class serve as prototypes for rows in queries. */
  class Logininfo(_tableTag: Tag) extends profile.api.Table[LogininfoRow](_tableTag, Some("todo"), "logininfo") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column providerID SqlType(VARCHAR), Length(255,true) */
    val providerid: Rep[String] = column[String]("providerID", O.Length(255, varying = true))
    /** Database column providerKey SqlType(VARCHAR), Length(255,true) */
    val providerkey: Rep[String] = column[String]("providerKey", O.Length(255, varying = true))

    def * = (id, providerid, providerkey) <> (LogininfoRow.tupled, LogininfoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(providerid), Rep.Some(providerkey)).shaped.<>({ r => import r._; _1.map(_ => LogininfoRow.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** Entity class storing rows of table Oauth1info
    *
    * @param id          Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    * @param token       Database column token SqlType(VARCHAR), Length(255,true)
    * @param secret      Database column secret SqlType(VARCHAR), Length(255,true)
    * @param logininfoid Database column loginInfoId SqlType(BIGINT) */
  case class Oauth1infoRow(id: Long, token: String, secret: String, logininfoid: Long)

  /** GetResult implicit for fetching OpenidinfoRow objects using plain SQL queries */
  implicit def GetResultOpenidinfoRow(implicit e0: GR[String], e1: GR[Long]): GR[OpenidinfoRow] = GR {
    prs =>
      import prs._
      OpenidinfoRow.tupled((<<[String], <<[Long]))
  }

  /** Table description of table oauth1info. Objects of this class serve as prototypes for rows in queries. */
  class Oauth1info(_tableTag: Tag) extends profile.api.Table[Oauth1infoRow](_tableTag, Some("todo"), "oauth1info") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column token SqlType(VARCHAR), Length(255,true) */
    val token: Rep[String] = column[String]("token", O.Length(255, varying = true))
    /** Database column secret SqlType(VARCHAR), Length(255,true) */
    val secret: Rep[String] = column[String]("secret", O.Length(255, varying = true))
    /** Database column loginInfoId SqlType(BIGINT) */
    val logininfoid: Rep[Long] = column[Long]("loginInfoId")

    def * = (id, token, secret, logininfoid) <> (Oauth1infoRow.tupled, Oauth1infoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(token), Rep.Some(secret), Rep.Some(logininfoid)).shaped.<>({ r => import r._; _1.map(_ => Oauth1infoRow.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** Entity class storing rows of table Oauth2info
    *
    * @param id           Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    * @param accesstoken  Database column accesstoken SqlType(VARCHAR), Length(255,true)
    * @param tokentype    Database column tokentype SqlType(VARCHAR), Length(255,true), Default(None)
    * @param expiresin    Database column expiresin SqlType(INT), Default(None)
    * @param refreshtoken Database column refreshtoken SqlType(VARCHAR), Length(255,true), Default(None)
    * @param logininfoid  Database column logininfoid SqlType(BIGINT) */
  case class Oauth2infoRow(id: Long, accesstoken: String, tokentype: Option[String] = None, expiresin: Option[Int] = None, refreshtoken: Option[String] = None, logininfoid: Long)

  /** Table description of table oauth2info. Objects of this class serve as prototypes for rows in queries. */
  class Oauth2info(_tableTag: Tag) extends profile.api.Table[Oauth2infoRow](_tableTag, Some("todo"), "oauth2info") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column accesstoken SqlType(VARCHAR), Length(255,true) */
    val accesstoken: Rep[String] = column[String]("accesstoken", O.Length(255, varying = true))
    /** Database column tokentype SqlType(VARCHAR), Length(255,true), Default(None) */
    val tokentype: Rep[Option[String]] = column[Option[String]]("tokentype", O.Length(255, varying = true), O.Default(None))
    /** Database column expiresin SqlType(INT), Default(None) */
    val expiresin: Rep[Option[Int]] = column[Option[Int]]("expiresin", O.Default(None))
    /** Database column refreshtoken SqlType(VARCHAR), Length(255,true), Default(None) */
    val refreshtoken: Rep[Option[String]] = column[Option[String]]("refreshtoken", O.Length(255, varying = true), O.Default(None))
    /** Database column logininfoid SqlType(BIGINT) */
    val logininfoid: Rep[Long] = column[Long]("logininfoid")

    def * = (id, accesstoken, tokentype, expiresin, refreshtoken, logininfoid) <> (Oauth2infoRow.tupled, Oauth2infoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(accesstoken), tokentype, expiresin, refreshtoken, Rep.Some(logininfoid)).shaped.<>({ r => import r._; _1.map(_ => Oauth2infoRow.tupled((_1.get, _2.get, _3, _4, _5, _6.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** GetResult implicit for fetching PasswordinfoRow objects using plain SQL queries */
  implicit def GetResultPasswordinfoRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Long]): GR[PasswordinfoRow] = GR {
    prs =>
      import prs._
      PasswordinfoRow.tupled((<<[String], <<[String], <<?[String], <<[Long]))
  }

  /** Entity class storing rows of table Openidattributes
    *
    * @param id    Database column id SqlType(VARCHAR), Length(255,true)
    * @param key   Database column key SqlType(VARCHAR), Length(255,true)
    * @param value Database column value SqlType(VARCHAR), Length(255,true) */
  case class OpenidattributesRow(id: String, key: String, value: String)

  /** Table description of table openidattributes. Objects of this class serve as prototypes for rows in queries. */
  class Openidattributes(_tableTag: Tag) extends profile.api.Table[OpenidattributesRow](_tableTag, Some("todo"), "openidattributes") {
    /** Database column id SqlType(VARCHAR), Length(255,true) */
    val id: Rep[String] = column[String]("id", O.Length(255, varying = true))
    /** Database column key SqlType(VARCHAR), Length(255,true) */
    val key: Rep[String] = column[String]("key", O.Length(255, varying = true))
    /** Database column value SqlType(VARCHAR), Length(255,true) */
    val value: Rep[String] = column[String]("value", O.Length(255, varying = true))

    def * = (id, key, value) <> (OpenidattributesRow.tupled, OpenidattributesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(key), Rep.Some(value)).shaped.<>({ r => import r._; _1.map(_ => OpenidattributesRow.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param hash Database column hash SqlType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column applied_at SqlType(TIMESTAMP)
   *  @param applyScript Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param revertScript Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends profile.api.Table[PlayEvolutionsRow](_tableTag, Some("todo"), "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(VARCHAR), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(TIMESTAMP) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column revert_script SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Length(16777215,varying=true), O.Default(None))
    /** Database column state SqlType(VARCHAR), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Length(16777215,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Project
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(200,true)
   *  @param status Database column status SqlType(ENUM), Length(5,false) */
  case class ProjectRow(id: Long, name: String, status: String)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Long], e1: GR[String]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, Some("todo"), "project") {
    def * = (id, name, status) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status)).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column status SqlType(ENUM), Length(5,false) */
    val status: Rep[String] = column[String]("status", O.Length(5,varying=false))
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))

  /** Entity class storing rows of table Task
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(200,true)
   *  @param due Database column due SqlType(DATETIME)
   *  @param projectid Database column projectId SqlType(BIGINT) */
  case class TaskRow(id: Long, name: String, due: java.sql.Timestamp, projectid: Long)
  /** GetResult implicit for fetching TaskRow objects using plain SQL queries */
  implicit def GetResultTaskRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TaskRow] = GR{
    prs => import prs._
    TaskRow.tupled((<<[Long], <<[String], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table task. Objects of this class serve as prototypes for rows in queries. */
  class Task(_tableTag: Tag) extends profile.api.Table[TaskRow](_tableTag, Some("todo"), "task") {
    def * = (id, name, due, projectid) <> (TaskRow.tupled, TaskRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(due), Rep.Some(projectid)).shaped.<>({r=>import r._; _1.map(_=> TaskRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column due SqlType(DATETIME) */
    val due: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("due")
    /** Database column projectId SqlType(BIGINT) */
    val projectid: Rep[Long] = column[Long]("projectId")

    /** Foreign key referencing Project (database name task_ibfk_1) */
    lazy val projectFk = foreignKey("task_ibfk_1", projectid, Project)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Task */
  lazy val Task = new TableQuery(tag => new Task(tag))

  /** Entity class storing rows of table Openidinfo
    *
    * @param id          Database column id SqlType(VARCHAR), PrimaryKey, Length(255,true)
    * @param logininfoid Database column logininfoid SqlType(BIGINT) */
  case class OpenidinfoRow(id: String, logininfoid: Long)

  /** Table description of table openidinfo. Objects of this class serve as prototypes for rows in queries. */
  class Openidinfo(_tableTag: Tag) extends profile.api.Table[OpenidinfoRow](_tableTag, Some("todo"), "openidinfo") {
    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))
    /** Database column logininfoid SqlType(BIGINT) */
    val logininfoid: Rep[Long] = column[Long]("logininfoid")

    def * = (id, logininfoid) <> (OpenidinfoRow.tupled, OpenidinfoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(logininfoid)).shaped.<>({ r => import r._; _1.map(_ => OpenidinfoRow.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** Entity class storing rows of table Passwordinfo
    *
    * @param hasher      Database column hasher SqlType(VARCHAR), Length(255,true)
    * @param password    Database column password SqlType(VARCHAR), Length(255,true)
    * @param salt        Database column salt SqlType(VARCHAR), Length(255,true), Default(None)
    * @param logininfoid Database column loginInfoId SqlType(BIGINT) */
  case class PasswordinfoRow(hasher: String, password: String, salt: Option[String] = None, logininfoid: Long)

  /** Table description of table passwordinfo. Objects of this class serve as prototypes for rows in queries. */
  class Passwordinfo(_tableTag: Tag) extends profile.api.Table[PasswordinfoRow](_tableTag, Some("todo"), "passwordinfo") {
    /** Database column hasher SqlType(VARCHAR), Length(255,true) */
    val hasher: Rep[String] = column[String]("hasher", O.Length(255, varying = true))
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255, varying = true))
    /** Database column salt SqlType(VARCHAR), Length(255,true), Default(None) */
    val salt: Rep[Option[String]] = column[Option[String]]("salt", O.Length(255, varying = true), O.Default(None))
    /** Database column loginInfoId SqlType(BIGINT) */
    val logininfoid: Rep[Long] = column[Long]("loginInfoId")

    def * = (hasher, password, salt, logininfoid) <> (PasswordinfoRow.tupled, PasswordinfoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(hasher), Rep.Some(password), salt, Rep.Some(logininfoid)).shaped.<>({ r => import r._; _1.map(_ => PasswordinfoRow.tupled((_1.get, _2.get, _3, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** Entity class storing rows of table User
    *
    * @param userid    Database column userID SqlType(VARCHAR), PrimaryKey, Length(255,true)
    * @param frstname  Database column frstName SqlType(VARCHAR), Length(255,true), Default(None)
    * @param lastname  Database column lastName SqlType(VARCHAR), Length(255,true), Default(None)
    * @param fullname  Database column fullName SqlType(VARCHAR), Length(255,true), Default(None)
    * @param email     Database column email SqlType(VARCHAR), Length(255,true), Default(None)
    * @param avatarurl Database column avatarURL SqlType(VARCHAR), Length(255,true), Default(None) */
  case class UserRow(userid: String, frstname: Option[String] = None, lastname: Option[String] = None, fullname: Option[String] = None, email: Option[String] = None, avatarurl: Option[String] = None)

  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, Some("todo"), "user") {
    /** Database column userID SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val userid: Rep[String] = column[String]("userID", O.PrimaryKey, O.Length(255, varying = true))
    /** Database column frstName SqlType(VARCHAR), Length(255,true), Default(None) */
    val frstname: Rep[Option[String]] = column[Option[String]]("frstName", O.Length(255, varying = true), O.Default(None))
    /** Database column lastName SqlType(VARCHAR), Length(255,true), Default(None) */
    val lastname: Rep[Option[String]] = column[Option[String]]("lastName", O.Length(255, varying = true), O.Default(None))
    /** Database column fullName SqlType(VARCHAR), Length(255,true), Default(None) */
    val fullname: Rep[Option[String]] = column[Option[String]]("fullName", O.Length(255, varying = true), O.Default(None))
    /** Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(255, varying = true), O.Default(None))
    /** Database column avatarURL SqlType(VARCHAR), Length(255,true), Default(None) */
    val avatarurl: Rep[Option[String]] = column[Option[String]]("avatarURL", O.Length(255, varying = true), O.Default(None))

    def * = (userid, frstname, lastname, fullname, email, avatarurl) <> (UserRow.tupled, UserRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userid), frstname, lastname, fullname, email, avatarurl).shaped.<>({ r => import r._; _1.map(_ => UserRow.tupled((_1.get, _2, _3, _4, _5, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /** Entity class storing rows of table Userlogininfo
    *
    * @param userid      Database column userID SqlType(VARCHAR), Length(255,true)
    * @param logininfoid Database column loginInfoId SqlType(BIGINT) */
  case class UserlogininfoRow(userid: String, logininfoid: Long)

  /** Table description of table userlogininfo. Objects of this class serve as prototypes for rows in queries. */
  class Userlogininfo(_tableTag: Tag) extends profile.api.Table[UserlogininfoRow](_tableTag, Some("todo"), "userlogininfo") {
    /** Database column userID SqlType(VARCHAR), Length(255,true) */
    val userid: Rep[String] = column[String]("userID", O.Length(255, varying = true))
    /** Database column loginInfoId SqlType(BIGINT) */
    val logininfoid: Rep[Long] = column[Long]("loginInfoId")

    def * = (userid, logininfoid) <> (UserlogininfoRow.tupled, UserlogininfoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userid), Rep.Some(logininfoid)).shaped.<>({ r => import r._; _1.map(_ => UserlogininfoRow.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }
}
