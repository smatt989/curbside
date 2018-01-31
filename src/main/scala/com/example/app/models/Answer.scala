package com.example.app.models

import com.example.app.{AppGlobals, MailJetSender, UpdatableUUIDObject}
import com.example.app.db.Tables.{Answers, AnswersRow}
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

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

  def toJson(userId: Int, answer: AnswersRow, creatorName: String) =
    AnswerJson(answer.answerId, answer.questionId, answer.answerText, answer.createdMillis, answer.updatedMillis, userId == answer.creatorId, creatorName, answer.isActive)

  def manyToJson(userId: Int, answers: Seq[AnswersRow]) = {
    val users = Await.result(User.byIds(answers.map(_.creatorId).distinct), Duration.Inf)
    val usersById = users.map(u => u.userAccountId -> u.username).toMap
    answers.map(q => toJson(userId, q, usersById(q.creatorId)))
  }

  def authorizedToEditAnswer(answerCreateObject: AnswerCreateObject, userId: Int): Boolean = {
    if(answerCreateObject.id.isDefined)
      authorizedToEditAnswer(answerCreateObject.id.get, userId)
    else
      true
  }

  def authorizedToEditAnswer(id: String, userId: Int): Boolean =
    Await.result(byId(id), Duration.Inf).creatorId == userId

  def toggleActiveStatus(id: String, status: Boolean) =
    db.run(table.filter(_.answerId === id).map(_.isActive).update(status))

  def sendEmailToSubscribers(newAnswer: AnswersRow) = {

    val question = Await.result(Question.byId(newAnswer.questionId), Duration.Inf)
    val userEmail = Await.result(User.byId(question.creatorId), Duration.Inf)

    if(question.creatorId != newAnswer.creatorId){
      MailJetSender.newAnswerAdded(question.questionTitle, question.questionId, userEmail.email)
    }
  }
}

case class AnswerCreateObject(id: Option[String], questionId: String, text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    AnswersRow(id.getOrElse(null), questionId, creatorId, text, true, now, now)
  }
}

case class AnswerDeleteObject(id: String)

case class AnswerJson(id: String, questionId: String, text: String, createdMillis: Long, updatedMillis: Long, isCreator: Boolean, creatorName: String, isActive: Boolean)