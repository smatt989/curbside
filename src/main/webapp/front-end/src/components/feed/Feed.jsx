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
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { getQuestionFeed, getQuestionFeedSuccess, getQuestionFeedError, questionSearch, questionSearchSuccess, questionSearchError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import Question from './Question.jsx';
import FeedElement from './FeedElement.jsx';

class Feed extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      query: '',
      goToSearch: false
    };

    this.getNextFeed = this.getNextFeed.bind(this);
    this.handleOnScroll = this.handleOnScroll.bind(this);
    this.handleQueryUpdate = this.handleQueryUpdate.bind(this);
    this.goToSearch = this.goToSearch.bind(this);
    this.getFeedItems = this.getFeedItems.bind(this);
  }

  componentDidMount() {
    this.getFeedItems()
  }

  getFeedItems() {
    this.getNextFeed()
    window.addEventListener('scroll', this.handleOnScroll);
  }

  componentWillUnmount() {
    window.removeEventListener('scroll', this.handleOnScroll);
  }

  getNextFeed() {
     const feed = this.props.questionFeed.get('feed')

     const lastResponseSize = this.props.questionFeed.get('lastResponseSize')

     if((lastResponseSize == null ||  lastResponseSize > 0) && !this.state.loading){
        const page = this.props.questionFeed.get('currentPage')
        this.setState({loading: true})
        this.props.getQuestionFeed(() => this.setState({loading: false}))(page)
     }
  }

  handleOnScroll() {
    var scrolledToBottom = (window.innerHeight + window.scrollY) >= document.body.offsetHeight;

    if (scrolledToBottom) {
      this.getNextFeed();
    }
  }

  handleQueryUpdate(e) {
    this.setState({query: e.target.value})
  }

  goToSearch(){
    if(this.state.query.length > 1){
        this.setState({goToSearch: true})
    }
  }

  render() {

    var feed = this.props.questionFeed.get('feed')

    if(this.state.goToSearch){
        return <Redirect to={"/search/"+this.state.query} />
    }


    return (
        <FeedElement feed={feed} handleQueryUpdate={this.handleQueryUpdate} queryValue={this.state.query} goToSearch={this.goToSearch} tags={this.props.tagChoices.get('tags')}/>

    );
  }
}

const mapStateToProps = state => {
  return {
    questionFeed: state.get('questionFeed'),
    tagChoices: state.get('tagChoices')
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
