package com.example.app.Routes

import com.example.app.db.Tables.{DeviceTokensRow, RegistrationsRow}
import com.example.app.models._
import com.example.app.{AuthenticationSupport, MailJetSender, SessionTokenStrategy, SlickRoutes}
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait UserRoutes extends SlickRoutes with AuthenticationSupport{

  post("/users/create") {
    contentType = formats("json")

    val email = request.header(SessionTokenStrategy.Email)
    val password = request.header(SessionTokenStrategy.Password)
    val username = request.header(SessionTokenStrategy.Username)

    val user = UserCreate(username.get, email.get, password.get)

    val created = Await.result(User.createNewUser(user), Duration.Inf)

    MailJetSender.sendRegistrationConfirmEmail(created)

    User.makeJson(created)
  }

  get("/register/:code") {
    contentType = formats("json")

    val code = {params("code")}

    val registered = Await.result(Registration.registerUser(code), Duration.Inf)

    MailJetSender.sendRegistrationAcceptedEmail(registered)

    User.makeJson(registered)
  }

  post("/users/search") {
    contentType = formats("json")
    val query = {params("query")}

    User.searchUserName(query).map(_.map(User.makeJson))
  }

  post("/users/connections/create") {
    contentType = formats("json")
    authenticate()

    val connectionRequest = parsedBody.extract[ConnectionRequestJson]
    val connection = connectionRequest.newConnection(user.userAccountId)

    //interface changed here... id is now userConnectionId
    UserConnection.safeSave(connection)
  }

  post("/users/connections/delete") {
    contentType = formats("json")
    authenticate()

    val rejectionRequest = parsedBody.extract[ConnectionDeleteJson]

    UserConnection.removeBySenderReceiverPair(user.userAccountId, rejectionRequest.removeUserId).map(_ => "200")
  }

  get("/users/connections/added") {
    contentType = formats("json")
    authenticate()

    UserConnection.getReceiversBySenderId(user.userAccountId).map(_.map(User.makeJson))
  }

  get("/users/connections/awaiting") {
    contentType = formats("json")
    authenticate()

    val sent = UserConnection.getReceiversBySenderId(user.userAccountId).map(_.map(User.makeJson))
    val received = UserConnection.getSendersByReceiverId(user.userAccountId).map(_.map(User.makeJson))

    for {
      s <- sent
      r <- received
    } yield (r diff s)
  }

  get("/users") {
    User.getAll.map(_.map(User.makeJson))
  }

  post("/users/tokens"){
    contentType = formats("json")
    authenticate()

    val rawToken = {params("device_token")}

    val deviceToken = DeviceTokensRow(deviceTokenId = 0, userId = user.userAccountId, deviceToken = Some(rawToken))

    //interface changed here... id is now deviceTokenId
    DeviceToken.save(deviceToken)
  }

  get("/users/tokens") {
    contentType = formats("json")

    DeviceToken.getAll
  }

}