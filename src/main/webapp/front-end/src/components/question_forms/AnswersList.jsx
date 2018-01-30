import React from 'react';
import { List } from 'immutable';
import {
  Button,
  ListGroup,
  ListGroupItem
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NewAnswerContainer from './NewAnswer.jsx';
import FullAnswerContainer from './FullAnswer.jsx';

class AnswersList extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {

    const refresh = this.props.refresh

    return (
      <div >
        <ListGroup componentClass="ul">
            {this.props.answers.map(a => <FullAnswerContainer key={a.getIn(['answer', 'id'])} refresh={refresh} questionId={this.props.questionId} answer={a} />)}
        </ListGroup>
        <div>
            <NewAnswerContainer refresh={refresh} questionId={this.props.questionId}/>
        </div>
      </div>

    );
  }
}

const mapStateToProps = state => {
  return {
    answers: state.getIn(['getQuestion', 'question', 'answers'], List.of())
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getQuestionFeed: dispatchPattern(getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError)
  };
};

const AnswersListContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(AnswersList);

export default AnswersListContainer;
