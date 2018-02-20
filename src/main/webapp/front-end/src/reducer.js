import { Map, List, is } from 'immutable';
import Immutable from 'immutable';
import { getSession, setSession } from './utilities';
import {
  SIGNUP_EMAIL_CHANGED, SIGNUP_USERNAME_CHANGED, SIGNUP_PASSWORD_CHANGED, SIGNUP_CLEAR_INPUTS,
  LOGIN_EMAIL_CHANGED, LOGIN_PASSWORD_CHANGED, LOGIN_CLEAR_INPUTS
} from './actions.js';


function cleanState() {
  const sessionKey = getSession();
  setSession(sessionKey); // refresh session key

  const cleanState = Map({
    createUser: Map({loading: false, error: null}),
    login: Map({session: sessionKey, error: null, loading: false}),
    user: Map({email: null, id: null}),
    logout: Map({error: null, loading: false}),
    signupEmail: Map({ email: '' }),
    signupPassword: Map({ password: '' }),
    signupUsername: Map({ username: ''}),
    loginEmail: Map({ email: '' }),
    loginPassword: Map({ password: '' }),
    getQuestion: Map({question: null, loading: false, error: null}),
    getQuestionFeed: Map({feed: List.of(), loading: false, error: null}),
    questionFeed: Map({feed: List.of(), currentPage: 0, lastResponseSize: null}),
    getCreatedQuestionFeed: Map({feed: List.of(), loading: false, error: null}),
    saveQuestion: Map({question: null, loading: false, error: null}),
    saveAnswer: Map({answer: null, loading: false, error: null}),
    saveComment: Map({comment: null, loading: false, error: null}),
    saveReview: Map({review: null, loading: false, error: null}),
    createQuestionView: Map({loading: false, error: null}),
    getSelf: Map({user: null, loading: false, error: null}),
    deleteQuestion: Map({loading: false, error: null}),
    deleteAnswer: Map({loading: false, error: null}),
    deleteComment: Map({loading: false, error: null}),
    questionSearch: Map({results: List.of(), loading: false, error: null}),
    validateUsername: Map({validation: null, loading: false, error: null}),
    questionsCreated: Map({questions: List.of(), loading: false, error: null}),
    tagChoices: Map({tags: List.of(), loading: false, error: null}),
    questionsByTag: Map({questions: List.of(), loading: false, error: null})
  });

  return cleanState;
}

function createUser(state) {
  return state.set('createUser', Map({loading: true, error: null}));
}

function createUserSuccess(state, user) {
  return state.set('createUser', Map({loading: false, error: null}));
}

function createUserError(state, error) {
  return state.set('createUser', Map({loading: false, error: Immutable.fromJS(error)}));
}

function login(state) {
  return state.set('login', Map({session: null, error: null, loading: true}));
}

function loginSuccess(state, session) {
  setSession(session);
  return state.set('login', Map({session: session, error: null, loading: false}));
}

function loginError(state, error) {
  return state.set('login', Map({session: null, error: error, loading: false}));
}

function logout(state) {
  return state.set('logout', Map({error: null, loading: true}));
}

function logoutSuccess(state, payload) {
  setSession(null);
  const newState = state.set('login', Map({session: null, error: null, loading: false}));
  return newState.set('logout', Map({error: null, loading: false}));
}

function logoutError(state, error) {
  return state.set('logout', Map({error: error, loading: false}));
}

function signupUsernameChanged(state, username) {
  return state.set('signupUsername', Map({ username: username }));
}

function signupEmailChanged(state, email) {
  return state.set('signupEmail', Map({ email: email }));
}

function signupPasswordChanged(state, password) {
  return state.set('signupPassword', Map({ password: password }));
}

function signupClearInputs(state) {
  const newState = state.set('signupEmail', Map({ email: '' }));
  return newState.set('signupPassword', Map({ password: '' }));
}

function loginEmailChanged(state, email) {
  return state.set('loginEmail', Map({ email: email }));
}

function loginPasswordChanged(state, password) {
  return state.set('loginPassword', Map({ password: password }));
}

function loginClearInputs(state) {
  const newState = state.set('loginEmail', Map({ email: '' }));
  return newState.set('loginPassword', Map({ password: '' }));
}

function getQuestion(state) {
  return state.set('getQuestion', Map({question: null, loading: true, error: null}));
}

function getQuestionSuccess(state, question) {
  const q = Immutable.fromJS(question)

  const newState = state.set('getQuestion', Map({question: q, loading: false, error: null}));
  return addQuestionToFeed(newState, q)
}

function getQuestionError(state, error) {
  return state.set('getQuestion', Map({question: null, loading: false, error: error}));
}

function getQuestionFeed(state) {
  const currentFeed = state.getIn(['getQuestionFeed', 'feed'])
  return state.set('getQuestionFeed', Map({feed: currentFeed, loading: true, error: null}));
}

function getQuestionFeedSuccess(state, questions) {

  const qs = Immutable.fromJS(questions)

  const newState = state.set('getQuestionFeed', Map({feed: qs, loading: false, error: null}));

  return newState.set('questionFeed', Map({feed: newState.getIn(['questionFeed', 'feed']).concat(qs), currentPage: newState.getIn(['questionFeed', 'currentPage'], 0) + 1, lastResponseSize: qs.size}))
}

function getQuestionFeedError(state, error) {
  return state.set('getQuestionFeed', Map({feed: List.of(), loading: false, error: error}));
}

function getCreatedQuestionFeed(state) {
  return state.set('getCreatedQuestionFeed', Map({feed: List.of(), loading: true, error: null}));
}

function getCreatedQuestionFeedSuccess(state, questions) {
  return state.set('getCreatedQuestionFeed', Map({feed: Immutable.fromJS(questions), loading: false, error: null}));
}

function getCreatedQuestionFeedError(state, error) {
  return state.set('getCreatedQuestionFeed', Map({feed: List.of(), loading: false, error: error}));
}

function addQuestionToFeed(state, question) {
    const questionId = question.getIn(['question', 'id'])

    const feed = state.getIn(['questionFeed', 'feed'])

    const questionIndex = feed.findIndex(a => a.getIn(['question', 'id']) == questionId)

    if(questionIndex >= 0) {
        return state.setIn(['questionFeed', 'feed'], feed.update(questionIndex, (a) => question))
    } else {
        return state.setIn(['questionFeed', 'feed'], feed.unshift(question))
    }
}

function removeQuestionFromFeed(state, questionId) {
    const feed = state.getIn(['questionFeed', 'feed'])
    const questionIndex = feed.findIndex(a => a.getIn(['question', 'id']) == questionId)
    return state.setIn(['questionFeed', 'feed'], feed.delete(questionIndex))
}

function saveQuestion(state) {
  return state.set('saveQuestion', Map({question: null, loading: true, error: null}));
}

function saveQuestionSuccess(state, question) {
  return state.set('saveQuestion', Map({question: Immutable.fromJS(question), loading: false, error: null}));
}

function saveQuestionError(state, error) {
  return state.set('saveQuestion', Map({question: null, loading: false, error: error}));
}

function saveAnswer(state) {
  return state.set('saveAnswer', Map({answer: null, loading: true, error: null}));
}

function saveAnswerSuccess(state, answer) {
  return state.set('saveAnswer', Map({answer: Immutable.fromJS(answer), loading: false, error: null}));
}

function saveAnswerError(state, error) {
  return state.set('saveAnswer', Map({answer: null, loading: false, error: error}));
}

function saveComment(state) {
  return state.set('saveComment', Map({comment: null, loading: true, error: null}));
}

function saveCommentSuccess(state, comment) {
  return state.set('saveComment', Map({comment: Immutable.fromJS(comment), loading: false, error: null}));
}

function saveCommentError(state, error){
  return state.set('saveComment', Map({comment: null, loading: false, error: error}));
}

function saveReview(state) {
  return state.set('saveReview', Map({review: null, loading: true, error: null}));
}

function saveReviewSuccess(state, review) {
  return state.set('saveReview', Map({review: Immutable.fromJS(review), loading: false, error: null}));
}

function saveReviewError(state, error) {
  return state.set('saveReview', Map({review: null, loading: false, error: error}));
}

function createQuestionView(state) {
  return state.set('createQuestionView', Map({loading: true, error: null}));
}

function createQuestionViewSuccess(state, view) {
  return state.set('createQuestionView', Map({loading: false, error: null}));
}

function createQuestionViewError(state, error) {
  return state.set('createQuestionView', Map({loading: false, error: error}));
}

function getSelf(state) {
  return state.set('getSelf', Map({user: null, loading: true, error: null}));
}

function getSelfSuccess(state, user) {
  return state.set('getSelf', Map({user: Immutable.fromJS(user), loading: false, error: null}));
}

function getSelfError(state, error) {
  return state.set('getSelf', Map({user: null, loading: false, error: error}));
}

function deleteQuestion(state) {
  return state.set('deleteQuestion', Map({loading: true, error: null}));
}

function deleteQuestionSuccess(state, deleteObj){
  const newState = state.set('deleteQuestion', Map({loading: false, error: null}));

  return removeQuestionFromFeed(newState, deleteObj.id)
}

function deleteQuestionError(state, error) {
  return state.set('deleteQuestion', Map({loading: false, error: error}));
}

function deleteAnswer(state) {
  return state.set('deleteAnswer', Map({loading: true, error: null}));
}

function deleteAnswerSuccess(state){
  return state.set('deleteAnswer', Map({loading: false, error: null}));
}

function deleteAnswerError(state, error) {
  return state.set('deleteAnswer', Map({loading: false, error: error}));
}

function deleteComment(state) {
  return state.set('deleteComment', Map({loading: true, error: null}));
}

function deleteCommentSuccess(state){
  return state.set('deleteComment', Map({loading: false, error: null}));
}

function deleteCommentError(state, error) {
  return state.set('deleteComment', Map({loading: false, error: error}));
}

function questionSearch(state){
  return state.set('questionSearch', Map({results: List.of(), loading: true, error: null}));
}

function questionSearchSuccess(state, questions) {
  return state.set('questionSearch', Map({results: Immutable.fromJS(questions), loading: false, error: null}));
}

function questionSearchError(state, error) {
  return state.set('questionSearch', Map({results: List.of(), loading: false, error: error}));
}

function validateUsername(state) {
  return state.set('validateUsername', Map({validation: null, loading: true, error: null}));
}

function validateUsernameSuccess(state, validation){
  return state.set('validateUsername', Map({validation: Immutable.fromJS(validation), loading: false, error: null}));
}

function validateUsernameError(state, error) {
  return state.set('validateUsername', Map({validation: null, loading: false, error: error}));
}

function questionsCreated(state) {
  return state.set('questionsCreated', Map({questions: List.of(), loading: true, error: null}));
}

function questionsCreatedSuccess(state, questions) {
  return state.set('questionsCreated', Map({questions: Immutable.fromJS(questions), loading: false, error: null}));
}

function questionsCreatedError(state, error) {
  return state.set('questionsCreated', Map({questions: List.of(), loading: false, error: error}));
}

function cleanQuestion(state){
  return state.set('getQuestion', Map({question: null, loading: false, error: null}));
}

function getTagChoices(state) {
  return state.set('tagChoices', Map({tags: List.of(), loading: true, error: null}));
}

function getTagChoicesSuccess(state, tags) {
  return state.set('tagChoices', Map({tags: Immutable.fromJS(tags), loading: false, error: null}));
}

function getTagChoicesError(state, error) {
  return state.set('tagChoices', Map({tags: List.of(), loading: false, error: error}));
}

function questionsByTag(state){
  return state.set('questionsByTag', Map({questions: List.of(), loading: true, error: null}));
}

function questionsByTagSuccess(state, questions) {
  return state.set('questionsByTag', Map({questions: Immutable.fromJS(questions), loading: false, error: null}));
}

function questionsByTagError(state, error) {
  return state.set('questionsByTag', Map({questions: List.of(), loading: false, error: error}));
}

export default function reducer(state = Map(), action) {
  switch (action.type) {
    case 'CLEAN_STATE':
      return cleanState();
    case 'CREATE_USER':
      return createUser(state);
    case 'CREATE_USER_SUCCESS':
      return createUserSuccess(state, action.email);
    case 'CREATE_USER_ERROR':
      return createUserError(state, action.error);
    case 'LOGIN':
      return login(state);
    case 'LOGIN_SUCCESS':
      return loginSuccess(state, action.payload);
    case 'LOGIN_ERROR':
      return loginError(state, action.error);
    case 'LOGOUT':
      return logout(state);
    case 'LOGOUT_SUCCESS':
      return logoutSuccess(state, action.payload);
    case 'LOGOUT_ERROR':
      return logoutError(state, action.error);
    case SIGNUP_USERNAME_CHANGED:
      return signupUsernameChanged(state, action.username);
    case SIGNUP_EMAIL_CHANGED:
      return signupEmailChanged(state, action.email);
    case SIGNUP_PASSWORD_CHANGED:
      return signupPasswordChanged(state, action.password);
    case SIGNUP_CLEAR_INPUTS:
      return signupClearInputs(state);
    case LOGIN_EMAIL_CHANGED:
      return loginEmailChanged(state, action.email);
    case LOGIN_PASSWORD_CHANGED:
      return loginPasswordChanged(state, action.password);
    case LOGIN_CLEAR_INPUTS:
      return loginClearInputs(state);
    case 'GET_QUESTION':
      return getQuestion(state);
    case 'GET_QUESTION_SUCCESS':
      return getQuestionSuccess(state, action.payload);
    case 'GET_QUESTION_ERROR':
      return getQuestionError(state, action.error);
    case 'GET_QUESTION_FEED':
      return getQuestionFeed(state);
    case 'GET_QUESTION_FEED_SUCCESS':
      return getQuestionFeedSuccess(state, action.payload);
    case 'GET_QUESTION_FEED_ERROR':
      return getQuestionFeedError(state, action.error);
    case 'GET_CREATED_QUESTION_FEED':
      return getCreatedQuestionFeed(state);
    case 'GET_CREATED_QUESTION_FEED_SUCCESS':
      return getCreatedQuestionFeedSuccess(state, action.payload);
    case 'GET_CREATED_QUESTION_FEED_ERROR':
      return getCreatedQuestionFeedError(state, action.error);
    case 'SAVE_QUESTION':
      return saveQuestion(state);
    case 'SAVE_QUESTION_SUCCESS':
      return saveQuestionSuccess(state, action.payload);
    case 'SAVE_QUESTION_ERROR':
      return saveQuestionError(state, action.error);
    case 'SAVE_ANSWER':
      return saveAnswer(state);
    case 'SAVE_ANSWER_SUCCESS':
      return saveAnswerSuccess(state, action.payload);
    case 'SAVE_ANSWER_ERROR':
      return saveAnswerError(state, action.error);
    case 'SAVE_COMMENT':
      return saveComment(state);
    case 'SAVE_COMMENT_SUCCESS':
      return saveCommentSuccess(state, action.payload);
    case 'SAVE_COMMENT_ERROR':
      return saveCommentError(state, action.error);
    case 'SAVE_REVIEW':
      return saveReview(state);
    case 'SAVE_REVIEW_SUCCESS':
      return saveReviewSuccess(state, action.payload);
    case 'SAVE_REVIEW_ERROR':
      return saveReviewError(state, action.error);
    case 'CREATE_QUESTION_VIEW':
      return createQuestionView(state);
    case 'CREATE_QUESTION_VIEW_SUCCESS':
      return createQuestionViewSuccess(state, action.payload);
    case 'CREATE_QUESTION_VIEW_ERROR':
      return createQuestionViewError(state, action.error);
    case 'GET_SELF':
      return getSelf(state);
    case 'GET_SELF_SUCCESS':
      return getSelfSuccess(state, action.payload);
    case 'GET_SELF_ERROR':
      return getSelfError(state, action.error);
    case 'DELETE_QUESTION':
      return deleteQuestion(state);
    case 'DELETE_QUESTION_SUCCESS':
      return deleteQuestionSuccess(state, action.payload);
    case 'DELETE_QUESTION_ERROR':
      return deleteQuestionError(state, action.error);
    case 'DELETE_ANSWER':
      return deleteAnswer(state);
    case 'DELETE_ANSWER_SUCCESS':
      return deleteAnswerSuccess(state);
    case 'DELETE_ANSWER_ERROR':
      return deleteAnswerError(state, action.error);
    case 'DELETE_COMMENT':
      return deleteComment(state);
    case 'DELETE_COMMENT_SUCCESS':
      return deleteCommentSuccess(state);
    case 'DELETE_COMMENT_ERROR':
      return deleteCommentError(state, action.error);
    case 'QUESTION_SEARCH':
      return questionSearch(state);
    case 'QUESTION_SEARCH_SUCCESS':
      return questionSearchSuccess(state, action.payload);
    case 'QUESTION_SEARCH_ERROR':
      return questionSearchError(state, action.error);
    case 'VALIDATE_USERNAME':
      return validateUsername(state);
    case 'VALIDATE_USERNAME_SUCCESS':
      return validateUsernameSuccess(state, action.payload);
    case 'VALIDATE_USERNAME_ERROR':
      return validateUsernameError(state, action.error);
    case 'QUESTIONS_CREATED':
      return questionsCreated(state);
    case 'QUESTIONS_CREATED_SUCCESS':
      return questionsCreatedSuccess(state, action.payload);
    case 'QUESTIONS_CREATED_ERROR':
      return questionsCreatedError(state, action.error);
    case 'CLEAN_QUESTION':
      return cleanQuestion(state);
    case 'GET_TAG_CHOICES':
      return getTagChoices(state);
    case 'GET_TAG_CHOICES_SUCCESS':
      return getTagChoicesSuccess(state, action.payload);
    case 'GET_TAG_CHOICES_ERROR':
      return getTagChoicesError(state, action.error);
    case 'QUESTIONS_BY_TAG':
      return questionsByTag(state);
    case 'QUESTIONS_BY_TAG_SUCCESS':
      return questionsByTagSuccess(state, action.payload);
    case 'QUESTIONS_BY_TAG_ERROR':
      return questionsByTagError(state, action.error);
    default:
      return state;
  }
};
