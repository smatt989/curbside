package com.example.app

import com.example.app.db.Tables.{QuestionTagsRow, QuestionTags}
import AppGlobals.dbConfig.driver.api._
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
  * Created by matt on 2/19/18.
  */


case class QuestionTag(id: Int, name: String)

object QuestionTag extends SlickUUIDObject[QuestionTagsRow, QuestionTags]{
  lazy val table = QuestionTags

  def idFromRow(a: _root_.com.example.app.db.Tables.QuestionTagsRow) =
    a.questionTagId

  def updateId(a: _root_.com.example.app.db.Tables.QuestionTagsRow, id: String) =
    a.copy(questionTagId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.QuestionTags) =
    a.questionTagId

  def deleteOldQuestionTags(questionId: String) =
    db.run(table.filter(_.questionId === questionId).delete)

  def saveQuestionTags(questionId: String, tags: Seq[Int]) = {
    Await.result(deleteOldQuestionTags(questionId), Duration.Inf)
    createMany(tags.map(t => QuestionTagsRow(null, questionId, t)))
  }

  def byQuestionIds(questionIds: Seq[String]) =
    Await.result(db.run(table.filter(_.questionId inSet questionIds).result), Duration.Inf)
      .groupBy(_.questionId)

  def toJson(tag: QuestionTagsRow) =
    byTagId(tag.tagId)

  val tags = Seq(
    QuestionTag(1, "Hospitalist"),
    QuestionTag(2, "Primary Care"),
    QuestionTag(3, "Cardiology"),
    QuestionTag(4, "Gastroenterology"),
    QuestionTag(5, "Rheumatology"),
    QuestionTag(6, "Neurology"),
    QuestionTag(7, "Heme-Onc"),
    QuestionTag(8, "Pulm/Critical Care"),
    QuestionTag(9, "Dermatology"),
    QuestionTag(10, "Plastics"),
    QuestionTag(11, "Orthopedics"),
    QuestionTag(12, "Neurosurgery"),
    QuestionTag(13, "ENT"),
    QuestionTag(14, "Urology"),
    QuestionTag(15, "Pediatrics"),
    QuestionTag(16, "Ob/Gyn"),
    QuestionTag(17, "Anesthesiology"),
    QuestionTag(18, "Emergency Medicine"),
    QuestionTag(19, "Family Medicine"),
    QuestionTag(20, "Hospice & Palliative"),
    QuestionTag(21, "Ophthalmology"),
    QuestionTag(22, "Pathology"),
    QuestionTag(23, "Pain Medicine"),
    QuestionTag(24, "Radiology"),
    QuestionTag(25, "Radiation Oncology"),
    QuestionTag(26, "Physical Medicine & Rehab")
  )

  val byTagName = tags.map(t => t.name -> t).toMap
  val byTagId = tags.map(t => t.id -> t).toMap
}

