import React from 'react';
import { connect } from 'react-redux';
import { Route, Redirect } from 'react-router-dom';

const PrivateRoute = ({ component: Component, isAuthenticated, isRegistered, path }) => {

  return <Route path={path} render={props => (
    isAuthenticated && isRegistered ? (
      <Component {...props}/>
    ) : isAuthenticated && !isRegistered ? (
      <Redirect to={'/unregistered'} />
    ): (
      <Redirect to={{
        pathname: '/login',
        state: { from: props.location }
      }}/>
    )
  )}/>;
};

const mapStateToProps = state => {
  return {
    isAuthenticated: state.getIn(['login', 'session']) != null,
    isRegistered: state.getIn(['getSelf', 'user', 'registered'], true)
  };
};

const PrivateRouteContainer = connect(
  mapStateToProps
)(PrivateRoute);

export default PrivateRouteContainer;
