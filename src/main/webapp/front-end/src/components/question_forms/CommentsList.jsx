import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NewCommentContainer from './NewComment.jsx';
import Comment from './Comment.jsx';

class CommentsList extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {

    const refresh = this.props.refresh

    const comments = this.props.comments

    return (
      <div >
        <ListGroup componentClass="ul">
            {comments.map(c => <Comment comment={c} />)}
        </ListGroup>
        <div>
            <NewCommentContainer questionId={this.props.questionId} answerId={this.props.answerId} refresh={refresh} />
        </div>
      </div>

    );
  }
}

const mapStateToProps = state => {
  return {

  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {

  };
};

const CommentsListContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(CommentsList);

export default CommentsListContainer;
