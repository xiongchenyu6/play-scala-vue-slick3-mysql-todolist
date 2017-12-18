package models
import javax.inject.Inject

import play.api.data.Form
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick._
import slick.dbio.Effect.Read
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import tables.Tables._

class ProjectRepo @Inject()(taskRepo: TaskRepo)(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  import dbConfig.profile.api._

  def all():Future[Seq[ProjectRow]] = db.run(Project.result)

  def insert(newProject: ProjectRow) = db.run(Project += newProject)

}
