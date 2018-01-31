import axios from 'axios';
import {authenticatedSession, authenticationHeader, authenticate} from './utilities';

const domain = CONFIG ? CONFIG.frontServer ? 'http://localhost:8080' : '' : '';

/*
 * action types
 */

export const SIGNUP_USERNAME_CHANGED = 'SIGNUP_USERNAME_CHANGED';
export const SIGNUP_EMAIL_CHANGED = 'SIGNUP_EMAIL_CHANGED';
export const SIGNUP_PASSWORD_CHANGED = 'SIGNUP_PASSWORD_CHANGED';
export const SIGNUP_CLEAR_INPUTS = 'SIGNUP_CLEAR_INPUTS';
export const LOGIN_EMAIL_CHANGED = 'LOGIN_EMAIL_CHANGED';
export const LOGIN_PASSWORD_CHANGED = 'LOGIN_PASSWORD_CHANGED';
export const LOGIN_CLEAR_INPUTS = 'LOGIN_CLEAR_INPUTS';

/*
 * action creators
 */

export function cleanState() {
  return {
    type: 'CLEAN_STATE'
  };
}

export function createUser(username, email, password) {
  const request = axios({
    method: 'post',
    url: `${domain}/users/create`,
    headers: {
      'email': email,
      'password': password,
      'username': username
    }
  });

  return {
    type: 'CREATE_USER',
    payload: request
  };
}

export function createUserSuccess(loaded) {
  return {
    type: 'CREATE_USER_SUCCESS',
    payload: loaded
  };
}

export function createUserError(error) {
  return {
    type: 'CREATE_USER_ERROR',
    error: error
  };
}

export function login(email, password) {
  const request = axios({
    method: 'get',
    url: `${domain}/sessions/new`,
    headers: {
      'email': email,
      'password': password
    }
  });

  return {
    type: 'LOGIN',
    payload: request
  };
}

export function loginSuccess(loaded) {
  return {
    type: 'LOGIN_SUCCESS',
    payload: loaded
  };
}

export function loginError(error) {
  return {
    type: 'LOGIN_ERROR',
    error: error
  };
}

export function logout(session) {
  const request = axios({
    method: 'post',
    url: `${domain}/sessions/logout`,
    headers: authenticate()
  });

  return {
    type: 'LOGOUT',
    payload: request
  };
}

export function logoutSuccess(loaded) {
  return {
    type: 'LOGOUT_SUCCESS',
    payload: loaded
  };
}

export function logoutError(error) {
  return {
    type: 'LOGOUT_ERROR',
    error: error
  };
}

export function signupUsernameChanged(username) {
  return {
    type: SIGNUP_USERNAME_CHANGED,
    username: username
  }
}

export function signupEmailChanged(email) {
  return {
    type: SIGNUP_EMAIL_CHANGED,
    email: email
  }
}

export function signupPasswordChanged(password) {
  return {
    type: SIGNUP_PASSWORD_CHANGED,
    password: password
  }
}

export function signupClearInputs() {
  return {
    type: SIGNUP_CLEAR_INPUTS
  }
}

export function loginEmailChanged(email) {
  return {
    type: LOGIN_EMAIL_CHANGED,
    email: email
  }
}

export function loginPasswordChanged(password) {
  return {
    type: LOGIN_PASSWORD_CHANGED,
    password: password
  }
}

export function loginClearInputs() {
  return {
    type: LOGIN_CLEAR_INPUTS
  }
}

export function getQuestion(questionId) {
  const request = axios({
    method: 'get',
    url: `${domain}/questions/${questionId}`,
    headers: authenticate()
  });

  return {
    type: 'GET_QUESTION',
    payload: request
  };
}

export function getQuestionSuccess(loaded) {
  return {
    type: 'GET_QUESTION_SUCCESS',
    payload: loaded
  };
}

export function getQuestionError(error) {
  return {
    type: 'GET_QUESTION_ERROR',
    error: error
  };
}

export function getQuestionFeed(page) {
  const request = axios({
    method: 'get',
    url: `${domain}/questions/feed/${page}`,
    headers: authenticate()
  });

  return {
    type: 'GET_QUESTION_FEED',
    payload: request
  };
}

export function getQuestionFeedSuccess(loaded) {
  return {
    type: 'GET_QUESTION_FEED_SUCCESS',
    payload: loaded
  };
}

export function getQuestionFeedError(error) {
  return {
    type: 'GET_QUESTION_FEED_ERROR',
    error: error
  };
}

export function getCreatedQuestionFeed(page) {
  const request = axios({
    method: 'get',
    url: `${domain}/questions/created/${page}`,
    headers: authenticate()
  });

  return {
    type: 'GET_CREATED_QUESTION_FEED',
    payload: request
  };
}

export function getCreatedQuestionFeedSuccess(loaded) {
  return {
    type: 'GET_CREATED_QUESTION_FEED_SUCCESS',
    payload: loaded
  };
}

export function getCreatedQuestionFeedError(error) {
  return {
    type: 'GET_CREATED_QUESTION_FEED_ERROR',
    error: error
  };
}

export function saveQuestion(title, text, id) {
  var obj = {title: title, text: text}
  if(id) {
    obj['id'] = id
  }

  const request = axios({
    method: 'post',
    url: `${domain}/questions/save`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'SAVE_QUESTION',
    payload: request
  };

}

export function saveQuestionSuccess(loaded) {
  return {
    type: 'SAVE_QUESTION_SUCCESS',
    payload: loaded
  };
}

export function saveQuestionError(error) {
  return {
    type: 'SAVE_QUESTION_ERROR',
    error: error
  };
}

export function saveAnswer(text, questionId, id) {
  var obj = {text: text, questionId: questionId}
  if(id) {
    obj['id'] = id
  }

  const request = axios({
    method: 'post',
    url: `${domain}/answers/save`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'SAVE_ANSWER',
    payload: request
  };

}

export function saveAnswerSuccess(loaded) {
  return {
    type: 'SAVE_ANSWER_SUCCESS',
    payload: loaded
  };
}

export function saveAnswerError(error) {
  return {
    type: 'SAVE_ANSWER_ERROR',
    error: error
  };
}

export function saveComment(text, questionId, answerId, id) {
  var obj = {text: text}
  if(id) {
    obj['id'] = id
  }
  if(questionId) {
    obj['questionId'] = questionId
  }
  if(answerId) {
    obj['answerId'] = answerId
  }

  const request = axios({
    method: 'post',
    url: `${domain}/comments/save`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'SAVE_COMMENT',
    payload: request
  };

}

export function saveCommentSuccess(loaded) {
  return {
    type: 'SAVE_COMMENT_SUCCESS',
    payload: loaded
  };
}

export function saveCommentError(error) {
  return {
    type: 'SAVE_COMMENT_ERROR',
    error: error
  };
}

export function saveReview(answerId, isPositive) {
  var obj = {answerId: answerId, isPositive: isPositive}

  const request = axios({
    method: 'post',
    url: `${domain}/reviews/save`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'SAVE_REVIEW',
    payload: request
  };

}

export function saveReviewSuccess(loaded) {
  return {
    type: 'SAVE_REVIEW_SUCCESS',
    payload: loaded
  };
}

export function saveReviewError(error) {
  return {
    type: 'SAVE_REVIEW_ERROR',
    error: error
  };
}

export function createQuestionView(questionId) {
  var obj = {questionId: questionId}

  const request = axios({
    method: 'post',
    url: `${domain}/views/create`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'CREATE_QUESTION_VIEW',
    payload: request
  };

}

export function createQuestionViewSuccess(loaded) {
  return {
    type: 'CREATE_QUESTION_VIEW_SUCCESS',
    payload: loaded
  };
}

export function createQuestionViewError(error) {
  return {
    type: 'CREATE_QUESTION_VIEW_ERROR',
    error: error
  };
}

export function getSelf() {

  const request = axios({
    method: 'get',
    url: `${domain}/me`,
    headers: authenticate()
  });

  return {
    type: 'GET_SELF',
    payload: request
  };

}

export function getSelfSuccess(loaded) {
  return {
    type: 'GET_SELF_SUCCESS',
    payload: loaded
  };
}

export function getSelfError(error) {
  return {
    type: 'GET_SELF_ERROR',
    error: error
  };
}

export function deleteQuestion(questionId) {

  const obj = {id: questionId}

  const request = axios({
    method: 'post',
    url: `${domain}/questions/delete`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'DELETE_QUESTION',
    payload: request
  };

}

export function deleteQuestionSuccess(loaded) {
  return {
    type: 'DELETE_QUESTION_SUCCESS',
    payload: loaded
  };
}

export function deleteQuestionError(error) {
  return {
    type: 'DELETE_QUESTION_ERROR',
    error: error
  };
}

export function deleteAnswer(answerId) {

  const obj = {id: answerId}

  const request = axios({
    method: 'post',
    url: `${domain}/answers/delete`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'DELETE_ANSWER',
    payload: request
  };

}

export function deleteAnswerSuccess(loaded) {
  return {
    type: 'DELETE_ANSWER_SUCCESS',
    payload: loaded
  };
}

export function deleteAnswerError(error) {
  return {
    type: 'DELETE_ANSWER_ERROR',
    error: error
  };
}

export function deleteComment(commentId) {

  const obj = {id: commentId}

  const request = axios({
    method: 'post',
    url: `${domain}/comments/delete`,
    headers: authenticate(),
    data: obj
  });

  return {
    type: 'DELETE_COMMENT',
    payload: request
  };

}

export function deleteCommentSuccess(loaded) {
  return {
    type: 'DELETE_COMMENT_SUCCESS',
    payload: loaded
  };
}

export function deleteCommentError(error) {
  return {
    type: 'DELETE_COMMENT_ERROR',
    error: error
  };
}

export function questionSearch(query) {

  const request = axios({
    method: 'get',
    url: `${domain}/questions/search/${query}`,
    headers: authenticate()
  });

  return {
    type: 'QUESTION_SEARCH',
    payload: request
  };

}

export function questionSearchSuccess(loaded) {
  return {
    type: 'QUESTION_SEARCH_SUCCESS',
    payload: loaded
  };
}

export function questionSearchError(error) {
  return {
    type: 'QUESTION_SEARCH_ERROR',
    error: error
  };
}

export function validateUsername(username) {

  const request = axios({
    method: 'post',
    url: `${domain}/validate/username`,
    headers: {'username': username}
  });

  return {
    type: 'VALIDATE_USERNAME',
    payload: request
  };

}

export function validateUsernameSuccess(loaded) {
  return {
    type: 'VALIDATE_USERNAME_SUCCESS',
    payload: loaded
  };
}

export function validateUsernameError(error) {
  return {
    type: 'VALIDATE_USERNAME_ERROR',
    error: error
  };
}

export function questionsCreated() {

  const request = axios({
    method: 'get',
    url: `${domain}/questions/created`,
    headers: authenticate()
  });

  return {
    type: 'QUESTIONS_CREATED',
    payload: request
  };

}

export function questionsCreatedSuccess(loaded) {
  return {
    type: 'QUESTIONS_CREATED_SUCCESS',
    payload: loaded
  };
}

export function questionsCreatedError(error) {
  return {
    type: 'QUESTIONS_CREATED_ERROR',
    error: error
  };
}