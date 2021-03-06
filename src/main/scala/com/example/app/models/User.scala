package com.example.app.models

import com.example.app.UpdatableDBObject
import org.mindrot.jbcrypt.BCrypt
import com.example.app.AppGlobals
import AppGlobals.dbConfig.driver.api._
import com.example.app.db.Tables._
import com.example.app.db.{Tables => T}
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.Await

case class UserCreate(username: String, email: String, password: String) {
  lazy val makeUser =
    UserAccountsRow(0, username, email, User.makeHash(password), registered = false, DateTime.now().getMillis)
}

case class UpdateUser(email: String, password: String, newEmail: Option[String], newPassword: Option[String]){
  lazy val userLogin =
    UserLogin(email, password)
}

case class UserLogin(email: String, password: String)

case class UserJson(id: Int, username: String)

case class UserRegisteredJson(username: String, registered: Boolean)


object User extends UpdatableDBObject[UserAccountsRow, UserAccounts]{

  def makeJson(a: UserAccountsRow) =
    UserJson(a.userAccountId, a.username)

  def registeredJson(a: UserAccountsRow) =
    UserRegisteredJson(a.username, a.registered)

  lazy val table = T.UserAccounts

  def idFromRow(a: _root_.com.example.app.db.Tables.UserAccountsRow) =
    a.userAccountId

  def updateId(a: _root_.com.example.app.db.Tables.UserAccountsRow, id: Int) =
    a.copy(userAccountId = id)

  def idColumnFromTable(a: _root_.com.example.app.db.Tables.UserAccounts) =
    a.userAccountId

  def updateQuery(a: UserAccountsRow) = table.filter(t => idColumnFromTable(t) === idFromRow(a))
    .map(x => (x.email, x.hashedPassword, x.registered))
    .update((a.email, a.hashedPassword, a.registered))

  def makeHash(password: String) =
    BCrypt.hashpw(password, BCrypt.gensalt())

  private[this] def checkPassword(password: String, hashedPassword: String) =
    BCrypt.checkpw(password, hashedPassword)

  def authenticate(user: UserAccountsRow, password: String) = {
    checkPassword(password, user.hashedPassword)
  }

  def searchUserName(query: String) = {
    val queryString = "%"+query.toLowerCase()+"%"
    db.run(table.filter(_.email.toLowerCase like queryString).result)//.map(_.map(reifyJson))
  }

  private[this] def unauthenticatedUserFromUserLogin(userLogin: UserLogin) = {

    Await.result(
      db.run(table.filter(_.email.toLowerCase === userLogin.email.toLowerCase()).result).map(_.headOption.getOrElse{
        throw new Exception("No user with that email")
      }), Duration.Inf)
  }

  def authenticatedUser(userLogin: UserLogin) = {
    val user = unauthenticatedUserFromUserLogin(userLogin)

    if(authenticate(user, userLogin.password))
      Some(user)
    else
      None
  }

  def uniqueEmail(email: String) =
    Await.result(db.run(table.filter(_.email.toLowerCase === email.toLowerCase).result).map(_.isEmpty), Duration.Inf)

  val emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

  def validEmailMakeup(email: String) =
    email.matches(emailRegex)

  def uniqueUserName(username: String) =
    Await.result(db.run(table.filter(_.username.toLowerCase === username.toLowerCase).result).map(_.isEmpty), Duration.Inf)

  def validUsernameMakeup(username: String) =
   username.matches("""^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9_]+(?<![_.])$""")

  def validUserName(username: String) = {
    val validMakeup = validUsernameMakeup(username)
    val unique = uniqueUserName(username)
    validMakeup && unique
  }

  def validEmail(email: String) = {
    val validMakeup = validEmailMakeup(email)
    val unique = uniqueEmail(email)
    validMakeup && unique
  }

  def validPassword(password: String) = {
    password.length > 5 && password.length < 41
  }

  def createNewUser(userCreate: UserCreate) = {

    if(validUserName(userCreate.username) && validEmail(userCreate.email) && validPassword(userCreate.password))
      create(userCreate.makeUser)
    else
      throw new Exception("Must provide unique email")
  }

  def updateUser(updateUser: UpdateUser) = {
    val user = authenticatedUser(updateUser.userLogin)
    if(user.isDefined){
      val newUser = (updateUser.newEmail, updateUser.newPassword) match {
        case (Some(ne), Some(np)) => user.get.copy(email = ne, hashedPassword = makeHash(np))
        case (Some(ne), None) => user.get.copy(email = ne)
        case (None, Some(np)) => user.get.copy(hashedPassword = makeHash(np))
        case _ => user.get
      }
      save(newUser)
    } else {
      throw new Exception("Unable to authenticate")
    }
  }
}

case class ValidInput(valid: Boolean, value: String, message: Option[String] = None)