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


const PointsBox = ({ major, saveReview, answerId }) => {
  return (
            <div className="inline info-box text-xs-center">
                <Button onClick={() => saveReview(answerId, true)}>Upvote</Button>
                <h2>{major}</h2>
                <Button onClick={() => saveReview(answerId, false)}>Downvote</Button>
            </div>
  );
};

export default PointsBox;
