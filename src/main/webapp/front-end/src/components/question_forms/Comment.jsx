import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem,
  FormGroup,
  FormControl,
  Form,
  ControlLabel,
  Col
} from 'react-bootstrap';
import { Link, Redirect } from 'react-router-dom';


const Comment = ({ comment, deleteComment }) => {

  var deleteCommentLink = null

  if(comment.get('isCreator')) {
    deleteCommentLink = <Link onClick={() => deleteComment(comment.get('id'))} to="#">delete</Link>
  }

  return (
            <ListGroupItem className="comment-item">
                <div className="info-text">
                    <b>{comment.get('creatorName')}</b> on {new Date(comment.get('updatedMillis')).toDateString()}{' '}
                    {deleteCommentLink}
                </div>
                <p>{comment.get('text')}</p>
            </ListGroupItem>
  );
};

export default Comment;
