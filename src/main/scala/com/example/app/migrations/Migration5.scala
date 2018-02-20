package com.example.app.migrations

import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import com.example.app.db.Tables

object Migration5 extends Migration {

  val id = 5

  class QuestionTags(tag: Tag) extends Table[(String, String, Int)](tag, Some(InitDB.SCHEMA_NAME), "QUESTION_TAGS") {
    def id = column[String]("QUESTION_TAG_ID")
    def questionId = column[String]("QUESTION_ID")
    def tagId = column[Int]("TAG_ID")

    def * = (id, questionId, tagId)

    def question = foreignKey("QUESTION_TAG_TO_QUESTIONS_FK", questionId, Tables.Questions)(_.questionId)
  }

  val questionTags = TableQuery[QuestionTags]

  def query = (questionTags.schema).create
}