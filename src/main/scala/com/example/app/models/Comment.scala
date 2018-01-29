package com.example.app.models

import com.example.app.UpdatableUUIDObject
import com.example.app.db.Tables.{Comments, CommentsRow}
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

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

  def toJson(userId: Int, comment: CommentsRow, creatorName: String) =
    CommentJson(comment.commentId, comment.questionId, comment.answerId, comment.commentText, comment.createdMillis, comment.updatedMillis, userId == comment.creatorId, creatorName)

  def manyToJson(userId: Int, comments: Seq[CommentsRow]) = {
    val users = Await.result(User.byIds(comments.map(_.creatorId).distinct), Duration.Inf)
    val usersById = users.map(u => u.userAccountId -> u.username).toMap
    comments.map(q => toJson(userId, q, usersById(q.creatorId)))
  }

  def authorizedToEditComment(commentCreateObject: CommentCreateObject, userId: Int) = {
    if(commentCreateObject.id.isDefined){
      Await.result(byId(commentCreateObject.id.get), Duration.Inf).creatorId == userId
    } else
      true
  }
}

case class CommentCreateObject(id: Option[String], questionId: Option[String], answerId: Option[String], text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    CommentsRow(id.getOrElse(null), creatorId, questionId, answerId, text, now, now)
  }
}

case class CommentJson(id: String, questionId: Option[String], answerId: Option[String], text: String, createdMillis: Long, updatedMillis: Long, isCreator: Boolean, creatorName: String)