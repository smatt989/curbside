package com.example.app.models

import akka.actor.{Actor, ActorSystem, Props}
import com.example.app.db.Tables.{AnswersRow, CommentsRow, QuestionsRow}

object EmailManager {

  val system = ActorSystem()

  val emailActor = system.actorOf(Props[EmailActor])
}

class EmailActor extends Actor {

  def receive = {
    case a: QuestionsRow =>
      Question.sendEmailToSubscribers(a)
    case a: AnswersRow =>
      Answer.sendEmailToSubscribers(a)
    case a: CommentsRow =>
      Comment.sendEmailToSubscribers(a)
  }
}