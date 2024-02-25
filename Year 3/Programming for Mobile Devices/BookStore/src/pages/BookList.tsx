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
    IonToast,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonLabel,
} from '@ionic/react';
import { add } from 'ionicons/icons';
import Book from '../core/Book';
import { getLogger } from '../core';
import { BookContext } from '../providers/BookProvider';
import CustomToolbar from '../components/CustomToolbar';
import { useIonToast } from '../hooks/useIonToast';

const log = getLogger('BookList');

const BookList: React.FC<RouteComponentProps> = ({ history }) => {
    const { books, fetching, fetchingError, savingError } = useContext(BookContext);
    const { showToast, ToastComponent, getErrorMessage } = useIonToast();

    useEffect(() => {
        if (fetchingError) {
            showToast({
                message: getErrorMessage(fetchingError) || 'Failed to fetch books',
            });
            log('fetchingError: ', fetchingError);
        }
    }, [fetchingError]);

    useEffect(() => {
        if (savingError) {
            showToast({
                message: getErrorMessage(savingError) || 'Failed to save book',
            });
            log('savingError: ', savingError);
        }
    }, [savingError]);


    const [loadedBooks, setLoadedBooks] = useState(4);
    const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false);

    const loadMoreData = () => {
        const nextSetOfBooks = loadedBooks + 4;
        setLoadedBooks(nextSetOfBooks);
        setDisableInfiniteScroll(nextSetOfBooks >= books?.length!);
    };

    useEffect(() => {
        setLoadedBooks(4);
    }, [books]);

    log('render ', 'yes/no: ', fetching, ' ' + JSON.stringify(books?.slice(0, loadedBooks).map(book => ({ ...book, photo: undefined }))));
    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (event.code === 'ArrowDown') {
                event.preventDefault();
                // scroll down
                window.scrollY = window.scrollY + 100;
                window.HTMLIonRefresherContentElement;
            }
        };

        document.addEventListener('keydown', handleKeyDown);

        return () => {
            document.removeEventListener('keydown', handleKeyDown);
        };
    }, []);

    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Book List" titleStyle="title" />
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching books" />
                {books && (
                    <>
                        <IonLabel>
                            <iframe style={{ borderRadius: '10px' }} src="https://open.spotify.com/embed/track/4FepoDiOtxnqgBU7qnjrI8?utm_source=generator"
                                width="100%" height="352" allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture" loading="lazy" allowFullScreen></iframe>
                        </IonLabel>
                        <IonList>
                            {books.slice(0, loadedBooks).map(({ id, title, author, publicationDate, isAvailable, price, photo, lat, lng }) => (
                                <Book
                                    key={id}
                                    id={id}
                                    title={title}
                                    author={author}
                                    publicationDate={publicationDate}
                                    isAvailable={isAvailable}
                                    price={price}
                                    photo={photo}
                                    lat={lat}
                                    lng={lng}
                                    onEdit={(bookId) => history.push(`/book/${bookId}`)}
                                />
                            ))}
                        </IonList>
                        <IonInfiniteScroll
                            threshold="88px"
                            disabled={disableInfiniteScroll}
                            onIonInfinite={(e: CustomEvent<void>) => {
                                loadMoreData();
                                (e.target as HTMLIonInfiniteScrollElement).complete();
                            }}
                        >
                            <IonInfiniteScrollContent loadingText="Loading more data..."></IonInfiniteScrollContent>
                        </IonInfiniteScroll>
                    </>
                )}
                {ToastComponent}
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/book')}>
                        <IonIcon icon={add} title='add' aria-label='Add' />
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default BookList;
