package com.example.app.models

import com.example.app.UpdatableUUIDObject
import com.example.app.db.Tables.{Questions, QuestionsRow}
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by matt on 1/26/18.
  */
object Question extends UpdatableUUIDObject[QuestionsRow, Questions] {
  def updateQuery(a: _root_.com.example.app.db.Tables.QuestionsRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.questionTitle, x.questionText, x.updatedMillis))
    .update((a.questionTitle, a.questionText, a.updatedMillis))

  lazy val table = Questions

  def idFromRow(a: _root_.com.example.app.db.Tables.QuestionsRow) =
    a.questionId

  def updateId(a: _root_.com.example.app.db.Tables.QuestionsRow, id: String) =
    a.copy(questionId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Questions) =
    a.questionId

  def toJson(userId: Int, question: QuestionsRow, creatorName: String) =
    QuestionJson(question.questionId, question.questionTitle, question.questionText, question.createdMillis, question.updatedMillis, userId == question.creatorId, creatorName)

  def manyToJson(userId: Int, questions: Seq[QuestionsRow]) = {
    val users = Await.result(User.byIds(questions.map(_.creatorId).distinct), Duration.Inf)
    val usersById = users.map(u => u.userAccountId -> u.username).toMap
    questions.map(q => toJson(userId, q, usersById(q.creatorId)))
  }


  def fullQuestionsFromQuestionRows(questions: Seq[QuestionsRow], userId: Int) = {
    val answers = Await.result(Answer.byQuestionIds(questions.map(_.questionId)), Duration.Inf)
    val reviews = Await.result(Review.byAnswerIds(answers.map(_.answerId)), Duration.Inf)
    val questionComments = Await.result(Comment.byQuestionIds(questions.map(_.questionId)), Duration.Inf)
    val answerComments = Await.result(Comment.byAnswerIds(answers.map(_.answerId)), Duration.Inf)

    val userIds = answers.map(_.creatorId) ++ questions.map(_.creatorId) ++ questionComments.map(_.creatorId) ++ answerComments.map(_.creatorId)

    val users = Await.result(User.byIds(userIds.distinct), Duration.Inf)

    val questionViews = QuestionView.distinctViewsByQuestionIds(questions.map(_.questionId))

    val answersByQuestionId = answers.groupBy(_.questionId)
    val reviewsByAnswerId = reviews.groupBy(_.answerId)
    val questionCommentsByQuestionId = questionComments.groupBy(_.questionId)
    val answerCommentsByAnswerId = answerComments.groupBy(_.answerId)
    val usernameById = users.map(u => u.userAccountId -> u.username).toMap

    questions.map(q => {
      val as = answersByQuestionId.get(q.questionId).getOrElse(Nil)
      val qcs = questionCommentsByQuestionId.get(Some(q.questionId)).getOrElse(Nil)
      val fullAnswers = as.map(a => {
        val rs = reviewsByAnswerId.get(a.answerId).getOrElse(Nil)
        val acs = answerCommentsByAnswerId.get(Some(a.answerId)).getOrElse(Nil).sortBy(_.createdMillis)

        val score = rs.count(_.isPositive) - rs.count(a => !a.isPositive)

        AnswerFull(Answer.toJson(userId, a, usernameById(a.creatorId)), acs.map(c => Comment.toJson(userId, c, usernameById(c.creatorId))), rs.map(Review.toJson), score)
      }).sortBy(_.score).reverse

      QuestionFull(Question.toJson(userId, q, usernameById(q.creatorId)), fullAnswers, qcs.map(c => Comment.toJson(userId, c, usernameById(c.creatorId))), questionViews(q.questionId))
    })
  }

  val onePage = 20

  def getFullQuestions(userId: Int, page: Int = 0) = {

    val questions = Await.result(db.run(table.sortBy(_.updatedMillis.desc).drop(page * onePage).take(onePage).result), Duration.Inf)

    fullQuestionsFromQuestionRows(questions, userId)
  }

  def fullQuestionsByIds(questionIds: Seq[String], userId: Int) = {

    val questions = Await.result(byIds(questionIds), Duration.Inf)

    fullQuestionsFromQuestionRows(questions, userId)
  }

  def oneFullQuestionById(questionId: String, userId: Int) = {

    fullQuestionsByIds(Seq(questionId), userId).head
  }

  def myQuestions(userId: Int, page: Int = 0) = {

    val questions = Await.result(db.run(table.filter(_.creatorId === userId).sortBy(_.updatedMillis.desc).drop(page * onePage).take(onePage).result), Duration.Inf)

    fullQuestionsFromQuestionRows(questions, userId)
  }

  def authorizedToEditQuestion(questionCreateObject: QuestionCreateObject, userId: Int) = {
    if(questionCreateObject.id.isDefined){
      Await.result(byId(questionCreateObject.id.get), Duration.Inf).creatorId == userId
    } else
      true
  }

}

case class QuestionCreateObject(id: Option[String], title: String, text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    QuestionsRow(id.getOrElse(null), creatorId, title, text, now, now)
  }
}

case class QuestionJson(id: String, title: String, text: String, createdMillis: Long, updatedMillis: Long, isCreator: Boolean, creatorName: String)

case class QuestionFull(question: QuestionJson, answers: Seq[AnswerFull], comments: Seq[CommentJson], viewCount: Int)

case class AnswerFull(answer: AnswerJson, comments: Seq[CommentJson], reviews: Seq[ReviewJson], score: Int)