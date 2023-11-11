import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonIcon, IonLabel, IonRouterOutlet, IonTab, IonTabBar, IonTabButton, IonTabs, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import { BookList, BookEdit, BookSearch, BookFilter, Login } from './pages';
import { AuthProvider, BookProvider, PrivateRoute } from './providers';
import { book, filter, searchCircle } from 'ionicons/icons';

setupIonicReact();

const App: React.FC = () => (

  <IonApp>
    <IonReactRouter>
      <IonTabs>
        <IonRouterOutlet>
          <AuthProvider>
            <Route path="/login" component={Login} exact={true} />
            <BookProvider>
              <PrivateRoute path="/books" component={BookList} exact={true} />
              <PrivateRoute path="/book" component={BookEdit} exact={true} />
              <PrivateRoute path="/book/:id" component={BookEdit} exact={true} />
              <PrivateRoute path="/search" component={BookSearch} exact={true} />
              <PrivateRoute path="/filter" component={BookFilter} exact={true} />
            </BookProvider>
            <Route exact path="/" render={() => <Redirect to="/books" />} />
          </AuthProvider>
        </IonRouterOutlet>
        <IonTabBar slot="bottom">
          <IonTabButton tab="books" href="/books">
            <IonIcon icon={book} />
            <IonLabel>Books</IonLabel>
          </IonTabButton>
          <IonTabButton tab="search" href="/search">
            <IonIcon icon={searchCircle} />
            <IonLabel>Search</IonLabel>
          </IonTabButton>
          <IonTabButton tab="filter" href="/filter">
            <IonIcon icon={filter} />
            <IonLabel>Filter</IonLabel>
          </IonTabButton>
        </IonTabBar>
      </IonTabs>
    </IonReactRouter>
  </IonApp>
);

export default App;
