import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError, deleteComment, deleteCommentSuccess, deleteCommentError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NewCommentContainer from './NewComment.jsx';
import Comment from './Comment.jsx';

class CommentsList extends React.Component {
  constructor(props) {
    super(props);

    this.state = {addComment: false}
  }

  render() {

    const refresh = this.props.refresh

    const comments = this.props.comments

    var newComment = <Link onClick={() => this.setState({addComment: true})} to="#">Add comment</Link>

    const deleteComment = this.props.deleteComment(refresh)

    if(this.state.addComment){
        newComment = <NewCommentContainer questionId={this.props.questionId} answerId={this.props.answerId} refresh={refresh} />
    }

    return (
      <div >
        <ListGroup componentClass="ul">
            {comments.map(c => <Comment comment={c} key={c.get('id')} deleteComment={deleteComment} />)}
        </ListGroup>
        <div>
            {newComment}
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

  const deleteCommentWithCallback = (callback) => dispatchPattern(deleteComment, deleteCommentSuccess, deleteCommentError, callback)

  return {
    deleteComment: deleteCommentWithCallback
  };
};

const CommentsListContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(CommentsList);

export default CommentsListContainer;
