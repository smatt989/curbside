package com.example.app.migrations

import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import com.example.app.db.Tables

object Migration2 extends Migration {

  val id = 2

  class Questions(tag: Tag) extends Table[(String, Int, String, Long, Long)](tag, Some(InitDB.SCHEMA_NAME), "QUESTIONS") {
    def id = column[String]("QUESTION_ID", O.PrimaryKey)
    def creatorId = column[Int]("CREATOR_ID")
    def text = column[String]("QUESTION_TEXT")
    def createdMillis = column[Long]("CREATED_MILLIS")
    def updatedMillis = column[Long]("UPDATED_MILLIS")

    def * = (id, creatorId, text, createdMillis, updatedMillis)

    def creator = foreignKey("QUESTION_TO_USERS_FK", creatorId, Tables.UserAccounts)(_.userAccountId)
  }

  class Answers(tag: Tag) extends Table[(String, String, Int, String, Long, Long)](tag, Some(InitDB.SCHEMA_NAME), "ANSWERS") {
    def id = column[String]("ANSWER_ID", O.PrimaryKey)
    def questionId = column[String]("QUESTION_ID")
    def creatorId = column[Int]("CREATOR_ID")
    def text = column[String]("ANSWER_TEXT")
    def createdMillis = column[Long]("CREATED_MILLIS")
    def updatedMillis = column[Long]("UPDATED_MILLIS")

    def * = (id, questionId, creatorId, text, createdMillis, updatedMillis)

    def creator = foreignKey("ANSWER_TO_USERS_FK", creatorId, Tables.UserAccounts)(_.userAccountId)
    def question = foreignKey("ANSWER_TO_QUESTIONS_FK", questionId, questions)(_.id)
  }

  class Comments(tag: Tag) extends Table[(String, Int, Option[String], Option[String], String, Long, Long)](tag, Some(InitDB.SCHEMA_NAME), "COMMENTS") {
    def id = column[String]("COMMENT_ID", O.PrimaryKey)
    def creatorId = column[Int]("CREATOR_ID")
    def questionId = column[Option[String]]("QUESTION_ID")
    def answerId = column[Option[String]]("ANSWER_ID")
    def text = column[String]("COMMENT_TEXT")
    def createdMillis = column[Long]("CREATED_MILLIS")
    def updatedMillis = column[Long]("UPDATED_MILLIS")

    def * = (id, creatorId, questionId, answerId, text, createdMillis, updatedMillis)

    def creator = foreignKey("COMMENT_TO_USERS_FK", creatorId, Tables.UserAccounts)(_.userAccountId)
    def question = foreignKey("COMMENT_TO_QUESTIONS_FK", questionId, questions)(_.id)
    def answer = foreignKey("COMMENT_TO_ANSWER_FK", answerId, answers)(_.id)
  }

  class Reviews(tag: Tag) extends Table[(String, String, Int, Boolean, Long)](tag, Some(InitDB.SCHEMA_NAME), "REVIEWS") {
    def id = column[String]("REVIEW_ID", O.PrimaryKey)
    def answerId = column[String]("ANSWER_ID")
    def creatorId = column[Int]("CREATOR_ID")
    def isPositive = column[Boolean]("IS_POSITIVE")
    def createdMillis = column[Long]("CREATED_MILLIS")

    def * = (id, answerId, creatorId, isPositive, createdMillis)

    def answer = foreignKey("REVIEW_TO_ANSWERS_FK", answerId, answers)(_.id)
    def creator = foreignKey("REVIEW_TO_USERS_FK", creatorId, Tables.UserAccounts)(_.userAccountId)
  }


  val questions = TableQuery[Questions]
  val answers = TableQuery[Answers]
  val comments = TableQuery[Comments]
  val reviews = TableQuery[Reviews]

  def query = (questions.schema ++ answers.schema ++ comments.schema ++ reviews.schema).create
}