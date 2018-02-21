import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem,
  Grid,
  FormGroup,
  InputGroup,
  FormControl
} from 'react-bootstrap';
import { Link, Redirect } from 'react-router-dom';
import NavBar from '../NavBar.jsx';
import Question from './Question.jsx'
import QuestionTagBox from '../question_forms/QuestionTagBox.jsx';

const FeedElement = ({ feed, handleQueryUpdate, queryValue, goToSearch, tags, activeTags, tagClick }) => {

  const onEnter = (e) => {
    var code = (e.keyCode ? e.keyCode : e.which);
    if(code == 13) {
        goToSearch()
    }
  }

  var activeIds = activeTags ? activeTags : []

  var noItems = <div><h2>Oh no!  No questions yet :(</h2></div>
  var itemList = feed.size == 0 ? noItems : <ListGroup componentClass="ul" className="feed-ul">
                                                                            {feed.map(q => <Question key={q.getIn(['question', 'id'])} question={q} />)}
                                                                        </ListGroup>

  return (
                  <div >
                    <NavBar inverse={false} />
                    <Grid>
                        <QuestionTagBox tags={tags} clickFunction={tagClick} activeIds={activeIds} small={true} linkToQuestions={true} />
                        <h3 className="col-md-3">Questions:</h3>
                        <FormGroup className="col-md-5">
                          <InputGroup>
                            <FormControl onChange={handleQueryUpdate} onKeyDown={onEnter} type="text" placeholder="Search for questions..." value={queryValue} />
                            <InputGroup.Button>
                              <Button onClick={goToSearch}>Search</Button>
                            </InputGroup.Button>
                          </InputGroup>
                        </FormGroup>
                        <Link to="/questions/new"><Button className="col-md-2 col-md-push-2" bsStyle="success">New Question</Button></Link>
                        <div className="col-md-12">
                            {itemList}
                        </div>
                    </Grid>
                  </div>
  );
};

export default FeedElement;
