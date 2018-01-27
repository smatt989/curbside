package com.example.app.db
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Answers.schema, Comments.schema, DeviceTokens.schema, Migrations.schema, Questions.schema, Registrations.schema, Reviews.schema, UserAccounts.schema, UserConnections.schema, UserSessions.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Answers
   *  @param answerId Database column ANSWER_ID SqlType(VARCHAR), PrimaryKey
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR)
   *  @param creatorId Database column CREATOR_ID SqlType(INTEGER)
   *  @param answerText Database column ANSWER_TEXT SqlType(VARCHAR)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT)
   *  @param updatedMillis Database column UPDATED_MILLIS SqlType(BIGINT) */
  case class AnswersRow(answerId: String, questionId: String, creatorId: Int, answerText: String, createdMillis: Long, updatedMillis: Long)
  /** GetResult implicit for fetching AnswersRow objects using plain SQL queries */
  implicit def GetResultAnswersRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Long]): GR[AnswersRow] = GR{
    prs => import prs._
    AnswersRow.tupled((<<[String], <<[String], <<[Int], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table ANSWERS. Objects of this class serve as prototypes for rows in queries. */
  class Answers(_tableTag: Tag) extends Table[AnswersRow](_tableTag, Some("CURBSIDE"), "ANSWERS") {
    def * = (answerId, questionId, creatorId, answerText, createdMillis, updatedMillis) <> (AnswersRow.tupled, AnswersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(answerId), Rep.Some(questionId), Rep.Some(creatorId), Rep.Some(answerText), Rep.Some(createdMillis), Rep.Some(updatedMillis)).shaped.<>({r=>import r._; _1.map(_=> AnswersRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ANSWER_ID SqlType(VARCHAR), PrimaryKey */
    val answerId: Rep[String] = column[String]("ANSWER_ID", O.PrimaryKey)
    /** Database column QUESTION_ID SqlType(VARCHAR) */
    val questionId: Rep[String] = column[String]("QUESTION_ID")
    /** Database column CREATOR_ID SqlType(INTEGER) */
    val creatorId: Rep[Int] = column[Int]("CREATOR_ID")
    /** Database column ANSWER_TEXT SqlType(VARCHAR) */
    val answerText: Rep[String] = column[String]("ANSWER_TEXT")
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")
    /** Database column UPDATED_MILLIS SqlType(BIGINT) */
    val updatedMillis: Rep[Long] = column[Long]("UPDATED_MILLIS")

    /** Foreign key referencing Questions (database name ANSWER_TO_QUESTIONS_FK) */
    lazy val questionsFk = foreignKey("ANSWER_TO_QUESTIONS_FK", questionId, Questions)(r => r.questionId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserAccounts (database name ANSWER_TO_USERS_FK) */
    lazy val userAccountsFk = foreignKey("ANSWER_TO_USERS_FK", creatorId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Answers */
  lazy val Answers = new TableQuery(tag => new Answers(tag))

  /** Entity class storing rows of table Comments
   *  @param commentId Database column COMMENT_ID SqlType(VARCHAR), PrimaryKey
   *  @param creatorId Database column CREATOR_ID SqlType(INTEGER)
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR), Default(None)
   *  @param answerId Database column ANSWER_ID SqlType(VARCHAR), Default(None)
   *  @param commentText Database column COMMENT_TEXT SqlType(VARCHAR)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT)
   *  @param updatedMillis Database column UPDATED_MILLIS SqlType(BIGINT) */
  case class CommentsRow(commentId: String, creatorId: Int, questionId: Option[String] = None, answerId: Option[String] = None, commentText: String, createdMillis: Long, updatedMillis: Long)
  /** GetResult implicit for fetching CommentsRow objects using plain SQL queries */
  implicit def GetResultCommentsRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]], e3: GR[Long]): GR[CommentsRow] = GR{
    prs => import prs._
    CommentsRow.tupled((<<[String], <<[Int], <<?[String], <<?[String], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table COMMENTS. Objects of this class serve as prototypes for rows in queries. */
  class Comments(_tableTag: Tag) extends Table[CommentsRow](_tableTag, Some("CURBSIDE"), "COMMENTS") {
    def * = (commentId, creatorId, questionId, answerId, commentText, createdMillis, updatedMillis) <> (CommentsRow.tupled, CommentsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(commentId), Rep.Some(creatorId), questionId, answerId, Rep.Some(commentText), Rep.Some(createdMillis), Rep.Some(updatedMillis)).shaped.<>({r=>import r._; _1.map(_=> CommentsRow.tupled((_1.get, _2.get, _3, _4, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column COMMENT_ID SqlType(VARCHAR), PrimaryKey */
    val commentId: Rep[String] = column[String]("COMMENT_ID", O.PrimaryKey)
    /** Database column CREATOR_ID SqlType(INTEGER) */
    val creatorId: Rep[Int] = column[Int]("CREATOR_ID")
    /** Database column QUESTION_ID SqlType(VARCHAR), Default(None) */
    val questionId: Rep[Option[String]] = column[Option[String]]("QUESTION_ID", O.Default(None))
    /** Database column ANSWER_ID SqlType(VARCHAR), Default(None) */
    val answerId: Rep[Option[String]] = column[Option[String]]("ANSWER_ID", O.Default(None))
    /** Database column COMMENT_TEXT SqlType(VARCHAR) */
    val commentText: Rep[String] = column[String]("COMMENT_TEXT")
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")
    /** Database column UPDATED_MILLIS SqlType(BIGINT) */
    val updatedMillis: Rep[Long] = column[Long]("UPDATED_MILLIS")

    /** Foreign key referencing Answers (database name COMMENT_TO_ANSWER_FK) */
    lazy val answersFk = foreignKey("COMMENT_TO_ANSWER_FK", answerId, Answers)(r => Rep.Some(r.answerId), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing Questions (database name COMMENT_TO_QUESTIONS_FK) */
    lazy val questionsFk = foreignKey("COMMENT_TO_QUESTIONS_FK", questionId, Questions)(r => Rep.Some(r.questionId), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserAccounts (database name COMMENT_TO_USERS_FK) */
    lazy val userAccountsFk = foreignKey("COMMENT_TO_USERS_FK", creatorId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Comments */
  lazy val Comments = new TableQuery(tag => new Comments(tag))

  /** Entity class storing rows of table DeviceTokens
   *  @param deviceTokenId Database column DEVICE_TOKEN_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param userId Database column USER_ID SqlType(INTEGER)
   *  @param deviceToken Database column DEVICE_TOKEN SqlType(VARCHAR), Default(None) */
  case class DeviceTokensRow(deviceTokenId: Int, userId: Int, deviceToken: Option[String] = None)
  /** GetResult implicit for fetching DeviceTokensRow objects using plain SQL queries */
  implicit def GetResultDeviceTokensRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[DeviceTokensRow] = GR{
    prs => import prs._
    DeviceTokensRow.tupled((<<[Int], <<[Int], <<?[String]))
  }
  /** Table description of table DEVICE_TOKENS. Objects of this class serve as prototypes for rows in queries. */
  class DeviceTokens(_tableTag: Tag) extends Table[DeviceTokensRow](_tableTag, Some("CURBSIDE"), "DEVICE_TOKENS") {
    def * = (deviceTokenId, userId, deviceToken) <> (DeviceTokensRow.tupled, DeviceTokensRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(deviceTokenId), Rep.Some(userId), deviceToken).shaped.<>({r=>import r._; _1.map(_=> DeviceTokensRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column DEVICE_TOKEN_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val deviceTokenId: Rep[Int] = column[Int]("DEVICE_TOKEN_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("USER_ID")
    /** Database column DEVICE_TOKEN SqlType(VARCHAR), Default(None) */
    val deviceToken: Rep[Option[String]] = column[Option[String]]("DEVICE_TOKEN", O.Default(None))

    /** Foreign key referencing UserAccounts (database name DEVICE_TOKENS_TO_USER_FK) */
    lazy val userAccountsFk = foreignKey("DEVICE_TOKENS_TO_USER_FK", userId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table DeviceTokens */
  lazy val DeviceTokens = new TableQuery(tag => new DeviceTokens(tag))

  /** Entity class storing rows of table Migrations
   *  @param migrationId Database column MIGRATION_ID SqlType(INTEGER), PrimaryKey */
  case class MigrationsRow(migrationId: Int)
  /** GetResult implicit for fetching MigrationsRow objects using plain SQL queries */
  implicit def GetResultMigrationsRow(implicit e0: GR[Int]): GR[MigrationsRow] = GR{
    prs => import prs._
    MigrationsRow(<<[Int])
  }
  /** Table description of table MIGRATIONS. Objects of this class serve as prototypes for rows in queries. */
  class Migrations(_tableTag: Tag) extends Table[MigrationsRow](_tableTag, Some("CURBSIDE"), "MIGRATIONS") {
    def * = migrationId <> (MigrationsRow, MigrationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = Rep.Some(migrationId).shaped.<>(r => r.map(_=> MigrationsRow(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MIGRATION_ID SqlType(INTEGER), PrimaryKey */
    val migrationId: Rep[Int] = column[Int]("MIGRATION_ID", O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Migrations */
  lazy val Migrations = new TableQuery(tag => new Migrations(tag))

  /** Entity class storing rows of table Questions
   *  @param questionId Database column QUESTION_ID SqlType(VARCHAR), PrimaryKey
   *  @param creatorId Database column CREATOR_ID SqlType(INTEGER)
   *  @param questionText Database column QUESTION_TEXT SqlType(VARCHAR)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT)
   *  @param updatedMillis Database column UPDATED_MILLIS SqlType(BIGINT) */
  case class QuestionsRow(questionId: String, creatorId: Int, questionText: String, createdMillis: Long, updatedMillis: Long)
  /** GetResult implicit for fetching QuestionsRow objects using plain SQL queries */
  implicit def GetResultQuestionsRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Long]): GR[QuestionsRow] = GR{
    prs => import prs._
    QuestionsRow.tupled((<<[String], <<[Int], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table QUESTIONS. Objects of this class serve as prototypes for rows in queries. */
  class Questions(_tableTag: Tag) extends Table[QuestionsRow](_tableTag, Some("CURBSIDE"), "QUESTIONS") {
    def * = (questionId, creatorId, questionText, createdMillis, updatedMillis) <> (QuestionsRow.tupled, QuestionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(questionId), Rep.Some(creatorId), Rep.Some(questionText), Rep.Some(createdMillis), Rep.Some(updatedMillis)).shaped.<>({r=>import r._; _1.map(_=> QuestionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column QUESTION_ID SqlType(VARCHAR), PrimaryKey */
    val questionId: Rep[String] = column[String]("QUESTION_ID", O.PrimaryKey)
    /** Database column CREATOR_ID SqlType(INTEGER) */
    val creatorId: Rep[Int] = column[Int]("CREATOR_ID")
    /** Database column QUESTION_TEXT SqlType(VARCHAR) */
    val questionText: Rep[String] = column[String]("QUESTION_TEXT")
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")
    /** Database column UPDATED_MILLIS SqlType(BIGINT) */
    val updatedMillis: Rep[Long] = column[Long]("UPDATED_MILLIS")

    /** Foreign key referencing UserAccounts (database name QUESTION_TO_USERS_FK) */
    lazy val userAccountsFk = foreignKey("QUESTION_TO_USERS_FK", creatorId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Questions */
  lazy val Questions = new TableQuery(tag => new Questions(tag))

  /** Entity class storing rows of table Registrations
   *  @param registrationId Database column REGISTRATION_ID SqlType(VARCHAR), PrimaryKey
   *  @param userId Database column USER_ID SqlType(INTEGER)
   *  @param imageUrl Database column IMAGE_URL SqlType(VARCHAR), Default(None)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT) */
  case class RegistrationsRow(registrationId: String, userId: Int, imageUrl: Option[String] = None, createdMillis: Long)
  /** GetResult implicit for fetching RegistrationsRow objects using plain SQL queries */
  implicit def GetResultRegistrationsRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]], e3: GR[Long]): GR[RegistrationsRow] = GR{
    prs => import prs._
    RegistrationsRow.tupled((<<[String], <<[Int], <<?[String], <<[Long]))
  }
  /** Table description of table REGISTRATIONS. Objects of this class serve as prototypes for rows in queries. */
  class Registrations(_tableTag: Tag) extends Table[RegistrationsRow](_tableTag, Some("CURBSIDE"), "REGISTRATIONS") {
    def * = (registrationId, userId, imageUrl, createdMillis) <> (RegistrationsRow.tupled, RegistrationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(registrationId), Rep.Some(userId), imageUrl, Rep.Some(createdMillis)).shaped.<>({r=>import r._; _1.map(_=> RegistrationsRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column REGISTRATION_ID SqlType(VARCHAR), PrimaryKey */
    val registrationId: Rep[String] = column[String]("REGISTRATION_ID", O.PrimaryKey)
    /** Database column USER_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("USER_ID")
    /** Database column IMAGE_URL SqlType(VARCHAR), Default(None) */
    val imageUrl: Rep[Option[String]] = column[Option[String]]("IMAGE_URL", O.Default(None))
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")

    /** Foreign key referencing UserAccounts (database name REGISTRATION_TO_USERS_FK) */
    lazy val userAccountsFk = foreignKey("REGISTRATION_TO_USERS_FK", userId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Registrations */
  lazy val Registrations = new TableQuery(tag => new Registrations(tag))

  /** Entity class storing rows of table Reviews
   *  @param reviewId Database column REVIEW_ID SqlType(VARCHAR), PrimaryKey
   *  @param answerId Database column ANSWER_ID SqlType(VARCHAR)
   *  @param creatorId Database column CREATOR_ID SqlType(INTEGER)
   *  @param isPositive Database column IS_POSITIVE SqlType(BOOLEAN)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT) */
  case class ReviewsRow(reviewId: String, answerId: String, creatorId: Int, isPositive: Boolean, createdMillis: Long)
  /** GetResult implicit for fetching ReviewsRow objects using plain SQL queries */
  implicit def GetResultReviewsRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Boolean], e3: GR[Long]): GR[ReviewsRow] = GR{
    prs => import prs._
    ReviewsRow.tupled((<<[String], <<[String], <<[Int], <<[Boolean], <<[Long]))
  }
  /** Table description of table REVIEWS. Objects of this class serve as prototypes for rows in queries. */
  class Reviews(_tableTag: Tag) extends Table[ReviewsRow](_tableTag, Some("CURBSIDE"), "REVIEWS") {
    def * = (reviewId, answerId, creatorId, isPositive, createdMillis) <> (ReviewsRow.tupled, ReviewsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(reviewId), Rep.Some(answerId), Rep.Some(creatorId), Rep.Some(isPositive), Rep.Some(createdMillis)).shaped.<>({r=>import r._; _1.map(_=> ReviewsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column REVIEW_ID SqlType(VARCHAR), PrimaryKey */
    val reviewId: Rep[String] = column[String]("REVIEW_ID", O.PrimaryKey)
    /** Database column ANSWER_ID SqlType(VARCHAR) */
    val answerId: Rep[String] = column[String]("ANSWER_ID")
    /** Database column CREATOR_ID SqlType(INTEGER) */
    val creatorId: Rep[Int] = column[Int]("CREATOR_ID")
    /** Database column IS_POSITIVE SqlType(BOOLEAN) */
    val isPositive: Rep[Boolean] = column[Boolean]("IS_POSITIVE")
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")

    /** Foreign key referencing Answers (database name REVIEW_TO_ANSWERS_FK) */
    lazy val answersFk = foreignKey("REVIEW_TO_ANSWERS_FK", answerId, Answers)(r => r.answerId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserAccounts (database name REVIEW_TO_USERS_FK) */
    lazy val userAccountsFk = foreignKey("REVIEW_TO_USERS_FK", creatorId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Reviews */
  lazy val Reviews = new TableQuery(tag => new Reviews(tag))

  /** Entity class storing rows of table UserAccounts
   *  @param userAccountId Database column USER_ACCOUNT_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param username Database column USERNAME SqlType(VARCHAR)
   *  @param email Database column EMAIL SqlType(VARCHAR)
   *  @param hashedPassword Database column HASHED_PASSWORD SqlType(VARCHAR)
   *  @param registered Database column REGISTERED SqlType(BOOLEAN)
   *  @param createdMillis Database column CREATED_MILLIS SqlType(BIGINT) */
  case class UserAccountsRow(userAccountId: Int, username: String, email: String, hashedPassword: String, registered: Boolean, createdMillis: Long)
  /** GetResult implicit for fetching UserAccountsRow objects using plain SQL queries */
  implicit def GetResultUserAccountsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean], e3: GR[Long]): GR[UserAccountsRow] = GR{
    prs => import prs._
    UserAccountsRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Boolean], <<[Long]))
  }
  /** Table description of table USER_ACCOUNTS. Objects of this class serve as prototypes for rows in queries. */
  class UserAccounts(_tableTag: Tag) extends Table[UserAccountsRow](_tableTag, Some("CURBSIDE"), "USER_ACCOUNTS") {
    def * = (userAccountId, username, email, hashedPassword, registered, createdMillis) <> (UserAccountsRow.tupled, UserAccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userAccountId), Rep.Some(username), Rep.Some(email), Rep.Some(hashedPassword), Rep.Some(registered), Rep.Some(createdMillis)).shaped.<>({r=>import r._; _1.map(_=> UserAccountsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_ACCOUNT_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val userAccountId: Rep[Int] = column[Int]("USER_ACCOUNT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USERNAME SqlType(VARCHAR) */
    val username: Rep[String] = column[String]("USERNAME")
    /** Database column EMAIL SqlType(VARCHAR) */
    val email: Rep[String] = column[String]("EMAIL")
    /** Database column HASHED_PASSWORD SqlType(VARCHAR) */
    val hashedPassword: Rep[String] = column[String]("HASHED_PASSWORD")
    /** Database column REGISTERED SqlType(BOOLEAN) */
    val registered: Rep[Boolean] = column[Boolean]("REGISTERED")
    /** Database column CREATED_MILLIS SqlType(BIGINT) */
    val createdMillis: Rep[Long] = column[Long]("CREATED_MILLIS")
  }
  /** Collection-like TableQuery object for table UserAccounts */
  lazy val UserAccounts = new TableQuery(tag => new UserAccounts(tag))

  /** Entity class storing rows of table UserConnections
   *  @param userConnectionId Database column USER_CONNECTION_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param senderUserId Database column SENDER_USER_ID SqlType(INTEGER)
   *  @param receiverUserId Database column RECEIVER_USER_ID SqlType(INTEGER) */
  case class UserConnectionsRow(userConnectionId: Int, senderUserId: Int, receiverUserId: Int)
  /** GetResult implicit for fetching UserConnectionsRow objects using plain SQL queries */
  implicit def GetResultUserConnectionsRow(implicit e0: GR[Int]): GR[UserConnectionsRow] = GR{
    prs => import prs._
    UserConnectionsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table USER_CONNECTIONS. Objects of this class serve as prototypes for rows in queries. */
  class UserConnections(_tableTag: Tag) extends Table[UserConnectionsRow](_tableTag, Some("CURBSIDE"), "USER_CONNECTIONS") {
    def * = (userConnectionId, senderUserId, receiverUserId) <> (UserConnectionsRow.tupled, UserConnectionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userConnectionId), Rep.Some(senderUserId), Rep.Some(receiverUserId)).shaped.<>({r=>import r._; _1.map(_=> UserConnectionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_CONNECTION_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val userConnectionId: Rep[Int] = column[Int]("USER_CONNECTION_ID", O.AutoInc, O.PrimaryKey)
    /** Database column SENDER_USER_ID SqlType(INTEGER) */
    val senderUserId: Rep[Int] = column[Int]("SENDER_USER_ID")
    /** Database column RECEIVER_USER_ID SqlType(INTEGER) */
    val receiverUserId: Rep[Int] = column[Int]("RECEIVER_USER_ID")

    /** Foreign key referencing UserAccounts (database name USER_CONNECTIONS_RECEIVER_TO_USERS_FK) */
    lazy val userAccountsFk1 = foreignKey("USER_CONNECTIONS_RECEIVER_TO_USERS_FK", receiverUserId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserAccounts (database name USER_CONNECTIONS_SENDER_TO_USERS_FK) */
    lazy val userAccountsFk2 = foreignKey("USER_CONNECTIONS_SENDER_TO_USERS_FK", senderUserId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserConnections */
  lazy val UserConnections = new TableQuery(tag => new UserConnections(tag))

  /** Entity class storing rows of table UserSessions
   *  @param userSessionId Database column USER_SESSION_ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param userId Database column USER_ID SqlType(INTEGER)
   *  @param hashString Database column HASH_STRING SqlType(VARCHAR) */
  case class UserSessionsRow(userSessionId: Int, userId: Int, hashString: String)
  /** GetResult implicit for fetching UserSessionsRow objects using plain SQL queries */
  implicit def GetResultUserSessionsRow(implicit e0: GR[Int], e1: GR[String]): GR[UserSessionsRow] = GR{
    prs => import prs._
    UserSessionsRow.tupled((<<[Int], <<[Int], <<[String]))
  }
  /** Table description of table USER_SESSIONS. Objects of this class serve as prototypes for rows in queries. */
  class UserSessions(_tableTag: Tag) extends Table[UserSessionsRow](_tableTag, Some("CURBSIDE"), "USER_SESSIONS") {
    def * = (userSessionId, userId, hashString) <> (UserSessionsRow.tupled, UserSessionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userSessionId), Rep.Some(userId), Rep.Some(hashString)).shaped.<>({r=>import r._; _1.map(_=> UserSessionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_SESSION_ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val userSessionId: Rep[Int] = column[Int]("USER_SESSION_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_ID SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("USER_ID")
    /** Database column HASH_STRING SqlType(VARCHAR) */
    val hashString: Rep[String] = column[String]("HASH_STRING")

    /** Foreign key referencing UserAccounts (database name USER_SESSIONS_TO_USER_FK) */
    lazy val userAccountsFk = foreignKey("USER_SESSIONS_TO_USER_FK", userId, UserAccounts)(r => r.userAccountId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserSessions */
  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))
}
