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
import PrivateRouteContainer from './PrivateRoute.jsx';
import FeedContainer from './feed/Feed.jsx';
import NewQuestionContainer from './question_forms/NewQuestion.jsx';
import FullQuestionContainer from './question_forms/FullQuestion.jsx';
import EditAnswerContainer from './question_forms/EditAnswer.jsx';
import Err from './Error.jsx';

export default class App extends React.Component {
  render() {
    return <Router>
      <div>
        <Switch>
          <Route exact path="/" component={Home}/>
          <Route exact path="/login" component={LoginContainer}/>
          <Route exact path="/register" component={RegisterContainer}/>
          <Route exact path="/recover" component={LoginContainer}/>
          <Route exact path="/feed" component={FeedContainer}/>
          <Route exact path="/questions/new" component={NewQuestionContainer}/>
          <PrivateRouteContainer exact path="/question/:id" component={FullQuestionContainer}/>
          <PrivateRouteContainer exact path="/question/:id/edit" component={NewQuestionContainer}/>
          <PrivateRouteContainer exact path="/question/:qid/answers/:aid" component={EditAnswerContainer}/>
          <Route component={Err}/>
        </Switch>
      </div>
    </Router>;
  }
}
