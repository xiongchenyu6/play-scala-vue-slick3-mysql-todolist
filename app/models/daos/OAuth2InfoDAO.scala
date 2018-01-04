package models.daos

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future

import tables.Tables
import tables.Tables._
/**
 * The DAO to store the OAuth2 information.
 */
class OAuth2InfoDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends DelegableAuthInfoDAO[OAuth2Info] with HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.profile.api._
  def loginInfoQuery(loginInfo: LoginInfo) =
    Logininfo.filter(dbLoginInfo => dbLoginInfo.providerid === loginInfo.providerID
      &&
      dbLoginInfo.providerkey === loginInfo.providerKey)
  protected def oAuth2InfoQuery(loginInfo: LoginInfo) = for {
    dbLoginInfo <- loginInfoQuery(loginInfo)
    dbOAuth2Info <- Oauth2info if dbOAuth2Info.logininfoid === dbLoginInfo.id
  } yield dbOAuth2Info

  // Use subquery workaround instead of join to get authinfo because slick only supports selecting
  // from a single table for update/delete queries (https://github.com/slick/slick/issues/684).
  protected def oAuth2InfoSubQuery(loginInfo: LoginInfo) =
    Oauth2info.filter(_.logininfoid in loginInfoQuery(loginInfo).map(_.id))

  protected def addAction(loginInfo: LoginInfo, authInfo: OAuth2Info) =
    loginInfoQuery(loginInfo).result.head.flatMap { dbLoginInfo =>
      Oauth2info += Oauth2infoRow(
        0,
        authInfo.accessToken,
        authInfo.tokenType,
        authInfo.expiresIn,
        authInfo.refreshToken,
        dbLoginInfo.id)
    }.transactionally

  def updateAction(loginInfo: LoginInfo, authInfo: OAuth2Info) =
    oAuth2InfoSubQuery(loginInfo).
      map(dbOAuth2Info => (dbOAuth2Info.accesstoken, dbOAuth2Info.tokentype, dbOAuth2Info.expiresin, dbOAuth2Info.refreshtoken)).
      update((authInfo.accessToken, authInfo.tokenType, authInfo.expiresIn, authInfo.refreshToken))

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[OAuth2Info]] = {
    val result = db.run(oAuth2InfoQuery(loginInfo).result.headOption)
    result.map { dbOAuth2InfoOption =>
      dbOAuth2InfoOption.map { dbOAuth2Info =>
        OAuth2Info(dbOAuth2Info.accesstoken, dbOAuth2Info.tokentype, dbOAuth2Info.expiresin, dbOAuth2Info.refreshtoken)
      }
    }
  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  def add(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] =
    db.run(addAction(loginInfo, authInfo)).map(_ => authInfo)

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] =
    db.run(updateAction(loginInfo, authInfo)).map(_ => authInfo)

  /**
   * Saves the auth info for the given login info.
   *
   * This method either adds the auth info if it doesn't exists or it updates the auth info
   * if it already exists.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  def save(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = {
    val query = for {
      result <- loginInfoQuery(loginInfo).joinLeft(Oauth2info).on(_.id === _.logininfoid)
    } yield result
    val action = query.result.head.flatMap {
      case (dbLoginInfo, Some(dbOAuth2Info)) => updateAction(loginInfo, authInfo)
      case (dbLoginInfo, None)               => addAction(loginInfo, authInfo)
    }.transactionally
    db.run(action).map(_ => authInfo)
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(loginInfo: LoginInfo): Future[Unit] =
    db.run(oAuth2InfoSubQuery(loginInfo).delete).map(_ => ())
}
