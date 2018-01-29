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

    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            <h3 className="m-b-3">{this.props.question.getIn(['question', 'question', 'title'], '')}</h3>
            <div>
                <div className="col-md-1">
                    <InfoBox major={this.props.question.getIn(['question', 'viewCount'], 0)} minor={"views"} />
                </div>
                <div className="col-md-11">
                    <div className="question-body">
                        <p>
                            {this.props.question.getIn(['question', 'question', 'text'], '')}
                        </p>
                    </div>
                    <div className="action-box">
                        <Link to="/edit">edit</Link>
                    </div>
                    <div className="poster-box">
                        <p>Asked by <b>matt</b> on {new Date(this.props.question.getIn(['question', 'question', 'createdMillis'], 0)).toDateString()}</p>
                    </div>
                    <div className="comments">
                        <CommentsListContainer comments={this.props.question.getIn(['question', 'comments'], List.of())} refresh={refresh} questionId={questionId} />
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
