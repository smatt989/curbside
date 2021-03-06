import '../styles/app.less';
import React from 'react';
import {
  HashRouter as Router,
  Route,
  Switch
} from 'react-router-dom';
import { Grid, Row, Col } from 'react-bootstrap';
import NavBarContainer from './NavBar.jsx';
import Home from './Home.jsx';
import LoginContainer from './account_forms/Login.jsx';
import RegisterContainer from './account_forms/Register.jsx';
import {PrivateRouteContainer, HomeRouteContainer} from './PrivateRoute.jsx';
import FeedContainer from './feed/Feed.jsx';
import NewQuestionContainer from './question_forms/NewQuestion.jsx';
import FullQuestionContainer from './question_forms/FullQuestion.jsx';
import EditAnswerContainer from './question_forms/EditAnswer.jsx';
import SearchResultsContainer from './feed/SearchResults.jsx';
import CreatedQuestionsContainer from './feed/CreatedQuestions.jsx';
import TagQuestionsContainer from './feed/TagQuestions.jsx';
import Unregistered from './Unregistered.jsx';
import Err from './Error.jsx';

export default class App extends React.Component {
  render() {
    return <Router>
      <div>
        <Switch>
          <HomeRouteContainer exact path="/" component={Home}/>
          <Route exact path="/login" component={LoginContainer}/>
          <Route exact path="/register" component={RegisterContainer}/>
          <Route exact path="/recover" component={LoginContainer}/>
          <PrivateRouteContainer exact path="/feed" component={FeedContainer}/>
          <Route exact path="/unregistered" component={Unregistered}/>
          <PrivateRouteContainer path="/search/:query" component={SearchResultsContainer}/>
          <PrivateRouteContainer path="/questions/created" component={CreatedQuestionsContainer}/>
          <PrivateRouteContainer path="/questions/tags/:id" component={TagQuestionsContainer}/>
          <PrivateRouteContainer path="/questions/new" component={NewQuestionContainer}/>
          <PrivateRouteContainer path="/question/:id/edit" component={NewQuestionContainer}/>
          <PrivateRouteContainer path="/question/:qid/answers/:aid" component={EditAnswerContainer}/>
          <PrivateRouteContainer path="/question/:id" component={FullQuestionContainer}/>
          <Route component={Err}/>
        </Switch>
      </div>
    </Router>;
  }
}
