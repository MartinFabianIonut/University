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
    IonFab,
    IonFabButton,
    IonFabList,
    IonIcon,
    IonImg,
    IonActionSheet,
} from '@ionic/react';
import { Prompt } from 'react-router-dom';
import { getLogger } from '../core';
import { BookContext } from '../providers/BookProvider';
import { RouteComponentProps } from 'react-router';
import { BookProps } from '../core/BookProps';
import { format, set } from 'date-fns';
import CustomToolbar from '../components/CustomToolbar';
import { camera, images, trash, close } from 'ionicons/icons';
import { MyPhoto, usePhotos } from '../hooks/usePhotos';

const log = getLogger('BookEdit');

interface BookEditProps extends RouteComponentProps<{ id?: string }> { }


function parseDDMMYYYY(dateString: string) {
    const [day, month, year] = dateString.split('/').map(Number);
    if (isNaN(day) || isNaN(month) || isNaN(year)) {
        log('Invalid date components');
        return null;
    }
    const adjustedMonth = month - 1;
    const parsedDate = new Date(year, adjustedMonth, day);
    if (
        parsedDate.getDate() !== day ||
        parsedDate.getMonth() !== adjustedMonth ||
        parsedDate.getFullYear() !== year
    ) {
        console.error('Invalid date');
        return null;
    }
    if (isNaN(parsedDate.getTime())) {
        log('Invalid date');
        return null;
    }

    return parsedDate;
}

const BookEdit: React.FC<BookEditProps> = ({ history, match }) => {
    const { books, saving, saveBook } = useContext(BookContext);
    const { takePhoto, deletePhoto } = usePhotos();
    const [book, setBook] = useState<BookProps | undefined>(undefined);
    const [title, setTitle] = useState('Type a title');
    const [author, setAuthor] = useState('Type an author');
    const [publicationDate, setPublicationDate] = useState<Date | undefined>(undefined);
    const [isAvailable, setIsAvailable] = useState(false);
    const [price, setPrice] = useState(0);
    const [photo, setPhoto] = useState<string | undefined>(undefined);
    const [photoToDelete, setPhotoToDelete] = useState<MyPhoto>();
    const [unsavedChanges, setUnsavedChanges] = useState(false);

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
            setPhoto(foundBook.photo);
        }
    }, [match.params.id, books]);

    const myPhoto: MyPhoto | undefined = photo ? {
        filepath: `${book?.id}.jpeg`,
        webviewPath: `data:image/jpeg;base64,${photo}`
    } : undefined;

    const handleSave = useCallback(() => {
        const editedBook: BookProps = {
            id: book?.id,
            title,
            author,
            publicationDate: publicationDate || new Date(),
            isAvailable,
            price,
            photo
        };
        setUnsavedChanges(false);
        log('handleSave - Saving edited book');
        saveBook && saveBook(editedBook).then(() => {
            log('handleSave - Book saved successfully. Navigating back.');
            history.goBack();
        });
    }, [book, saveBook, title, author, publicationDate, isAvailable, price, photo, history]);

    log('render ' + title);
    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Edit Book" titleStyle="title" />
            </IonHeader>
            <IonContent>
                <div className='inputContainer' >
                    <IonLabel className='label'>Title:</IonLabel>
                    <IonInput className='input' value={title} onIonChange={(e) => {
                        setTitle(e.detail.value || '');
                        setUnsavedChanges(true);
                    }} />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Author:</IonLabel>
                    <IonInput className='input' value={author} onIonChange={(e) => {
                        setAuthor(e.detail.value || '');
                        setUnsavedChanges(true);
                    }} />
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
                                setUnsavedChanges(true);
                            }
                            else {
                                e.detail.value = publicationDate ? format(new Date(publicationDate), 'dd/MM/yyyy') : '';
                            }
                        }}
                    />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Available:</IonLabel>
                    <IonCheckbox className='checkbox' checked={isAvailable} onIonChange={(e) => {
                        setIsAvailable(e.detail.checked);
                        setUnsavedChanges(true);
                    }} />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Price:</IonLabel>
                    <IonInput className='input' value={price.toString()} onIonChange={(e) => {
                        setPrice(parseInt(e.detail.value || '0'));
                        setUnsavedChanges(true);
                    }} />
                </div>

                {myPhoto && (
                    <IonImg
                        onClick={() => setPhotoToDelete(myPhoto)}
                        src={myPhoto.webviewPath}
                        alt={myPhoto.filepath}
                        style={{ width: '100%', height: 'auto' }}
                    />
                )}

                <IonButton className="custom-button"
                    shape='round'
                    color='secondary'
                    style={{ marginTop: '20px' }}
                    onClick={() => {
                        setUnsavedChanges(false);
                        handleSave();
                    }}>
                    Save
                </IonButton>

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton>
                        <IonIcon icon={images} title="Load Photo" aria-label="Load Photo" />
                    </IonFabButton>

                    <IonFabList side="top">
                        <IonFabButton
                            onClick={async () => {
                                const newPhoto = await takePhoto();
                                setPhoto(newPhoto);
                                setUnsavedChanges(true);
                            }}
                        >
                            <IonIcon icon={camera} title="Take Photo" aria-label="Take Photo" />
                        </IonFabButton>
                    </IonFabList>
                </IonFab>

                <IonActionSheet
                    isOpen={!!photoToDelete}
                    buttons={[{
                        text: 'Delete',
                        role: 'destructive',
                        icon: trash,
                        handler: async () => {
                            if (photoToDelete) {
                                deletePhoto(photoToDelete.filepath);
                                setPhoto(undefined);
                                setPhotoToDelete(undefined);
                            }
                        }
                    }, {
                        text: 'Cancel',
                        icon: close,
                        role: 'cancel'
                    }]}
                    onDidDismiss={() => setPhotoToDelete(undefined)}
                />

                <Prompt
                    when={unsavedChanges}
                    message="You have unsaved changes. Are you sure you want to leave?"
                />

                <IonLoading isOpen={saving} />
            </IonContent>
        </IonPage >
    );
};

export default BookEdit;
