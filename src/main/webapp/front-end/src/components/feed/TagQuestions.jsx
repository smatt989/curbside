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
import { questionsByTag, questionsByTagSuccess, questionsByTagError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import Question from './Question.jsx';
import FeedElement from './FeedElement.jsx';

class TagQuestions extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      query: '',
      goToSearch: false,
      needsRefresh: true,
      tagId: null
    };

    this.handleQueryUpdate = this.handleQueryUpdate.bind(this);
    this.goToSearch = this.goToSearch.bind(this);
    this.getFeedItems = this.getFeedItems.bind(this);
    this.getTagId = this.getTagId.bind(this);
    this.tagClick = this.tagClick.bind(this);
  }

  componentDidMount() {
    this.getFeedItems()
  }

  componentDidUpdate() {
    this.getFeedItems()
  }

  getFeedItems() {
    if(this.state.needsRefresh) {
        const tagId = this.getTagId()
        this.setState({tagId: tagId})
        this.props.fetchQuestionsByTag(tagId)
        this.setState({needsRefresh: false});
    }
  }

  tagClick() {
    this.setState({needsRefresh: true})
  }

  handleQueryUpdate(e) {
    this.setState({query: e.target.value})
  }

  getTagId() {
    return parseInt(this.props.match.params.id)
  }

  goToSearch(){
    if(this.state.query.length > 1){
        this.setState({goToSearch: true})
    }
  }

  render() {
    var feed = this.props.questionsByTag.get('questions')

    if(this.state.goToSearch){
        return <Redirect to={"/search/"+this.state.query} />
    }


    return (
        <FeedElement feed={feed} handleQueryUpdate={this.handleQueryUpdate} queryValue={this.state.query} goToSearch={this.goToSearch} tags={this.props.tagChoices.get('tags')} activeTags={[this.getTagId()]} tagClick={this.tagClick} />

    );
  }
}

const mapStateToProps = state => {
  return {
    questionsByTag: state.get('questionsByTag'),
    tagChoices: state.get('tagChoices')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    fetchQuestionsByTag: dispatchPattern(questionsByTag, questionsByTagSuccess, questionsByTagError)
  };
};

const TagQuestionsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(TagQuestions);

export default TagQuestionsContainer;
