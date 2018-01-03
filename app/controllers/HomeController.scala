package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.Silhouette
import models.{ProjectRepo, TaskRepo}
import org.webjars.play.WebJarsUtil
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.json.JsValue
import play.api.mvc._
import slick.jdbc.JdbcProfile
import tables.Tables._
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext
/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(implicit ec: ExecutionContext,
                               webJarsUtil: WebJarsUtil,
                               assets: AssetsFinder,
                               silhouette: Silhouette[DefaultEnv],
                               protected val dbConfigProvider: DatabaseConfigProvider,
                               projectRepo: ProjectRepo,
                               taskRepo: TaskRepo,
                               cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile
    ] with I18nSupport {

  import dbConfig.profile.api._
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  val projectForm: Form[CreateProjectForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateProjectForm.apply)(CreateProjectForm.unapply)
  }

  def indexPageRender(form:Form[CreateProjectForm] = projectForm)(implicit request: MessagesRequestHeader) = {
    projectRepo.all().map {projects =>
      Ok(views.html.index(projectForm,projects))
    }
      .recover{
        case e: Exception =>
          Ok(e.toString)
      }
  }

  def index() = silhouette.SecuredAction.async { implicit request: Request[AnyContent] =>
    projectRepo.all().map { projects =>
      Ok(views.html.index(projectForm, projects))
    }
      .recover {
        case e: Exception =>
          Ok(e.toString)
      }
  }

  def createProject() = Action.async { implicit request: MessagesRequest[AnyContent] =>

    projectForm.bindFromRequest.fold(
      errorForm =>{
        indexPageRender(errorForm)
      },
      project => {
        val projectSql = Project += ProjectRow(0, project.name, "ready")
        db.run(projectSql).flatMap { data =>
          indexPageRender(projectForm)
        }
            .recover {
              case e: Exception =>
                Ok(e.toString)
            }
      })
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

case class CreateProjectForm(name: String)
