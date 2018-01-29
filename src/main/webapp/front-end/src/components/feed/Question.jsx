import React from 'react';
import {
  Button,
  ButtonGroup,
  ListGroupItem
} from 'react-bootstrap';
import { Link, Redirect } from 'react-router-dom';
import InfoBox from './InfoBox.jsx';

const Question = ({  }) => {

  return (
    <Link to={"/hello"}>
        <ListGroupItem className="question-feed-item">
            <InfoBox major={4} minor={"views"} />
            <InfoBox major={2} minor={"answers"} />
            <div className="inline content-box">
                <h3>This is a very serious question!</h3>
                <p>Last update: 10:26pm Fri, October 16, 1988</p>
            </div>
        </ListGroupItem>
    </Link>
  );
};

export default Question;
