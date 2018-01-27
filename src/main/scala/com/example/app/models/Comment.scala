package com.example.app.models

import com.example.app.UpdatableUUIDObject
import com.example.app.db.Tables.{Comments, CommentsRow}
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by matt on 1/26/18.
  */
object Comment extends UpdatableUUIDObject[CommentsRow, Comments]{
  def updateQuery(a: _root_.com.example.app.db.Tables.CommentsRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.commentText, x.updatedMillis))
    .update((a.commentId, a.updatedMillis))

  lazy val table = Comments

  def idFromRow(a: _root_.com.example.app.db.Tables.CommentsRow) =
    a.commentId

  def updateId(a: _root_.com.example.app.db.Tables.CommentsRow, id: String) =
    a.copy(commentId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Comments) =
    a.commentId

  def byAnswerIds(answerIds: Seq[String]) =
    db.run(table.filter(_.answerId inSet answerIds).result)

  def byQuestionIds(questionIds: Seq[String]) =
    db.run(table.filter(_.questionId inSet questionIds).result)

  def toJson(userId: Int, comment: CommentsRow) =
    CommentJson(comment.commentId, comment.questionId, comment.answerId, comment.commentText, comment.createdMillis, comment.updatedMillis, userId == comment.creatorId)
}

case class CommentCreateObject(id: Option[String], questionId: Option[String], answerId: Option[String], text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    CommentsRow(id.getOrElse(null), creatorId, questionId, answerId, text, now, now)
  }
}

case class CommentJson(id: String, questionId: Option[String], answerId: Option[String], text: String, createdMillis: Long, updatedMillis: Long, creator: Boolean)