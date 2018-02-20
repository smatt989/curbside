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
import QuestionTag from './QuestionTag.jsx';


const QuestionTagBox = ({ tags, clickFunction, activeIds, small, linkToQuestions }) => {

  var smallerClass = small ? "smaller" : null

  return (<div className={"tag-container "+smallerClass}>
    {tags.map(function(tag){
         var activeClassName = activeIds.includes(tag.get('id')) ? 'selected' : 'unselected'
         return <QuestionTag key={tag.get('id')} tag={tag} clickFunction={clickFunction} activeClassName={activeClassName} linkToQuestions={linkToQuestions} />
        })
    }
  </div>)
};

export default QuestionTagBox;
