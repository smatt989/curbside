package com.example.app.Routes

import javax.naming.AuthenticationException

import com.example.app.models._
import com.example.app._


trait QuestionRoutes extends SlickRoutes with AuthenticationSupport with RegisteredSupport{

  post("/questions/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val questionRequest = parsedBody.extract[QuestionCreateObject]

    if(registered()){
      Question.save(questionRequest.toRow(userId)).map(q => Question.toJson(userId, q))
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  get("/questions/:questionId") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val questionId = {params("questionId")}

    if(registered()){
      Question.oneFullQuestionById(questionId, userId)
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  get("/questions/feed/:page") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val page = {params("page")}.toInt

    if(registered()){
      Question.getFullQuestions(userId, page)
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  get("/questions/created/:page") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val page = {params("page")}.toInt

    if(registered()){
      Question.myQuestions(userId, page)
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/answers/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val answerRequest = parsedBody.extract[AnswerCreateObject]

    if(registered()){
       Answer.save(answerRequest.toRow(userId)).map(a => Answer.toJson(userId, a))
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/comments/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val commentRequest = parsedBody.extract[CommentCreateObject]

    if(registered()){
      Comment.save(commentRequest.toRow(userId)).map(c => Comment.toJson(userId, c))
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/reviews/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val reviewRequest = parsedBody.extract[ReviewCreateObject]

    if(registered()){
      Review.save(reviewRequest.toRow(userId)).map(Review.toJson)
    } else {
      throw new AuthenticationException("Not registered")
    }
  }


}