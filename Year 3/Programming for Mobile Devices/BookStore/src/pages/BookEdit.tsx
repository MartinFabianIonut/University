import React, { useCallback, useContext, useEffect, useState } from 'react';
import {
    IonButton,
    IonContent,
    IonHeader,
    IonInput,
    IonLabel,
    IonLoading,
    IonPage,
    IonCheckbox,
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from './BookProps';
import { format } from 'date-fns';
import CustomToolbar from '../components/CustomToolbar';
import { useNetwork } from '../use/useNetwork';

const log = getLogger('BookEdit');

interface BookEditProps extends RouteComponentProps<{ id?: string }> { }


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
    const { networkStatus } = useNetwork();

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
        <IonPage >
            <IonHeader>
                <CustomToolbar title="Edit Book" titleStyle="title" />
            </IonHeader>
            <IonContent>
                <div className='inputContainer' >
                    <IonLabel className='label'>Title:</IonLabel>
                    <IonInput className='input' value={title} onIonChange={(e) => setTitle(e.detail.value || '')} />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Author:</IonLabel>
                    <IonInput className='input' value={author} onIonChange={(e) => setAuthor(e.detail.value || '')} />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Publication Date:</IonLabel>
                    <IonInput
                        class="input"
                        className='input'
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

                <div className='inputContainer'>
                    <IonLabel className='label'>Available:</IonLabel>
                    <IonCheckbox className='checkbox' checked={isAvailable} onIonChange={(e) => setIsAvailable(e.detail.checked)} />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Price:</IonLabel>
                    <IonInput className='input' value={price.toString()} onIonChange={(e) => setPrice(parseInt(e.detail.value || '0'))} />
                </div>

                <IonButton className="custom-button"
                    shape='round'
                    color='secondary'
                    style={{ marginTop: '20px' }}
                    onClick={handleSave}>
                    Save
                </IonButton>


                <IonLoading isOpen={saving} />
            </IonContent>
        </IonPage >
    );
};

export default BookEdit;
