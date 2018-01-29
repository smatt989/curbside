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

  def toJson(userId: Int, question: QuestionsRow) =
    QuestionJson(question.questionId, question.questionTitle, question.questionText, question.createdMillis, question.updatedMillis, userId == question.creatorId)


  def fullQuestionsFromQuestionRows(questions: Seq[QuestionsRow], userId: Int) = {
    val answers = Await.result(Answer.byQuestionIds(questions.map(_.questionId)), Duration.Inf)
    val reviews = Await.result(Review.byAnswerIds(answers.map(_.answerId)), Duration.Inf)
    val questionComments = Await.result(Comment.byQuestionIds(questions.map(_.questionId)), Duration.Inf)
    val answerComments = Await.result(Comment.byAnswerIds(answers.map(_.answerId)), Duration.Inf)

    val questionViews = QuestionView.distinctViewsByQuestionIds(questions.map(_.questionId))

    val answersByQuestionId = answers.groupBy(_.questionId)
    val reviewsByAnswerId = reviews.groupBy(_.answerId)
    val questionCommentsByQuestionId = questionComments.groupBy(_.questionId)
    val answerCommentsByAnswerId = answerComments.groupBy(_.answerId)

    questions.map(q => {
      val as = answersByQuestionId.get(q.questionId).getOrElse(Nil)
      val qcs = questionCommentsByQuestionId.get(Some(q.questionId)).getOrElse(Nil)
      val fullAnswers = as.map(a => {
        val rs = reviewsByAnswerId.get(a.answerId).getOrElse(Nil)
        val acs = answerCommentsByAnswerId.get(Some(a.answerId)).getOrElse(Nil).sortBy(_.createdMillis)

        val score = rs.count(_.isPositive) - rs.count(a => !a.isPositive)

        AnswerFull(Answer.toJson(userId, a), acs.map(c => Comment.toJson(userId, c)), rs.map(Review.toJson), score)
      }).sortBy(_.score).reverse

      QuestionFull(Question.toJson(userId, q), fullAnswers, qcs.map(c => Comment.toJson(userId, c)), questionViews(q.questionId))
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

}

case class QuestionCreateObject(id: Option[String], title: String, text: String) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis
    QuestionsRow(id.getOrElse(null), creatorId, title, text, now, now)
  }
}

case class QuestionJson(id: String, title: String, text: String, createdMillis: Long, updatedMillis: Long, creator: Boolean)

case class QuestionFull(question: QuestionJson, answers: Seq[AnswerFull], comments: Seq[CommentJson], viewCount: Int)

case class AnswerFull(answer: AnswerJson, comments: Seq[CommentJson], reviews: Seq[ReviewJson], score: Int)