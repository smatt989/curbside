package com.example.app

import com.example.app.db.Tables.{QuestionsRow, RegistrationsRow, UserAccountsRow}
import com.example.app.models.{QuestionFull, Registration, UserJson}
import com.mailjet.client.{MailjetClient, MailjetRequest}
import com.mailjet.client.resource.Email
import org.joda.time.DateTime
import org.json.{JSONArray, JSONObject}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by matt on 1/26/18.
  */
object MailJetSender {

  val mailjetClient = new MailjetClient(System.getenv("MAIL_JET_PUBLIC_KEY"), System.getenv("MAIL_JET_SECRET_KEY"))
  val fromEmail = "DrCurbside@gmail.com"
  val fromName = "DrCurbside"

  val DOMAIN = sys.env.get("DOMAIN").getOrElse(s"http://localhost:8080/")

  val CONFIRMATION_EMAIL = sys.env.get("CONFIRMATION_EMAIL").getOrElse("matthew.slotkin@gmail.com")

  def sendEmail(subject: String, body: String, to: String) = {
    val request = new MailjetRequest(Email.resource)
      //.property(Email.RECIPIENTS, "matthew.slotkin@gmail.com")
      .property(Email.SUBJECT, subject)
      .property(Email.HTMLPART, body)
      .property(Email.FROMEMAIL, fromEmail)
      .property(Email.FROMNAME, fromName)
      .property(Email.RECIPIENTS, new JSONArray()
        .put(new JSONObject()
          .put("Email", to)));

    println("sending")
    val response = mailjetClient.post(request)
    System.out.println(response.getData());
  }


  def questionEdited(questionTitle: String, questionId: String, toEmail: Seq[String]) = {
    val emailTemplate = {
      "The author edited the question, "+questionTitle+"<br /><br /><a href='"+DOMAIN+"#/question/"+questionId+"'>VIEW NOW </a>"
    }

    toEmail.foreach( u =>
      sendEmail("Question edited", emailTemplate, u)
    )
  }

  def newAnswerAdded(questionTitle: String, questionId: String, toEmail: String) = {
    val emailTemplate = {
      "Someone has added a new answer to the question, "+questionTitle+"<br /><br /><a href='"+DOMAIN+"#/question/"+questionId+"'>VIEW NOW</a>"
    }

    sendEmail("New answer", emailTemplate, toEmail)
  }

  def newQuestionCommentOtherCommenters(questionTitle: String, questionId: String, toEmail: Seq[String]) = {
    val emailTemplate = {
      "Someone has added a new comment to the question, "+questionTitle+"<br /><br /><a href='"+DOMAIN+"#/question/"+questionId+"'>VIEW NOW</a>"
    }

    toEmail.foreach( u =>
      sendEmail("New comment", emailTemplate, u)
    )
  }

  def newQuestionCommentQuestionAuthor(questionTitle: String, questionId: String, toEmail: String) = {
    val emailTemplate = {
      "Someone has added a new comment to your question, "+questionTitle+"<br /><br /><a href='"+DOMAIN+"#/question/"+questionId+"'>VIEW NOW</a>"
    }

    sendEmail("New comment", emailTemplate, toEmail)
  }

  def newAnswerCommentOtherCommenters(questionTitle: String, questionId: String, toEmail: Seq[String]) = {
    val emailTemplate = {
      "Someone has added a new comment in the question, "+questionTitle+"<br /><br /><a href='"+DOMAIN+"#/question/"+questionId+"'>VIEW NOW</a>"
    }

    toEmail.foreach(u =>
      sendEmail("New comment", emailTemplate, u)
    )
  }

  def newAnswerCommentAnswerAuthor(questionTitle: String, questionId: String, toEmail: String) = {
    val emailTemplate = {
      "Someone has added a new comment to your answer to the question, "+questionTitle+"<br /><br /><a href='"+DOMAIN+"#/question"+questionId+"'>VIEW NOW</a>"
    }

    sendEmail("New comment", emailTemplate, toEmail)
  }

  def sendRegistrationConfirmEmail(user: UserAccountsRow) = {

    val newRegistration = Await.result(Registration.create(RegistrationsRow(null, user.userAccountId, None, DateTime.now().getMillis)), Duration.Inf)

    val emailTemplate = {
      user.email+" would like to join Curbside<br /><br />"+"<a href='"+DOMAIN+"register/"+newRegistration.registrationId+"'>CONFIRM</a>"
    }

    sendEmail("New Curbside Signup!", emailTemplate, CONFIRMATION_EMAIL)
  }

  def sendRegistrationAcceptedEmail(user: UserAccountsRow) = {

    val emailTemplate = {
      "You're all set! <br /><br />Your account has been confirmed and now you're free to post questions and answers on curbside!<br /><br />"+"<a href='"+DOMAIN+"'>Go to Curbside Now!</a>"
    }

    sendEmail(subject = "Welcome to Curbside!",
      body = emailTemplate,
      to = user.email)
  }
}
