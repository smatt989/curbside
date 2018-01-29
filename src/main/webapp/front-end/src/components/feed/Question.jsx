import React from 'react';
import { List } from 'immutable';
import {
  Button,
  ButtonGroup,
  ListGroupItem
} from 'react-bootstrap';
import { Link, Redirect } from 'react-router-dom';
import InfoBox from './InfoBox.jsx';

const Question = ({ question }) => {

  return (
    <Link to={"/question/"+question.getIn(['question', 'id'])}>
        <ListGroupItem className="question-feed-item">
            <InfoBox major={question.get('viewCount', 0)} minor={"views"} />
            <InfoBox major={question.get('answers', List.of()).size} minor={"answers"} />
            <div className="inline content-box">
                <h3>{question.getIn(['question', 'title'])}</h3>
                <p>Last update: {new Date(question.getIn(['question', 'updatedMillis'])).toDateString()}</p>
            </div>
        </ListGroupItem>
    </Link>
  );
};

export default Question;
