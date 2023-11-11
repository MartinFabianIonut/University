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


    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Book List" titleStyle="title" />
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching books" />
                {books && (
                    <><IonList>
                        {books.slice(0, loadedBooks).map(({ id, title, author, publicationDate, isAvailable, price, photo }) => (
                            <Book
                                key={id}
                                id={id}
                                title={title}
                                author={author}
                                publicationDate={publicationDate}
                                isAvailable={isAvailable}
                                price={price}
                                photo={photo}
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
