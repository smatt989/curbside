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
      page: 0,
      loading: false
    };

    this.getNextFeed = this.getNextFeed.bind(this);
    this.handleOnScroll = this.handleOnScroll.bind(this);
  }

  componentDidMount() {
     this.getNextFeed()
     window.addEventListener('scroll', this.handleOnScroll);
  }

  componentWillUnmount() {
    window.removeEventListener('scroll', this.handleOnScroll);
  }

  getNextFeed() {
     const feed = this.props.questionFeed.get('feed')

     if((feed.size == 0 || feed.last().size > 0) && !this.state.loading){
        const page = this.state.page
        this.setState({loading: true, page: page + 1})
        this.props.getQuestionFeed(() => this.setState({loading: false}))(page)
     }
  }

  handleOnScroll() {
    var scrolledToBottom = (window.innerHeight + window.scrollY) >= document.body.offsetHeight;

    if (scrolledToBottom) {
      this.getNextFeed();
    }
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
                    {this.props.questionFeed.get('feed').map(qs => qs.map(q => <Question question={q} />))}
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

  const getQuestionFeedWithCallback = (callback) => dispatchPattern(getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError, callback)

  return {
    getQuestionFeed: getQuestionFeedWithCallback
  };
};

const FeedContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Feed);

export default FeedContainer;
