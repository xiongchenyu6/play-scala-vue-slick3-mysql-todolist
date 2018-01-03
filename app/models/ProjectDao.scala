package models
import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import tables.Tables._

import scala.concurrent.Future

class ProjectRepo @Inject()(taskRepo: TaskRepo)(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{

  import dbConfig.profile.api._

  def all():Future[Seq[ProjectRow]] = db.run(Project.result)

  def insert(newProject: ProjectRow) = db.run(Project += newProject)

}
