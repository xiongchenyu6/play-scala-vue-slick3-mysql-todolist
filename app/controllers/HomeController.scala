package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import models.graphql.ProjectDefination
import models.{ProjectRepo, TaskRepo}
import org.webjars.play.WebJarsUtil
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import sangria.marshalling.playJson._
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.mvc._
import sangria.execution._
import sangria.parser.{QueryParser, SyntaxError}
import slick.jdbc.JdbcProfile
import tables.Tables._
import utils.Logger
import utils.auth.{DefaultEnv, NormalRight}

import scala.concurrent.Future
import scala.util.{Failure, Success}
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
    ] with I18nSupport with Logger {

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

  def indexPageRender(form:Form[CreateProjectForm] = projectForm)
                     (implicit request: SecuredRequest[DefaultEnv,AnyContent]) = {
    projectRepo.all().map {projects =>
      Ok(views.html.index(projectForm,projects,Some(request.identity)))
    }
      .recover{
        case e: Exception =>
          Ok(e.toString)
      }
  }

  def index() = silhouette.SecuredAction(NormalRight[DefaultEnv#A]).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    indexPageRender(projectForm)
  }

  def createProject() = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv,AnyContent] =>

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

  def graphql(query: String, variables: Option[String], operation: Option[String]) =
    Action.async(executeQuery(query, variables map parseVariables, operation))

  def graphqlBody = Action.async(parse.json) { request ⇒
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]

    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) ⇒ Some(parseVariables(vars))
      case obj: JsObject ⇒ Some(obj)
      case _ ⇒ None
    }

    executeQuery(query, variables, operation)
  }

 private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]

  private def executeQuery(query: String, variables: Option[JsObject], operation: Option[String]) =
    QueryParser.parse(query) match {

      // query parsed successfully, time to execute it!
      case Success(queryAst) ⇒
        Executor.execute(ProjectDefination.StarWarsSchema, queryAst, projectRepo,
            operationName = operation,
            variables = variables getOrElse Json.obj())
          .map(Ok(_))
          .recover {
            case error: QueryAnalysisError ⇒ BadRequest(error.resolveError)
            case error: ErrorWithResolver ⇒ InternalServerError(error.resolveError)
          }

      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) ⇒
        Future.successful(BadRequest(Json.obj(
          "syntaxError" → error.getMessage,
          "locations" → Json.arr(Json.obj(
            "line" → error.originalError.position.line,
            "column" → error.originalError.position.column)))))

      case Failure(error) ⇒
        throw error
    }
}
case class CreateProjectForm(name: String)