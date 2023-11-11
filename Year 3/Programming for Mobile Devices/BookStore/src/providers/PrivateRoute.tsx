import React, { useContext, useState, useEffect } from 'react';
import { Redirect, Route } from 'react-router-dom';
import { AuthContext, AuthState } from './AuthProvider';
import { getLogger } from '../core';

const log = getLogger('PrivateRoute');

export interface PrivateRouteProps {
  component: any;
  path: string;
  exact?: boolean;
}

export const PrivateRoute: React.FC<PrivateRouteProps> = ({ component: Component, ...rest }) => {
  const { isAuthenticated } = useContext<AuthState>(AuthContext);

  log('render, isAuthenticated', isAuthenticated);
  return (
    <Route {...rest} render={props => {
      if (isAuthenticated) {
        return <Component {...props} />;
      }
      return <Redirect to={{ pathname: '/login' }} />
    }} />
  );
}
