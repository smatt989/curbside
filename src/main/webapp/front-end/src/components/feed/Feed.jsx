import React from 'react';
import {
  Button,
  ListGroup,
  ListGroupItem,
  Grid
} from 'react-bootstrap';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import Question from './Question.jsx'

class Feed extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      page: 0
    };
  }

  componentDidMount() {
    this.props.getQuestionFeed(this.state.page)
    this.setState({page: this.state.page + 1})
  }

  render() {


    return (
      <div >
        <NavBar inverse={false} />
        <Grid>
            <h3 className="col-md-6">Questions:</h3>
            <Link to="/questions/new"><Button className="col-md-2 col-md-push-4" bsStyle="success">New Question</Button></Link>
            <div className="col-md-12">
                <ListGroup componentClass="ul">
                    {this.props.questionFeed.get('feed').map(q => <Question question={q} />)}
                </ListGroup>
            </div>
        </Grid>
      </div>

    );
  }
}

const mapStateToProps = state => {
  return {
    questionFeed: state.get('getQuestionFeed')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getQuestionFeed: dispatchPattern(getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError)
  };
};

const FeedContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Feed);

export default FeedContainer;
