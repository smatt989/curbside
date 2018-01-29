package com.example.app.models

import com.example.app.SlickUUIDObject
import com.example.app.db.Tables.{QuestionViewsRow, QuestionViews}
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
  * Created by matt on 1/28/18.
  */
object QuestionView extends SlickUUIDObject[QuestionViewsRow, QuestionViews]{
  lazy val table = QuestionViews

  def idFromRow(a: _root_.com.example.app.db.Tables.QuestionViewsRow) =
    a.questionViewId

  def updateId(a: _root_.com.example.app.db.Tables.QuestionViewsRow, id: String) =
    a.copy(questionViewId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.QuestionViews) =
    a.questionViewId

  def viewsByQuestionIds(ids: Seq[String]) = {
    val rows = Await.result(db.run(table.filter(_.questionId inSet ids).result), Duration.Inf)
    rows.groupBy(_.questionId)
  }

  def distinctViewsByQuestionIds(ids: Seq[String]) = {
    val mapped = viewsByQuestionIds(ids).mapValues(_.map(_.userId).distinct.size)
    ids.map(id => id -> mapped.get(id).getOrElse(0)).toMap
  }

}

case class QuestionViewCreateObject(questionId: String) {
  def toRow(userId: Int) = {
    QuestionViewsRow(null, questionId, userId, DateTime.now().getMillis)
  }
}