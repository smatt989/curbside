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

class SearchResults extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      query: '',
      needsRefresh: true
    };

    this.getSearchQuery = this.getSearchQuery.bind(this);
    this.handleQueryUpdate = this.handleQueryUpdate.bind(this);
    this.getFeedItems = this.getFeedItems.bind(this);
    this.goToSearch = this.goToSearch.bind(this);
  }

  getSearchQuery() {
    return this.props.match.params.query
  }

  componentDidMount() {
    this.getFeedItems()
  }

  getFeedItems() {
    if(this.state.needsRefresh) {
        this.setState({query: this.getSearchQuery()})
        this.props.questionSearch(this.getSearchQuery())
        this.setState({needsRefresh: false});
    }
  }

  componentDidUpdate() {
    this.getFeedItems()
  }

  handleQueryUpdate(e) {
    this.setState({query: e.target.value})
  }

  goToSearch() {
    var history = this.props.history;
    history.push('/search/'+this.state.query)
    this.setState({needsRefresh: true})
  }

  render() {

    var feed = this.props.searchFeed.get('results')


    return (
        <FeedElement feed={feed} handleQueryUpdate={this.handleQueryUpdate} queryValue={this.state.query} goToSearch={this.goToSearch}/>

    );
  }
}

const mapStateToProps = state => {
  return {
    searchFeed: state.get('questionSearch')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    questionSearch: dispatchPattern(questionSearch, questionSearchSuccess, questionSearchError)
  };
};

const SearchResultsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchResults);

export default SearchResultsContainer;
