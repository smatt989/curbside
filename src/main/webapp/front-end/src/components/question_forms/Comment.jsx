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


const Comment = ({ comment }) => {
  return (
            <ListGroupItem className="comment-item">
                <div className="info-text"><b>{comment.get('creatorName')}</b> on {new Date(comment.get('updatedMillis')).toDateString()}</div>
                <p>{comment.get('text')}</p>
            </ListGroupItem>
  );
};

export default Comment;
