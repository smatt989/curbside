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
  Col,
  Clearfix
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { saveReview, saveReviewSuccess, saveReviewError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import PointsBox from './PointsBox.jsx';
import CommentsListContainer from './CommentsList.jsx';
import AnswersListContainer from './AnswersList.jsx';

class FullAnswer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      page: 0
    };
  }

  render() {

    const answer = this.props.answer

    const answerId = answer.getIn(['answer', 'id'])

    const refresh = this.props.refresh

    return (
        <div>
        <ListGroupItem>

                <div className="col-md-1">
                    <PointsBox major={answer.get('score')} minor={"views"} saveReview={this.props.saveReview} answerId={answerId} />
                </div>
                <div className="col-md-11">
                    <div className="answer-body">
                        <p>
                            {answer.getIn(['answer', 'text'])}
                        </p>
                    </div>
                    <div className="action-box">
                        <Link to={"/question/"+this.props.questionId+"/answers/"+answerId}>edit</Link>
                    </div>
                    <div className="poster-box">
                        <p>Answered by <b>matt</b> on {new Date(answer.getIn(['answer', 'updatedMillis'])).toDateString()}</p>
                    </div>
                    <div className="comments">
                        <CommentsListContainer comments={answer.get('comments', List.of())} refresh={refresh} answerId={answerId} />
                    </div>
                </div>
                <Clearfix />
        </ListGroupItem>
        </div>
    );
  }
}

const mapStateToProps = state => {
  return {
    questionFeed: state.get('getQuestionFeed')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {

  return {
    saveReview: dispatchPattern(saveReview, saveReviewSuccess, saveReviewError, ownProps.refresh)
  };
};

const FullAnswerContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(FullAnswer);

export default FullAnswerContainer;