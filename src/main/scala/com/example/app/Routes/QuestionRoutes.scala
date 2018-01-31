package com.example.app.Routes

import javax.naming.AuthenticationException

import com.example.app.models._
import com.example.app._
import org.scalatra.Ok

import scala.concurrent.Await
import scala.concurrent.duration.Duration


trait QuestionRoutes extends SlickRoutes with AuthenticationSupport with RegisteredSupport{

  get("/questions/search/:query") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val query = {params("query")}

    if(registered()){
      if(query.length > 1)
        Question.simpleSearch(query, userId)
      else
        Nil
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

  get("/questions/created") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    if(registered()){
      Question.allMyQuestions(userId)
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
      val saved = Await.result(Question.save(questionRequest.toRow(userId)), Duration.Inf)
      //Question.sendEmailToSubscribers(saved)
      EmailManager.emailActor ! saved
      Question.manyToJson(userId, Seq(saved)).head
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
      val saved = Await.result(Answer.save(answerRequest.toRow(userId)), Duration.Inf)
      //Answer.sendEmailToSubscribers(saved)
      EmailManager.emailActor ! saved
      Answer.manyToJson(userId, Seq(saved)).head
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
      val saved = Await.result(Comment.save(commentRequest.toRow(userId)), Duration.Inf)
      //Comment.sendEmailToSubscribers(saved)
      EmailManager.emailActor ! saved
      Comment.manyToJson(userId, Seq(saved)).head
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

  post("/questions/delete") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val deleteObject = parsedBody.extract[QuestionDeleteObject]

    if(registered() && Question.authorizedToEditQuestion(deleteObject.id, userId)){
      Await.result(Question.toggleActiveStatus(deleteObject.id, false), Duration.Inf)
      deleteObject
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/answers/delete") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val deleteObject = parsedBody.extract[AnswerDeleteObject]

    if(registered() && Answer.authorizedToEditAnswer(deleteObject.id, userId)){
      Await.result(Answer.toggleActiveStatus(deleteObject.id, false), Duration.Inf)
      Ok{"200"}
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

  post("/comments/delete") {
    contentType = formats("json")
    authenticate()

    val userId = user.userAccountId

    val deleteObject = parsedBody.extract[CommentDeleteObject]

    if(registered() && Comment.authorizedToEditComment(deleteObject.id, userId)){
      Await.result(Comment.toggleActiveStatus(deleteObject.id, false), Duration.Inf)
      Ok{"200"}
    } else {
      throw new AuthenticationException("Not registered")
    }
  }

}