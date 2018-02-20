import React from 'react';
import { List } from 'immutable';
import {
  Button,
  ListGroup,
  ListGroupItem,
  Grid,
  Form,
  FormGroup,
  ControlLabel,
  FormControl,
  Col
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { getQuestion, getQuestionSuccess, getQuestionError, createQuestionView, createQuestionViewSuccess, createQuestionViewError, deleteQuestion, deleteQuestionSuccess, deleteQuestionError, cleanQuestion } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import InfoBox from '../feed/InfoBox.jsx';
import CommentsListContainer from './CommentsList.jsx';
import AnswersListContainer from './AnswersList.jsx';
import QuestionTagBox from './QuestionTagBox.jsx';

class FullQuestion extends React.Component {
  constructor(props) {
    super(props);

    this.getQuestionId = this.getQuestionId.bind(this)
    this.setDeleteFinished = this.setDeleteFinished.bind(this)

    this.state = {
        deleteFinished: false
    }
  }

  getQuestionId() {
    return this.props.match.params.id
  }

  componentDidMount() {
    this.props.cleanQuestion()
    const questionId = this.getQuestionId()
    this.props.getQuestion(questionId)
    this.props.createQuestionView(questionId)
  }

  setDeleteFinished() {
    this.setState({deleteFinished: true})
  }

  render() {

    if(this.state.deleteFinished){
        return <Redirect to={"/feed"} />
    }

    const questionId = this.getQuestionId()

    const refresh = () => this.props.getQuestion(questionId)

    const question = this.props.question

    var actionBox = null

    const setDeleteFinished = this.setDeleteFinished
    const deleteQuestion = this.props.deleteQuestion(setDeleteFinished)

    if(this.props.question.getIn(['question', 'question', 'isCreator'])) {
        actionBox = <div className="action-box">
                        <Link to={"/question/"+questionId+"/edit"}>edit</Link>{' '}
                        <Link to="#" onClick={() => deleteQuestion(questionId)}>delete</Link>
                    </div>
    }


    var questionBody = <div></div>

    if(this.props.question.get('question')) {
        questionBody = <div>
                           <h2 className="question-header m-b-3">{question.getIn(['question', 'question', 'title'], '')}</h2>
                           <div className="question-item">
                               <div className="col-md-1">
                                   <InfoBox major={question.getIn(['question', 'viewCount'], 0)} minor={"views"} />
                               </div>
                               <div className="col-md-11">
                                   <div className="question-body">
                                       <p>
                                           {question.getIn(['question', 'question', 'text'], '')}
                                       </p>
                                   </div>
                                   <QuestionTagBox tags={question.getIn(['question', 'question', 'tags'])} clickFunction={function(){}} activeIds={[]} small={true} linkToQuestions={true}/>
                                   {actionBox}
                                   <div className="poster-box">
                                       <p className="info-text">Last updated by <b>{question.getIn(['question', 'question', 'creatorName'])}</b> on {new Date(question.getIn(['question', 'question', 'updatedMillis'], 0)).toDateString()}</p>
                                   </div>
                                   <div className="comments">
                                       <CommentsListContainer comments={question.getIn(['question', 'comments'], List.of())} refresh={refresh} questionId={questionId} />
                                   </div>
                               </div>
                           </div>
                           <div>
                               <h4>Answers</h4>
                               <AnswersListContainer refresh={refresh} questionId={questionId} />
                           </div>
                        </div>
    }

    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            {questionBody}
        </Grid>
      </div>

    );
  }
}

const mapStateToProps = state => {
  return {
    question: state.get('getQuestion')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {

  const questionId = ownProps.match.params.id

  const getQuestionFunction = dispatchPattern(getQuestion, getQuestionSuccess, getQuestionError)
  const createQuestionViewCallback = (view) => {
    getQuestionFunction(view.questionId)
  }

  const deleteQuestionCallback = (callback) => dispatchPattern(deleteQuestion, deleteQuestionSuccess, deleteQuestionError, callback)

  return {
    getQuestion: getQuestionFunction,
    createQuestionView: dispatchPattern(createQuestionView, createQuestionViewSuccess, createQuestionViewError, createQuestionViewCallback),
    deleteQuestion: deleteQuestionCallback,
    cleanQuestion: () => dispatch(cleanQuestion())
  };
};

const FullQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(FullQuestion);

export default FullQuestionContainer;
