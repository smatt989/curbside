package com.example.app.models

import com.example.app.SlickUUIDObject
import com.example.app.db.Tables.{Registrations, RegistrationsRow}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by matt on 1/26/18.
  */
object Registration extends SlickUUIDObject[RegistrationsRow, Registrations] {
  lazy val table = Registrations

  def idFromRow(a: _root_.com.example.app.db.Tables.RegistrationsRow) =
    a.registrationId

  def updateId(a: _root_.com.example.app.db.Tables.RegistrationsRow, id: String) =
    a.copy(registrationId = id)

  def idColumnFromTable(a: Registrations) =
    a.registrationId

  def registerUser(registrationCode: String) = {
    val registration = Await.result(byId(registrationCode), Duration.Inf)
    val user = Await.result(User.byId(registration.userId), Duration.Inf)
    val toSave = user.copy(registered = true)
    User.updateOne(toSave)
  }
}

case class RegistrationRequest(code: String)