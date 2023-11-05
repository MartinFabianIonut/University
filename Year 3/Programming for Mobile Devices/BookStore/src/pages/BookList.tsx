import React, { useContext, useState, useEffect } from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonList,
    IonLoading,
    IonPage
} from '@ionic/react';
import { add } from 'ionicons/icons';
import Book from './Book';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import CustomToolbar from '../components/CustomToolbar';


const log = getLogger('BookList');

const styles = {
    title: {
        fontFamily: 'Geneva, sans-serif',
        fontSize: '2rem',
        fontWeight: 'bold',
    }
};

const BookList: React.FC<RouteComponentProps> = ({ history }) => {
    const { books, fetching, fetchingError } = useContext(BookContext);

    log('render ', 'yes/no: ', fetching, ' ' + JSON.stringify(books));

    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Book List" titleStyle="title" />
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
                                onEdit={boodId => history.push(`/book/${boodId}`)}
                            />
                        ))}
                    </IonList>
                )}
                {fetchingError && <div>{fetchingError.message || 'Failed to fetch books'}</div>}
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
