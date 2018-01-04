package models.daos

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OpenIDInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future

import tables.Tables
import tables.Tables._
/**
 * The DAO to store the OpenID information.
 */
class OpenIDInfoDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends DelegableAuthInfoDAO[OpenIDInfo] with HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.profile.api._

  def loginInfoQuery(loginInfo: LoginInfo) =
    Logininfo.filter(dbLoginInfo => dbLoginInfo.providerid === loginInfo.providerID
      &&
      dbLoginInfo.providerkey === loginInfo.providerKey)
  protected def openIDInfoQuery(loginInfo: LoginInfo) = for {
    dbLoginInfo <- loginInfoQuery(loginInfo)
    dbOpenIDInfo <- Openidinfo if dbOpenIDInfo.logininfoid === dbLoginInfo.id
  } yield dbOpenIDInfo

  protected def addAction(loginInfo: LoginInfo, authInfo: OpenIDInfo) =
    loginInfoQuery(loginInfo).result.head.flatMap { dbLoginInfo =>
      DBIO.seq(
        Openidinfo += OpenidinfoRow(authInfo.id, dbLoginInfo.id),
        Openidattributes ++= authInfo.attributes.map {
          case (key, value) => OpenidattributesRow(authInfo.id, key, value)
        })
    }.transactionally

  protected def updateAction(loginInfo: LoginInfo, authInfo: OpenIDInfo) =
    openIDInfoQuery(loginInfo).result.head.flatMap { dbOpenIDInfo =>
      DBIO.seq(
        Openidinfo.filter(_.id === dbOpenIDInfo.id) update dbOpenIDInfo.copy(id = authInfo.id),
        Openidattributes.filter(_.id === dbOpenIDInfo.id).delete,
        Openidattributes ++= authInfo.attributes.map {
          case (key, value) => OpenidattributesRow(authInfo.id, key, value)
        })
    }.transactionally

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[OpenIDInfo]] = {
    val query = openIDInfoQuery(loginInfo).joinLeft(Openidattributes).on(_.id === _.id)
    val result = db.run(query.result)
    result.map { openIDInfos =>
      if (openIDInfos.isEmpty) None
      else {
        val attrs = openIDInfos.collect { case (id, Some(attr)) => (attr.key, attr.value) }.toMap
        Some(OpenIDInfo(openIDInfos.head._1.id, attrs))
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
  def add(loginInfo: LoginInfo, authInfo: OpenIDInfo): Future[OpenIDInfo] =
    db.run(addAction(loginInfo, authInfo)).map(_ => authInfo)

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: OpenIDInfo): Future[OpenIDInfo] =
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
  def save(loginInfo: LoginInfo, authInfo: OpenIDInfo): Future[OpenIDInfo] = {
    val query = loginInfoQuery(loginInfo).joinLeft(Openidinfo).on(_.id === _.logininfoid)
    val action = query.result.head.flatMap {
      case (dbLoginInfo, Some(dbOpenIDInfo)) => updateAction(loginInfo, authInfo)
      case (dbLoginInfo, None)               => addAction(loginInfo, authInfo)
    }
    db.run(action).map(_ => authInfo)
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(loginInfo: LoginInfo): Future[Unit] = {
    // val attributeQuery = for {
    //  dbOpenIDInfo <- openIDInfoQuery(loginInfo)
    //  dbOpenIDAttributes <- slickOpenIDAttributes.filter(_.id === dbOpenIDInfo.id)
    //} yield dbOpenIDAttributes
    // Use subquery workaround instead of join because slick only supports selecting
    // from a single table for update/delete queries (https://github.com/slick/slick/issues/684).
    val openIDInfoSubQuery = Openidinfo.filter(_.logininfoid in loginInfoQuery(loginInfo).map(_.id))
    val attributeSubQuery = Openidattributes.filter(_.id in openIDInfoSubQuery.map(_.id))
    db.run((openIDInfoSubQuery.delete andThen attributeSubQuery.delete).transactionally).map(_ => ())
  }
}
