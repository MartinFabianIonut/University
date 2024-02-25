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
import { useMyLocation } from '../hooks/useMyLocation';
import MyMap from '../components/MyMap';
import { createAnimation } from '@ionic/react';
import Bruckner8 from '../core/audio/Celibidache_Bruckner_8_Finale.mp3'

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
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [publicationDate, setPublicationDate] = useState<Date | undefined>(undefined);
    const [isAvailable, setIsAvailable] = useState(false);
    const [price, setPrice] = useState(0);
    const [photo, setPhoto] = useState<string | undefined>(undefined);
    const [lat, setLat] = useState<number | undefined>(undefined);
    const [lng, setLng] = useState<number | undefined>(undefined);
    const [photoToDelete, setPhotoToDelete] = useState<MyPhoto>();
    const [unsavedChanges, setUnsavedChanges] = useState(false);
    const myLocation = useMyLocation();
    const [shakeAnimation, setShakeAnimation] = useState(false);

    useEffect(() => {
        if (myLocation.position?.coords && !lat && !lng) {
            const { latitude, longitude } = myLocation.position.coords;
            log(`useEffect - Setting lat: ${latitude}, lng: ${longitude}`);
            setLat(latitude);
            setLng(longitude);
        }
    }, [myLocation]);

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
            setLat(foundBook.lat);
            setLng(foundBook.lng);
        }
    }, [match.params.id, books]);

    const myPhoto: MyPhoto | undefined = photo ? {
        filepath: `${book?.id}.jpeg`,
        webviewPath: `data:image/jpeg;base64,${photo}`
    } : undefined;

    const handleSave = useCallback(() => {
        if (!title || !author) {
            setShakeAnimation(true);
            setTimeout(() => {
                setShakeAnimation(false);
            }, 1000);
            return;
        }
        const editedBook: BookProps = {
            id: book?.id,
            title,
            author,
            publicationDate: publicationDate || new Date(),
            isAvailable,
            price,
            photo,
            lat,
            lng,
        };
        setUnsavedChanges(false);
        log('handleSave - Saving edited book');
        const audio = new Audio(Bruckner8);
        //audio.play();

        setTimeout(() => {
            audio.pause();
            audio.currentTime = 0;
        }, 10000);

        saveBook && saveBook(editedBook).then(() => {
            log('handleSave - Book saved successfully. Navigating back.');
            history.goBack();
        });
    }, [book, saveBook, title, author, publicationDate, isAvailable, price, photo, lat, lng, history]);

    const handleMapClick = useCallback((latLng: { latitude: number; longitude: number }) => {
        const { latitude, longitude } = latLng;
        log(`handleMapClick - lat: ${latitude}, lng: ${longitude}`);
        setLat(latitude);
        setLng(longitude);
        setUnsavedChanges(true);
    }, []);

    useEffect(() => {
        if (shakeAnimation) {
            const emptyInputFields = [];

            // Check if title is empty
            if (!title.trim()) {
                const titleInput = document.querySelector('.inputContainer.title input');
                if (titleInput) {
                    emptyInputFields.push(titleInput);
                }
            }

            // Check if author is empty
            if (!author.trim()) {
                const authorInput = document.querySelector('.inputContainer.author input');
                if (authorInput) {
                    emptyInputFields.push(authorInput);
                }
            }

            if (emptyInputFields.length > 0) {
                emptyInputFields.forEach((inputField) => {
                    const container = inputField.closest('.inputContainer');
                    if (container) {
                        const animation = createAnimation()
                            .addElement(container)
                            .duration(500)
                            .direction('alternate')
                            .iterations(3)
                            .keyframes([
                                { offset: 0, transform: 'translateX(0)' },
                                { offset: 0.25, transform: 'translateX(-10px)' },
                                { offset: 0.5, transform: 'translateX(10px)' },
                                { offset: 0.75, transform: 'translateX(-10px)' },
                                { offset: 1, transform: 'translateX(0)' }
                            ]);
                        animation.play();
                    }
                });
            }
        }
    }, [shakeAnimation, title, author]);



    log('render ' + title);
    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Edit Book" titleStyle="title" />
            </IonHeader>
            <IonContent>
                <div className='inputContainer title' >
                    <IonLabel className='label'>Title:</IonLabel>
                    <IonInput className='input' placeholder='Type a title' value={title} onIonChange={(e) => {
                        setTitle(e.detail.value || '');
                        setUnsavedChanges(true);
                    }} />
                </div>

                <div className='inputContainer author'>
                    <IonLabel className='label'>Author:</IonLabel>
                    <IonInput className='input' placeholder='Type an author' value={author} onIonChange={(e) => {
                        setAuthor(e.detail.value || '');
                        setUnsavedChanges(true);
                    }} />
                </div>

                <div className='inputContainer'>
                    <IonLabel className='label'>Publication Date:</IonLabel>
                    <IonInput
                        class="input"
                        className='input'
                        placeholder='dd/MM/yyyy'
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

                {lat && lng &&
                    <MyMap
                        lat={lat}
                        lng={lng}
                        onMapClick={(e) => handleMapClick(e)}
                    />}

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
