package com.example.app.models

import com.example.app.{AppGlobals, MailJetSender, UpdatableUUIDObject}
import com.example.app.db.Tables.{Comments, CommentsRow}
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
    CommentJson(comment.commentId, comment.questionId, comment.answerId, comment.commentText, comment.createdMillis, comment.updatedMillis, userId == comment.creatorId, creatorName, comment.isActive)

  def manyToJson(userId: Int, comments: Seq[CommentsRow]) = {
    val users = Await.result(User.byIds(comments.map(_.creatorId).distinct), Duration.Inf)
    val usersById = users.map(u => u.userAccountId -> u.username).toMap
    comments.map(q => toJson(userId, q, usersById(q.creatorId)))
  }

  def authorizedToEditComment(commentCreateObject: CommentCreateObject, userId: Int): Boolean = {
    if(commentCreateObject.id.isDefined)
      authorizedToEditComment(commentCreateObject.id.get, userId)
    else
      true
  }

  def authorizedToEditComment(id: String, userId: Int): Boolean =
    Await.result(byId(id), Duration.Inf).creatorId == userId

  def toggleActiveStatus(id: String, status: Boolean) =
    db.run(table.filter(_.commentId === id).map(_.isActive).update(status))

  def sendEmailToSubscribers(newComment: CommentsRow) = {
    //comment on answer
    if(newComment.answerId.isDefined){
      val answer = Await.result(Answer.byId(newComment.answerId.get), Duration.Inf)
      val question = Await.result(Question.byId(answer.questionId), Duration.Inf)
      val otherComments = Await.result(byAnswerIds(Seq(answer.answerId)), Duration.Inf)
      val usersToNotify = otherComments.filter(a => a.creatorId != newComment.creatorId && a.creatorId != question.creatorId).map(_.creatorId).distinct
      val userEmails = Await.result(User.byIds(usersToNotify :+ answer.creatorId), Duration.Inf).map(u => u.userAccountId -> u.email).toMap

      MailJetSender.newAnswerCommentOtherCommenters(question.questionTitle, question.questionId, usersToNotify.map(userEmails))

      if(newComment.creatorId != answer.creatorId){
        MailJetSender.newAnswerCommentAnswerAuthor(question.questionTitle, question.questionId, userEmails(answer.creatorId))
      }

      //comment on question
    } else {

      val question = Await.result(Question.byId(newComment.questionId.get), Duration.Inf)
      val otherComments = Await.result(byQuestionIds(Seq(question.questionId)), Duration.Inf)
      val usersToNotify = otherComments.filter(a => a.creatorId != newComment.creatorId && a.creatorId != question.creatorId).map(_.creatorId).distinct
      val userEmails = Await.result(User.byIds(usersToNotify :+ question.creatorId), Duration.Inf).map(u => u.userAccountId -> u.email).toMap

      MailJetSender.newQuestionCommentOtherCommenters(question.questionTitle, question.questionId, usersToNotify.map(userEmails))

      if(newComment.creatorId != question.creatorId) {
        MailJetSender.newQuestionCommentQuestionAuthor(question.questionTitle, question.questionId, userEmails(question.creatorId))
      }
    }
  }
}

case class CommentCreateObject(id: Option[String], questionId: Option[String], answerId: Option[String], text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    CommentsRow(id.getOrElse(null), creatorId, questionId, answerId, text, true, now, now)
  }
}

case class CommentDeleteObject(id: String)

case class CommentJson(id: String, questionId: Option[String], answerId: Option[String], text: String, createdMillis: Long, updatedMillis: Long, isCreator: Boolean, creatorName: String, isActive: Boolean)