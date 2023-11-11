import React, { useCallback, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../core';
import { login as loginApi, signup as signupApi } from '../api/authApi';
import { Preferences } from '@capacitor/preferences';

const log = getLogger('AuthProvider');

type LoginFn = (username?: string, password?: string) => void;
type SignupFn = (username?: string, password?: string) => void;
type LogoutFn = () => void;

export interface AuthState {
  authenticationError: Error | null;
  isAuthenticated: boolean;
  isAuthenticating: boolean;
  login?: LoginFn;
  signup?: SignupFn;
  pendingAuthentication?: boolean;
  pendingSignUp?: boolean; // Added
  username?: string;
  password?: string;
  token: string;
  logout?: LogoutFn;
}

const initialState: AuthState = {
  isAuthenticated: false,
  isAuthenticating: false,
  authenticationError: null,
  pendingAuthentication: false,
  pendingSignUp: false, // Added
  token: '',
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
  children: PropTypes.ReactNodeLike,
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, setState] = useState<AuthState>(initialState);
  const { isAuthenticated, isAuthenticating, authenticationError, pendingAuthentication, pendingSignUp, token } = state;
  const login = useCallback<LoginFn>(loginCallback, []);
  const signup = useCallback<SignupFn>(signupCallback, []);
  const logout = useCallback<LogoutFn>(logoutCallback, []);

  useEffect(() => {
    loadUserFromPreferences();
    authenticationEffect();
  }, [pendingAuthentication, pendingSignUp]);
  const value = { isAuthenticated, login, signup, isAuthenticating, authenticationError, token, pendingSignUp, logout };

  log('render');
  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );

  function loginCallback(username?: string, password?: string): void {
    log('login');
    setState({
      ...state,
      pendingAuthentication: true,
      pendingSignUp: false,
      username,
      password
    });
  }

  function signupCallback(username?: string, password?: string): void {
    log('signup');
    setState({
      ...state,
      pendingSignUp: true,
      pendingAuthentication: false,
      username,
      password
    });
  }

  function logoutCallback() {
    log('logout');
    Preferences.clear();
    setState({
      ...initialState
    });
  }

  async function loadUserFromPreferences() {
    try {
      const userString = await Preferences.get({ key: 'user' });
      if (userString && userString.value) {
        const user = JSON.parse(userString.value);
        setState({
          ...state,
          isAuthenticated: true,
          token: user.token,
        });
      }
    } catch (error) {
      log('Error loading user from Preferences:', error);
    }
  }

  function authenticationEffect() {
    let canceled = false;
    authenticate();
    return () => {
      canceled = true;
    }

    async function authenticate() {
      if (!pendingAuthentication && !pendingSignUp) {
        log('now we are waiting for user to press login or signup');
        return;
      }
      try {
        log('authenticate...');
        setState({
          ...state,
          isAuthenticating: true,
        });
        const { username, password } = state;
        let result;
        if (pendingSignUp) {
          result = await signupApi(username, password);
        } else {
          result = await loginApi(username, password);
        }
        const { token } = result;
        if (canceled) {
          return;
        }
        log('authenticate succeeded');
        setState({
          ...state,
          token,
          pendingAuthentication: false,
          pendingSignUp: false,
          isAuthenticated: true,
          isAuthenticating: false,
        });
        Preferences.set({
          key: 'user',
          value: JSON.stringify({ token }),
        });
      } catch (error) {
        if (canceled) {
          return;
        }
        log('authenticate failed');
        setState({
          ...state,
          authenticationError: error as Error,
          pendingAuthentication: false,
          pendingSignUp: false,
          isAuthenticating: false,
        });
      }
    }
  }
};
