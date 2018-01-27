package com.example.app.migrations

import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import com.example.app.db.Tables

object Migration3 extends Migration {

  val id = 3

  class Registrations(tag: Tag) extends Table[(String, Int, Option[String], Long)](tag, Some(InitDB.SCHEMA_NAME), "REGISTRATIONS") {
    def id = column[String]("REGISTRATION_ID", O.PrimaryKey)
    def userId = column[Int]("USER_ID")
    def imageUrl = column[Option[String]]("IMAGE_URL")
    def createdMillis = column[Long]("CREATED_MILLIS")

    def * = (id, userId, imageUrl, createdMillis)

    def user = foreignKey("REGISTRATION_TO_USERS_FK", userId, Tables.UserAccounts)(_.userAccountId)
  }

  val registrations = TableQuery[Registrations]

  def query = (registrations.schema).create
}