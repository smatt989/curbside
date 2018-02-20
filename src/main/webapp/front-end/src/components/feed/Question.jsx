import React from 'react';
import { List } from 'immutable';
import {
  Button,
  ButtonGroup,
  ListGroupItem,
  Col,
  Row
} from 'react-bootstrap';
import { Link, Redirect } from 'react-router-dom';
import InfoBox from './InfoBox.jsx';
import QuestionTagBox from '../question_forms/QuestionTagBox.jsx';

const Question = ({ question }) => {

  return (
    <Link to={"/question/"+question.getIn(['question', 'id'])}>
        <ListGroupItem className="question-feed-item">
            <Row>
                <Col md={2}>
                    <div className="question-feed-stats">
                        <InfoBox major={question.get('viewCount', 0)} minor={"views"} />
                        <InfoBox major={question.get('answers', List.of()).size} minor={"answers"} />
                    </div>
                </Col>
                <Col md={10}>
                    <div className="inline content-box">
                        <h3>{question.getIn(['question', 'title'])}</h3>
                        <QuestionTagBox tags={question.getIn(['question', 'tags'])} clickFunction={function(){}} activeIds={[]} small={true} linkToQuestions={true} />
                        <p>Last update: {new Date(question.getIn(['question', 'updatedMillis'])).toDateString()}</p>
                    </div>
                </Col>
            </Row>
        </ListGroupItem>
    </Link>
  );
};

export default Question;
