package com.example.app.Routes

import com.example.app.models.{User, UserSession}
import com.example.app.{AuthenticationSupport, RegisteredSupport, SlickRoutes}
import org.scalatra.Ok

trait SessionRoutes extends SlickRoutes with AuthenticationSupport with RegisteredSupport{

  get("/sessions") {
    contentType = formats("json")
    UserSession.getAll
  }

  get("/sessions/new"){
    contentType = formats("json")
    authenticate()

    println("registered: "+registered())

    if(!registered()){
      Ok{"333"}
    }
    User.makeJson(user)
  }

  post("/sessions/logout"){
    authenticate()
    val id = user.userAccountId
    scentry.logout()
    scentry.store.invalidate()
    val session = UserSession.fromUser(id)
    session.map(s => UserSession.delete(s.userSessionId))
    "200"
  }

}