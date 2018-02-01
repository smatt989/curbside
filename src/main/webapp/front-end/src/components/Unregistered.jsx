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
import NavBar from './NavBar.jsx';

class Unregistered extends React.Component {

  render() {

    if(this.props.registered && !this.props.isLoading) {
        return <Redirect to={"/feed"}/>
    }

    return <div className="full-screen-page">
        <NavBar inverse={false} />
        <div className='container'>
          <div className="text-xs-center">
              <div className='row'>
                <div className='col-md-12'>
                  <h1>Thanks for signing up!</h1>
                  <h3>You'll get an email from resident@gmail.com when we've finished confirming that you're a doctor. Hold tight!</h3>
                </div>
              </div>
          </div>
        </div>
      </div>;
  }
}

const mapStateToProps = state => {
  return {
    registered: state.getIn(['getSelf', 'user', 'registered'], true),
    isLoading: state.getIn(['getSelf', 'loading'])
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {

  };
};

const UnregisteredContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Unregistered);

export default UnregisteredContainer;
