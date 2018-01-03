package models

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import tables.Tables._

import scala.concurrent.Future

class TaskRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.profile.api._

  def all() = db.run(Task.result)

  def insert(NewTask: TaskRow) = db.run(Task += NewTask)

  def _taskByGroupId(id:Long) = for {
    task <- Task.filter(_.id === id)
    project <- task.projectFk
  }yield (project)

  def taskByGroupId(id:Long):Future[Seq[ProjectRow]] = db.run(_taskByGroupId(id).result)
}
