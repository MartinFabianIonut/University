import React, { useCallback, useContext, useEffect, useState } from 'react';
import {
    IonButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonInput,
    IonLabel,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar,
    IonCheckbox,
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from './BookProps';
import { format } from 'date-fns';
import moment from 'moment';
import { render } from '@testing-library/react';

const log = getLogger('BookEdit');

interface BookEditProps extends RouteComponentProps<{ id?: string }> { }

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
    inputContainer: {
        marginBottom: '10px',
        display: 'flex',
        alignItems: 'center',
        boxShadow: '0 4px 8px rgba(22, 14, 143, 0.507)',
    },
    label: {
        marginLeft: '20px',
        marginRight: '10px',
        minWidth: '120px',
        color: 'var(--ion-color-tertiary)',
        fontWeight: 'bold'
    },
    input: {
        flex: 1,
        borderRadius: '5px',
        color: 'var(--ion-color-tertiary-tint)',
    },
    checkbox: {
        marginTop: '20px',
        marginBottom: '20px'
    },
    button: {
        backgroundColor: '#4caf50',
        color: '#fff',
        borderRadius: '15px',
    },
    errorMessage: {
        color: '#ff0000',
        marginTop: '10px',
    },
    title: {
        fontFamily: 'Geneva, sans-serif',
        fontSize: '2rem',
        fontWeight: 'bold',
    }
};

function parseDDMMYYYY(dateString: string) {
    const [day, month, year] = dateString.split('/').map(Number);

    // Validate the components
    if (isNaN(day) || isNaN(month) || isNaN(year)) {
        log('Invalid date components');
        return null;
    }

    // Months are zero-based, so subtract 1 from the month
    const adjustedMonth = month - 1;

    // Create the Date object
    const parsedDate = new Date(year, adjustedMonth, day);

    if (
        parsedDate.getDate() !== day ||
        parsedDate.getMonth() !== adjustedMonth ||
        parsedDate.getFullYear() !== year
    ) {
        console.error('Invalid date');
        return null;
    }

    // Validate the Date object
    if (isNaN(parsedDate.getTime())) {
        log('Invalid date');
        return null;
    }

    return parsedDate;
}

const BookEdit: React.FC<BookEditProps> = ({ history, match }) => {
    const { books, saving, savingError, saveBook: saveBook } = useContext(BookContext);
    const [book, setBook] = useState<BookProps | undefined>(undefined);
    const [title, setTitle] = useState('Type a title');
    const [author, setAuthor] = useState('Type an author');
    const [publicationDate, setPublicationDate] = useState<Date | undefined>(undefined);
    const [isAvailable, setIsAvailable] = useState(false);
    const [price, setPrice] = useState(0);

    useEffect(() => {
        log('useEffect - Fetching book details');
        const routeId = match.params.id || '';
        const foundBook = books?.find((book) => book.id === routeId);
        setBook(foundBook);

        if (foundBook) {
            log('useEffect - Setting book details');
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
            price,
        };

        log('handleSave - Saving edited book');
        saveBook && saveBook(editedBook).then(() => {
            log('handleSave - Book saved successfully. Navigating back.');
            history.goBack();
        });
    }, [book, saveBook, title, author, publicationDate, isAvailable, price, history]);

    log('render ' + title);
    return (
        <IonPage style={styles.page} >
            <IonHeader style={styles.header}>
                <IonToolbar>
                    <IonTitle style={styles.title} color="primary">Edit</IonTitle>
                    <IonButtons slot="end">
                        <IonButton style={styles.button} onClick={handleSave}>
                            Save
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent style={styles.content}>
                <div style={styles.inputContainer}>
                    <IonLabel style={styles.label}>Title:</IonLabel>
                    <IonInput style={styles.input} value={title} onIonChange={(e) => setTitle(e.detail.value || '')} />
                </div>

                <div style={styles.inputContainer}>
                    <IonLabel style={styles.label}>Author:</IonLabel>
                    <IonInput style={styles.input} value={author} onIonChange={(e) => setAuthor(e.detail.value || '')} />
                </div>

                <div style={styles.inputContainer}>
                    <IonLabel style={styles.label}>Publication Date:</IonLabel>
                    <IonInput
                        class="input"
                        style={styles.input}
                        value={publicationDate ? format(new Date(publicationDate), 'dd/MM/yyyy') : ''}
                        onIonChange={(e) => {
                            const inputDate = parseDDMMYYYY(e.detail.value || '');
                            if (inputDate !== null) {
                                setPublicationDate(inputDate);
                            }
                            else {
                                // If the date is invalid, change the input value to the previous valid date
                                e.detail.value = publicationDate ? format(new Date(publicationDate), 'dd/MM/yyyy') : '';
                            }
                        }}
                    />
                </div>

                <div style={styles.inputContainer}>
                    <IonLabel style={styles.label}>Available:</IonLabel>
                    <IonCheckbox style={styles.checkbox} checked={isAvailable} onIonChange={(e) => setIsAvailable(e.detail.checked)} />
                </div>

                <div style={styles.inputContainer}>
                    <IonLabel style={styles.label}>Price:</IonLabel>
                    <IonInput style={styles.input} value={price.toString()} onIonChange={(e) => setPrice(parseInt(e.detail.value || '0'))} />
                </div>

                <IonLoading isOpen={saving} />
                {savingError && <div style={styles.errorMessage}>{savingError.message || 'Failed to save book'}</div>}
            </IonContent>
        </IonPage>
    );
};

export default BookEdit;
