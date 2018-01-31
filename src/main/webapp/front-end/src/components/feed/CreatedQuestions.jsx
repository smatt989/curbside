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
import { questionsCreated, questionsCreatedSuccess, questionsCreatedError } from '../../actions.js';
import { tryLogin, dispatchPattern } from '../../utilities.js';
import NavBar from '../NavBar.jsx';
import Question from './Question.jsx';
import FeedElement from './FeedElement.jsx';

class CreatedQuestions extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      query: '',
      goToSearch: false
    };

    this.handleQueryUpdate = this.handleQueryUpdate.bind(this);
    this.goToSearch = this.goToSearch.bind(this);
    this.getFeedItems = this.getFeedItems.bind(this);
  }

  componentDidMount() {
    this.getFeedItems()
  }

  getFeedItems() {
    this.props.createdQuestions()
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

    var feed = this.props.myQuestions.get('questions')

    if(this.state.goToSearch){
        return <Redirect to={"/search/"+this.state.query} />
    }


    return (
        <FeedElement feed={feed} handleQueryUpdate={this.handleQueryUpdate} queryValue={this.state.query} goToSearch={this.goToSearch}/>

    );
  }
}

const mapStateToProps = state => {
  return {
    myQuestions: state.get('questionsCreated')
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    createdQuestions: dispatchPattern(questionsCreated, questionsCreatedSuccess, questionsCreatedError)
  };
};

const CreatedQuestionsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatedQuestions);

export default CreatedQuestionsContainer;
