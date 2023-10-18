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
    IonDatetime
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from './BookProps';
import { format } from 'date-fns';
const log = getLogger('BookEdit');

interface BookEditProps extends RouteComponentProps<{
    id?: string;
}> { }

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
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>Save</IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLabel>Title:</IonLabel>
                <IonInput value={title} onIonChange={e => setTitle(e.detail.value || '')} />

                <IonLabel>Author:</IonLabel>
                <IonInput value={author} onIonChange={e => setAuthor(e.detail.value || '')} />

                <IonLabel>Publication Date:</IonLabel>
                <IonDatetime
                    locale="ro-RO"
                    value={publicationDate ? format(new Date(publicationDate), 'YYYY-MM-DD') : ''}
                    onIonChange={(e) => {
                        const value = e.detail.value;
                        if (typeof value === 'string' || typeof value === 'number') {
                            setPublicationDate(new Date(value));
                        }
                    }}
                />


                <IonLabel>Available:</IonLabel>
                <IonCheckbox checked={isAvailable} onIonChange={e => setIsAvailable(e.detail.checked)} />

                <IonLabel>Price:</IonLabel>
                <IonInput value={price.toString()} onIonChange={(e) => setPrice(parseInt(e.detail.value || '0'))} />



                <IonLoading isOpen={saving} />
                {savingError && (
                    <div>{savingError.message || 'Failed to save book'}</div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default BookEdit;