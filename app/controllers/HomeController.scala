package controllers

import javax.inject._

import models.{ProjectRepo, TaskRepo}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import play.api.mvc._
import slick.jdbc.JdbcProfile
import tables.Tables._
import play.filters.csrf._

import scala.concurrent.{ExecutionContext, Future}
/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext ,protected val dbConfigProvider: DatabaseConfigProvider, projectRepo: ProjectRepo, taskRepo: TaskRepo, cc: ControllerComponents) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile
  ] {

  import dbConfig.profile.api._
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action.async { implicit request: Request[AnyContent] =>

    val allProjectSql = Project.result;

    db.run(allProjectSql).map {projects =>
      Ok(views.html.index(projects))
    }
      .recover{
        case e: Exception =>
          Ok(e.toString)
      }
    //    taskRepo.taskByGroupId(1L).map {data =>
    //      Ok(views.html.index(data(0).id,data(0).name))
    //    }.recover {
    //      case e: Exception =>
    //        Ok(e.toString)
    //    }
  }
  def createProject() = Action.async(parse.json) { implicit request: Request[JsValue] =>

      val name = (request.body \ "project").as[String]
      val projectSql = Project += ProjectRow(0, name, "ready")
      db.run(projectSql).map { data =>
        Ok("success")
      }
        .recover {
          case e: Exception =>
            Ok(e.toString)
        }
    }

  def updateProject() = Action.async(parse.json) {implicit request: Request[JsValue] =>
    val id = (request.body \ "id").as[Long]
    val name = (request.body \ "name").as[String]
    val status = (request.body \ "status").as[String]
    val updateSql = Project.filter(_.id === id).map(project => (project.name, project.status))

    db.run(updateSql.update((name,status))).map { data =>
      print(status)
      Ok("success")
    }
      .recover {
        case e: Exception =>
          Ok(e.toString)
      }
  }
  def generateTable() = Action { implicit request: Request[AnyContent] =>

    val slickDriver = "slick.jdbc.MySQLProfile"   // -- ①
    val jdbcDriver = "com.mysql.cj.jdbc.Driver"          // -- ②
    val url =                                         // -- ③
    """jdbc:mysql://127.0.0.1:3306/todo?useSSL=false&nullNamePatternMatchesAll=true""".stripMargin
    val user = "root"
    val password = " "
    val outputFolder = "./app"                          // -- ④
  val pkg = "tables"                // -- ⑤

    slick.codegen.SourceCodeGenerator.main(
      Array(
        slickDriver,
        jdbcDriver,
        url,
        outputFolder,
        pkg,
        user,
        password
      )
    )
    Ok("success")
  }
}
