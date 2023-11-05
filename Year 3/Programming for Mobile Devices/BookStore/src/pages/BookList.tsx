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
    IonPage,
    IonToast
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
    const { books, fetching, fetchingError, savingError } = useContext(BookContext);
    const [showToast, setShowToast] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const handleToastClose = () => setShowToast(false);
    useEffect(() => {
        log('useEffect');
        if (savingError) {
            setToastMessage(savingError.message);
            setShowToast(true);
        }
    }, [savingError]);
    useEffect(() => {
        log('useEffect');
        if (fetchingError) {
            setToastMessage(fetchingError.message);
            setShowToast(true);
        }
    }, [fetchingError]);

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
                <IonToast
                    isOpen={showToast}
                    onDidDismiss={handleToastClose}
                    message={toastMessage}
                    duration={3000}
                    cssClass="custom-toast"
                    position="middle"
                />
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
