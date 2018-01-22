package models.daos

import java.sql.Timestamp
import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent._
import ExecutionContext.Implicits.global
import tables.Tables
import tables.Tables._

/**
 * Give access to the user object.
 */
class UserDAOImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with UserDAO {

  import dbConfig.profile.api._
  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def loginInfoQuery(loginInfo: LoginInfo) =
    Logininfo.filter(dbLoginInfo => dbLoginInfo.providerid === loginInfo.providerID
      &&
      dbLoginInfo.providerkey === loginInfo.providerKey)

  def find(loginInfo: LoginInfo) = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- Userlogininfo.filter(_.logininfoid === dbLoginInfo.id)
      dbUser <- Tables.User.filter(_.userid === dbUserLoginInfo.userid)
    } yield dbUser
    db.run(userQuery.result.headOption).map { dbUserOption =>
      dbUserOption.map { user =>
        User(UUID.fromString(user.userid),
          loginInfo,
          user.firstname,
          user.lastname,
          user.fullname,
          user.email,
          user.wechat,
          user.avatarurl,
          user.agreement,
          user.`type`,
          user.activate)
      }
    }
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */

  def find(userID: UUID) = {
    val query = for {
      dbUser <- Tables.User.filter(_.userid === userID.toString)
      dbUserLoginInfo <- Userlogininfo.filter(_.userid === dbUser.userid)
      dbLoginInfo <- Logininfo.filter(_.id === dbUserLoginInfo.logininfoid)
    } yield (dbUser, dbLoginInfo)
    db.run(query.result.headOption).map { resultOption =>
      resultOption.map {
        case (user, loginInfo) =>
          User(UUID.fromString(user.userid),
            LoginInfo(loginInfo.providerid,loginInfo.providerkey),
            user.firstname,
            user.lastname,
            user.fullname,
            user.email,
            user.wechat,
            user.avatarurl,
            user.agreement,
            user.`type`,
            user.activate)
      }
    }
  }
  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */

  def save(user: User) = {
    val dbUser = UserRow(user.userID.toString,
      user.firstName,
      user.lastName,
      user.fullName,
      user.email,
      user.avatarURL,
      user.wechat,
      user.`type`,
      user.agreement,
      user.activate,
      null
    )
    val dbLoginInfo = LogininfoRow(0, user.loginInfo.providerID, user.loginInfo.providerKey)
    // We don't have the LoginInfo id so we try to get it first.
    // If there is no LoginInfo yet for this user we retrieve the id on insertion.
    val loginInfoAction = {
      val retrieveLoginInfo = Logininfo.filter(
        info => info.providerid === user.loginInfo.providerID &&
          info.providerkey === user.loginInfo.providerKey).result.headOption
      val insertLoginInfo = Logininfo.returning(Logininfo.map(_.id)).
        into((info, id) => info.copy(id = id)) += dbLoginInfo
      for {
        loginInfoOption <- retrieveLoginInfo
        loginInfo <- loginInfoOption.map(DBIO.successful(_)).getOrElse(insertLoginInfo)
      } yield loginInfo
    }
    // combine database actions to be run sequentially
    val actions = (for {
      _ <- Tables.User.insertOrUpdate(dbUser)
      loginInfo <- loginInfoAction
      _ <- Userlogininfo += UserlogininfoRow(dbUser.userid, loginInfo.id, null)
    } yield ()).transactionally
    // run actions and return user afterwards
    db.run(actions).map(_ => user)
  }
}

