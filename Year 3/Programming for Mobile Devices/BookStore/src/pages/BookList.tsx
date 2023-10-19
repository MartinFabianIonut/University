import React, { useContext } from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonContent,
    IonDatetime,
    IonDatetimeButton,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonList,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar,
} from '@ionic/react';
import { add } from 'ionicons/icons';
import Book from './Book'; // Am schimbat denumirea la import
import { getLogger } from '../core';
import { BookContext } from './BookProvider';

const log = getLogger('BookList'); // Am schimbat denumirea logului

const BookList: React.FC<RouteComponentProps> = ({ history }) => {
    const { books, fetching, fetchingError } = useContext(BookContext); // Am schimbat la BookContext

    log('render');

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Book List</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching books" />
                {books && (
                    <IonList>
                        {books.map(({ id, title, author, publicationDate, isAvailable, price }) => (
                            <Book
                                key={id}
                                id={id}
                                title={title}
                                author={author}
                                publicationDate={publicationDate}
                                isAvailable={isAvailable}
                                price={price}
                                onEdit={() => history.push(`/book/${id}`)}
                            />
                        ))}
                    </IonList>
                )}
                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch books'}</div>
                )}
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/book')}>
                        <IonIcon icon={add} />
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default BookList;
