package com.example.app.Routes

import javax.naming.AuthenticationException

import com.example.app.models._
import com.example.app._


trait QuestionRoutes extends SlickRoutes with AuthenticationSupport with RegisteredSupport{

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

  post("/questions/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val questionRequest = parsedBody.extract[QuestionCreateObject]

    if(registered() && Question.authorizedToEditQuestion(questionRequest, userId)){
      Question.save(questionRequest.toRow(userId)).map(q => Question.manyToJson(userId, Seq(q)).head)
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/answers/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val answerRequest = parsedBody.extract[AnswerCreateObject]

    if(registered() && Answer.authorizedToEditAnswer(answerRequest, userId)){
       Answer.save(answerRequest.toRow(userId)).map(a => Answer.manyToJson(userId, Seq(a)).head)
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/comments/save") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val commentRequest = parsedBody.extract[CommentCreateObject]

    if(registered() && Comment.authorizedToEditComment(commentRequest, userId)){
      Comment.save(commentRequest.toRow(userId)).map(c => Comment.manyToJson(userId, Seq(c)).head)
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

  post("/views/create") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val viewRequest = parsedBody.extract[QuestionViewCreateObject]

    if(registered()) {
      QuestionView.create(viewRequest.toRow(userId))
    } else {
      throw new AuthenticationException("Not registered")
    }
  }


}