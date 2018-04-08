package models

import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import tables.Tables._
import scala.collection.JavaConverters._
import com.hankcs.hanlp.tokenizer._
import com.hankcs.hanlp.corpus.tag._
import com.hankcs.hanlp.dictionary._

import scala.concurrent.Future

class BoxDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.profile.api._

  def all(): Future[Seq[BoxRow]] = db.run(Box.result);

  def byId(id: Int): Future[BoxRow] = db.run(Box.filter(_.boxId === id).result.head)

  def byDay() :Future[Seq[BD]] = all().map { data =>
    data.groupBy { r =>
      val cal = Calendar.getInstance();
      cal.setTime(r.createat)
      val sdf = new SimpleDateFormat("yyyyMM");
      sdf.format(cal.getTime)
    }.map { data =>
      BD(data._1, data._2.size)
    }.toSeq.sortBy(_.date)

  }
  def bestBuy():Future[String] = db.run(Box.map(t => (t.content,t.userId)).result).map(data => {
    CustomDictionary.add("手机膜", "n 1")
    CustomDictionary.add("手机壳", "n 1")
    CustomDictionary.remove("粉")
    val groupedTable = data.groupBy(_._2)
    val tableList = groupedTable.map(_._2.map(c => NLPTokenizer.segment(c._1).asScala))
    val flatternTableList = tableList.map(_.flatten.filter(f => f.nature == Nature.n).map(_.word))
    val a = flatternTableList.map(_.distinct.toArray).filter(_.length > 0).toArray
    a.flatten.groupBy(identity).mapValues(_.size).maxBy(_._2)._1
  }
   )
}
object BoxDao{

  import play.api.libs.json._
  implicit val bdReads = Json.reads[BD]
  implicit val bdWrites = Json.writes[BD]
}

case class BD(date:String, count:Int)
