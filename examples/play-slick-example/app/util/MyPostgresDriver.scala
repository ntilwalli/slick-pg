package util

import com.github.tminglei.slickpg._
import play.api.libs.json.{JsValue, Json}

trait MyPostgresDriver extends ExPostgresDriver
                          with PgArraySupport
                          with PgDate2Support
                          with PgPlayJsonSupport
                          with PgNetSupport
                          with PgLTreeSupport
                          with PgRangeSupport
                          with PgHStoreSupport
                          with PgSearchSupport
                          with PgPostGISSupport {

  override val pgjson = "jsonb"
  ///
  override val api = new API with ArrayImplicits
                             with DateTimeImplicits
                             with PlayJsonImplicits
                             with NetImplicits
                             with LTreeImplicits
                             with RangeImplicits
                             with HStoreImplicits
                             with SearchImplicits
                             with SearchAssistants
                             with PostGISImplicits
                             with PostGISAssistants {
    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
    implicit val playJsonArrayTypeMapper =
      new AdvancedArrayJdbcType[JsValue](pgjson,
        (s) => utils.SimpleArrayUtils.fromString[JsValue](Json.parse(_))(s).orNull,
        (v) => utils.SimpleArrayUtils.mkString[JsValue](_.toString())(v)
      ).to(_.toList)
  }
}

object MyPostgresDriver extends MyPostgresDriver
