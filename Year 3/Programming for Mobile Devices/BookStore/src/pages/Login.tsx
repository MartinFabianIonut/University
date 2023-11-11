import React, { useCallback, useContext, useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router';
import { IonButton, IonContent, IonHeader, IonInput, IonLoading, IonPage, IonTitle, IonToolbar, IonToast } from '@ionic/react';
import { AuthContext } from '../providers/AuthProvider';
import { getLogger } from '../core';
import { useIonToast } from '../hooks/useIonToast';

const log = getLogger('Login');

interface LoginState {
  username?: string;
  password?: string;
}

const Login: React.FC<RouteComponentProps> = ({ history }) => {
  const { isAuthenticated, isAuthenticating, login, signup, authenticationError } = useContext(AuthContext);
  const [state, setState] = useState<LoginState>({
    username: '',
    password: ''
  });
  const { username, password } = state;
  const { showToast, ToastComponent, getErrorMessage } = useIonToast();

  const isLoginDisabled = !username || !password;
  const isSignupDisabled = isLoginDisabled || isAuthenticating;

  const handlePasswwordChange = useCallback((e: any) => setState({
    ...state,
    password: e.detail.value || ''
  }), [state]);
  const handleUsernameChange = useCallback((e: any) => setState({
    ...state,
    username: e.detail.value || ''
  }), [state]);

  const handleLogin = useCallback(() => {
    if (!isLoginDisabled) {
      log('handleLogin...');
      login?.(username, password);
    }
  }, [username, password, isLoginDisabled, login]);

  const handleSignup = () => {
    if (!isSignupDisabled) {
      log('handleSignup...');
      signup?.(username, password);
    }
  };

  log('render');
  useEffect(() => {
    if (isAuthenticated) {
      log('redirecting to home');
      history.push('/');
    }
  }, [isAuthenticated]);

  useEffect(() => {
    if (authenticationError) {
      showToast({
        message: getErrorMessage(authenticationError) || 'Failed to authenticate',
      });
    }
  }, [authenticationError, showToast]);

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle
            className="custom-title"
          >Login</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonInput
          style={{ marginTop: '20px' }}
          className="custom-input"
          placeholder="Username"
          value={username}
          autofocus={true}
          onIonChange={handleUsernameChange} />
        <IonInput
          className="custom-input"
          placeholder="Password"
          value={password}
          onIonChange={handlePasswwordChange} />
        <IonLoading isOpen={isAuthenticating} />
        <IonButton
          className="custom-button"
          disabled={isLoginDisabled}
          onClick={handleLogin}>
          Login
        </IonButton>
        <IonButton className="custom-button" disabled={isSignupDisabled} onClick={handleSignup}>Signup</IonButton>
        {ToastComponent}
      </IonContent>
    </IonPage>
  );
};

export default Login;