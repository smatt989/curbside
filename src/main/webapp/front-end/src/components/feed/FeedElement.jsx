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

const FeedElement = ({ feed, handleQueryUpdate, queryValue, goToSearch }) => {
  return (
                  <div >
                    <NavBar inverse={false} />
                    <Grid>
                        <h3 className="col-md-3">Questions:</h3>
                        <FormGroup className="col-md-5">
                          <InputGroup>
                            <FormControl onChange={handleQueryUpdate} type="text" placeholder="Search for questions..." value={queryValue} />
                            <InputGroup.Button>
                              <Button onClick={goToSearch}>Search</Button>
                            </InputGroup.Button>
                          </InputGroup>
                        </FormGroup>
                        <Link to="/questions/new"><Button className="col-md-2 col-md-push-2" bsStyle="success">New Question</Button></Link>
                        <div className="col-md-12">
                            <ListGroup componentClass="ul" className="feed-ul">
                                {feed.map(q => <Question key={q.getIn(['question', 'id'])} question={q} />)}
                            </ListGroup>
                        </div>
                    </Grid>
                  </div>
  );
};

export default FeedElement;
