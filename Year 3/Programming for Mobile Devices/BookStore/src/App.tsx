import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
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
import { BookList, BookEdit } from './pages';
import { BookProvider } from './pages/BookProvider';

setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <BookProvider>
      <IonReactRouter>
        <IonRouterOutlet>
          <Route path="/books" component={BookList} exact={true} />
          <Route path="/book" component={BookEdit} exact={true} />
          <Route path="/book/:id" component={BookEdit} exact={true} />
          <Route exact path="/" render={() => <Redirect to="/books" />} />
        </IonRouterOutlet>
      </IonReactRouter>
    </BookProvider>
  </IonApp>
);

export default App;
