package controllers.admin

import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.Silhouette
import controllers.AssetsFinder
import models.{BD, BoxDao, ProjectRepo, TaskRepo}
import org.webjars.play.WebJarsUtil
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import utils.Logger
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

@Singleton
class AdminController @Inject()(implicit ec: ExecutionContext,
                               webJarsUtil: WebJarsUtil,
                               assets: AssetsFinder,
                               silhouette: Silhouette[DefaultEnv],
                               protected val dbConfigProvider: DatabaseConfigProvider,
                               boxDao: BoxDao,
                               cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile
    ] with I18nSupport with Logger {

  import dbConfig.profile.api._

  def home  = Action.async { implicit request : Request[AnyContent]=>
    boxDao.bestBuy.map { d =>
      Ok(views.html.admin.home(d))
    }
  }

  def getCountByDay = Action.async {implicit request : Request[AnyContent]=>
    boxDao.byDay.map { c =>
      import  models.BoxDao._
      val bdJson: JsValue = Json.toJson(c)
      Ok(bdJson)
    }
  }

  def getBestBuy = Action.async {implicit request : Request[AnyContent] =>
    boxDao.bestBuy.map { d =>
      Ok(Json.toJson(d))
    }
  }
}
