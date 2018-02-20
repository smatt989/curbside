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


const QuestionTag = ({ tag, clickFunction, activeClassName, linkToQuestions }) => {

  function onClickFunction(e, id) {
    if(!linkToQuestions){
        e.preventDefault();
    }
    clickFunction(id);
  }

  var linkPath = linkToQuestions ? "/questions/tags/"+tag.get('id') : "#"

  return (
            <Link onClick={(e) => onClickFunction(e, tag.get('id'))} to={linkPath}><div className={"tag-choice "+activeClassName}>{tag.get('name')}</div></Link>
  );
};

export default QuestionTag;
