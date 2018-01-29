package com.example.app.models

import com.example.app.UpdatableUUIDObject
import com.example.app.db.Tables.{Answers, AnswersRow}
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by matt on 1/26/18.
  */
object Answer extends UpdatableUUIDObject[AnswersRow, Answers]{
  def updateQuery(a: _root_.com.example.app.db.Tables.AnswersRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.answerText, x.updatedMillis))
    .update((a.answerText, a.updatedMillis))

  lazy val table = Answers

  def idFromRow(a: _root_.com.example.app.db.Tables.AnswersRow) =
    a.answerId

  def updateId(a: _root_.com.example.app.db.Tables.AnswersRow, id: String) =
    a.copy(answerId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Answers) =
    a.answerId

  def byQuestionIds(questionIds: Seq[String]) =
    db.run(table.filter(_.questionId inSet questionIds).result)

  def toJson(userId: Int, answer: AnswersRow) =
    AnswerJson(answer.answerId, answer.questionId, answer.answerText, answer.createdMillis, answer.updatedMillis, userId == answer.creatorId)
}

case class AnswerCreateObject(id: Option[String], questionId: String, text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    AnswersRow(id.getOrElse(null), questionId, creatorId, text, now, now)
  }
}

case class AnswerJson(id: String, questionId: String, text: String, createdMillis: Long, updatedMillis: Long, creator: Boolean)