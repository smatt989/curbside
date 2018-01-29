package com.example.app.migrations

import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import com.example.app.db.Tables

object Migration4 extends Migration {

  val id = 4

  class QuestionViews(tag: Tag) extends Table[(String, String, Int, Long)](tag, Some(InitDB.SCHEMA_NAME), "QUESTION_VIEWS") {
    def id = column[String]("QUESTION_VIEW_ID", O.PrimaryKey)
    def questionId = column[String]("QUESTION_ID")
    def userId = column[Int]("USER_ID")
    def createdMillis = column[Long]("CREATED_MILLIS")

    def * = (id, questionId, userId, createdMillis)

    def question = foreignKey("QUESTION_VIEW_TO_QUESTIONS_FK", questionId, Tables.Questions)(_.questionId)
  }

  val questionViews = TableQuery[QuestionViews]

  def query = (questionViews.schema).create
}