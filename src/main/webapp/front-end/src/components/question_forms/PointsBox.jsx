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
import { Link } from 'react-router-dom';


const PointsBox = ({ major, saveReview, answerId }) => {
  return (
            <div className="inline info-box text-xs-center">
                <Link to="#"><div className="triangle-up" onClick={() => saveReview(answerId, true)}></div></Link>
                <h2>{major}</h2>
                <Link to="#"><div className="triangle-down" onClick={() => saveReview(answerId, false)}></div></Link>
            </div>
  );
};

export default PointsBox;
