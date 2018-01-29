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
import NewAnswerContainer from './NewAnswer.jsx';

class EditAnswer extends React.Component {
  constructor(props) {
    super(props);

    this.getQuestionId = this.getQuestionId.bind(this)
    this.getAnswerId = this.getAnswerId.bind(this)
  }

  getQuestionId() {
    return this.props.match.params.qid
  }

  getAnswerId() {
    return this.props.match.params.aid
  }

  componentDidMount() {
    const questionId = this.getQuestionId()
    this.props.getQuestion(questionId)
  }

  render() {
    const questionId = this.getQuestionId()
    const answerId = this.getAnswerId()

    const refresh = () => this.props.getQuestion(questionId)

    const answer = this.props.question.getIn(['question', 'answers'], List.of()).find(a => a.getIn(['answer', 'id']) == answerId)

    var answerBox = null

    if(answer){
        answerBox = <NewAnswerContainer refresh={refresh} questionId={questionId} answer={answer} />
    }

    console.log(answer)

    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            <h3 className="m-b-3">{this.props.question.getIn(['question', 'question', 'title'], '')}</h3>
            <div>
                <div className="col-md-12">
                    <div className="question-body">
                        <p>
                            {this.props.question.getIn(['question', 'question', 'text'], '')}
                        </p>
                    </div>
                </div>
            </div>
            <div>
                {answerBox}
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

const EditAnswerContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(EditAnswer);

export default EditAnswerContainer;
