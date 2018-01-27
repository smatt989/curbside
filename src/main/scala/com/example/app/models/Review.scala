package com.example.app.models

import com.example.app.UpdatableUUIDObject
import com.example.app.db.Tables.{Reviews, ReviewsRow}
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by matt on 1/26/18.
  */
object Review extends UpdatableUUIDObject[ReviewsRow, Reviews]{
  def updateQuery(a: _root_.com.example.app.db.Tables.ReviewsRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.isPositive, x.createdMillis))
    .update((a.isPositive, a.createdMillis))

  lazy val table = Reviews

  def idFromRow(a: _root_.com.example.app.db.Tables.ReviewsRow) =
    a.reviewId

  def updateId(a: _root_.com.example.app.db.Tables.ReviewsRow, id: String) =
    a.copy(reviewId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.Reviews) =
    a.reviewId

  def byAnswerIdAndCreatorId(answerId: String, userId: Int) =
    db.run(table.filter(a => a.answerId === answerId && a.creatorId === userId).result).map(_.headOption)

  def byAnswerIds(answerIds: Seq[String]) =
    db.run(table.filter(_.answerId inSet answerIds).result)

  def toJson(review: ReviewsRow) =
    ReviewJson(review.answerId, review.isPositive, review.createdMillis)
}

case class ReviewCreateObject(answerId: String, isPositive: Boolean) {
  def toRow(creatorId: Int) = {
    val now = DateTime.now().getMillis

    val preexisting = Await.result(Review.byAnswerIdAndCreatorId(answerId, creatorId), Duration.Inf)

    preexisting.map(_.copy(isPositive = isPositive, createdMillis = now)).getOrElse(ReviewsRow(null, answerId, creatorId, isPositive, now))
  }
}

case class ReviewJson(answerId: String, isPositive: Boolean, createdMillis: Long)