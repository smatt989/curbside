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
import { getQuestion, getQuestionSuccess, getQuestionError, createQuestionView, createQuestionViewSuccess, createQuestionViewError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import InfoBox from '../feed/InfoBox.jsx';
import CommentsListContainer from './CommentsList.jsx';
import AnswersListContainer from './AnswersList.jsx';

class FullQuestion extends React.Component {
  constructor(props) {
    super(props);

    this.getQuestionId = this.getQuestionId.bind(this)
  }

  getQuestionId() {
    return this.props.match.params.id
  }

  componentDidMount() {
    const questionId = this.getQuestionId()
    this.props.getQuestion(questionId)
    this.props.createQuestionView(questionId)
  }

  render() {
    const questionId = this.getQuestionId()

    const refresh = () => this.props.getQuestion(questionId)

    const question = this.props.question

    var actionBox = null

    if(this.props.question.getIn(['question', 'question', 'isCreator'])) {
        actionBox = <div className="action-box">
                        <Link to={"/question/"+questionId+"/edit"}>edit</Link>
                    </div>
    }

    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            <h3 className="m-b-3">{question.getIn(['question', 'question', 'title'], '')}</h3>
            <div>
                <div className="col-md-1">
                    <InfoBox major={question.getIn(['question', 'viewCount'], 0)} minor={"views"} />
                </div>
                <div className="col-md-11">
                    <div className="question-body">
                        <p>
                            {question.getIn(['question', 'question', 'text'], '')}
                        </p>
                    </div>
                    {actionBox}
                    <div className="poster-box">
                        <p>Last updated by <b>{question.getIn(['question', 'question', 'creatorName'])}</b> on {new Date(question.getIn(['question', 'question', 'updatedMillis'], 0)).toDateString()}</p>
                    </div>
                    <div className="comments">
                        <CommentsListContainer comments={question.getIn(['question', 'comments'], List.of())} refresh={refresh} questionId={questionId} />
                    </div>
                </div>
            </div>
            <div>
                <h3>Answers</h3>
                <AnswersListContainer refresh={refresh} questionId={questionId} />
            </div>
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

  const getQuestionFunction = dispatchPattern(getQuestion, getQuestionSuccess, getQuestionError)
  const createQuestionViewCallback = (view) => {
    getQuestionFunction(view.questionId)
  }

  return {
    getQuestion: getQuestionFunction,
    createQuestionView: dispatchPattern(createQuestionView, createQuestionViewSuccess, createQuestionViewError, createQuestionViewCallback)
  };
};

const FullQuestionContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(FullQuestion);

export default FullQuestionContainer;
