import React, { useCallback, useContext, useEffect, useState } from 'react';
import {
    IonButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonInput,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar,
    IonLabel,
    IonCheckbox,
    IonDatetimeButton
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from './BookProps';
import { format } from 'date-fns';
import moment from 'moment';
const log = getLogger('BookEdit');

interface BookEditProps extends RouteComponentProps<{
    id?: string;
}> { }

const styles = {
    page: {
        backgroundColor: '#f8f8f8',
    },
    header: {
        backgroundColor: '#333',
        color: '#fff',
    },
    content: {
        padding: '20px',
    },
    input: {
        color: '#be1',
        borderRadius: '5px',
        marginBottom: '10px',
    },
    checkbox: {
        marginTop: '10px',
    },
    button: {
        backgroundColor: '#4caf50',
        color: '#fff',
    },
    errorMessage: {
        color: '#ff0000',
        marginTop: '10px',
    },
};

const BookEdit: React.FC<BookEditProps> = ({ history, match }) => {
    const { books, saving, savingError, saveBook: saveBook } = useContext(BookContext);
    const [book, setBook] = useState<BookProps | undefined>(undefined);
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [publicationDate, setPublicationDate] = useState<Date | undefined>(undefined);
    const [isAvailable, setIsAvailable] = useState(false);
    const [price, setPrice] = useState(0);

    useEffect(() => {
        log('useEffect');
        const routeId = match.params.id || '';
        const foundBook = books?.find(book => book.id === routeId);
        setBook(foundBook);

        if (foundBook) {
            setTitle(foundBook.title);
            setAuthor(foundBook.author);
            setPublicationDate(foundBook.publicationDate);
            setIsAvailable(foundBook.isAvailable);
            setPrice(foundBook.price);
        }
    }, [match.params.id, books]);

    const handleSave = useCallback(() => {
        const editedBook: BookProps = {
            id: book?.id,
            title,
            author,
            publicationDate: publicationDate || new Date(),
            isAvailable,
            price
        };

        saveBook && saveBook(editedBook).then(() => history.goBack());
    }, [book, saveBook, title, author, publicationDate, isAvailable, price, history]);

    log('render');
    return (
        <IonPage>
            <IonHeader class='header'>
                <IonToolbar>
                    <IonTitle>Edit</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>Save</IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <div style={{ display: 'flex', alignItems: "center" }}>
                    <IonLabel style={{ marginRight: '10px' }}>Title:</IonLabel>
                    <IonInput style={styles.input} value={title} onIonChange={e => setTitle(e.detail.value || '')} />
                </div>

                <IonLabel>Author:</IonLabel>
                <IonInput style={styles.input} value={author} onIonChange={e => setAuthor(e.detail.value || '')} />

                <IonLabel>Publication Date:</IonLabel>
                <IonInput class="input"
                    value={publicationDate ? format(new Date(publicationDate), 'dd/MM/yyyy') : ''}
                    onIonChange={e => {
                        const inputDate = moment(e.detail.value, 'dd/MM/yyyy').toDate();
                        setPublicationDate(inputDate || '');
                    }}
                />

                <IonLabel>Available:</IonLabel>
                <IonCheckbox class="checkbox" checked={isAvailable} onIonChange={e => setIsAvailable(e.detail.checked)} />
                <br />
                <IonLabel>Price:</IonLabel>
                <IonInput class="input" value={price.toString()} onIonChange={(e) => setPrice(parseInt(e.detail.value || '0'))} />

                <IonLoading isOpen={saving} />
                {savingError && (
                    <div>{savingError.message || 'Failed to save book'}</div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default BookEdit;