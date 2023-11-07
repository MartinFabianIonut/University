import React, { useState, useContext, useEffect } from 'react';
import {
    IonContent,
    IonHeader,
    IonItem,
    IonLabel,
    IonList,
    IonPage,
    IonSearchbar,
    IonTitle,
    IonToolbar,
    useIonViewWillEnter,
    IonInfiniteScroll,
    IonInfiniteScrollContent
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from './BookProvider';
import CustomToolbar from '../components/CustomToolbar';
import Book from './Book';
import { RouteComponentProps } from 'react-router';

const log = getLogger('BookSearch');

const BookSearch: React.FC<RouteComponentProps> = ({ history }) => {
    const { books } = useContext(BookContext);

    const [titleSearch, setTitleSearch] = useState<string>('');
    const [authorSearch, setAuthorSearch] = useState<string>('');

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

    log('render');

    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Search books" titleStyle="title" />
            </IonHeader>
            <IonContent fullscreen>
                <IonSearchbar
                    value={titleSearch}
                    debounce={1000}
                    onIonChange={(e) => setTitleSearch(e.detail.value!)}
                    placeholder="Search by title"
                ></IonSearchbar>
                <IonSearchbar
                    value={authorSearch}
                    debounce={1000}
                    onIonChange={(e) => setAuthorSearch(e.detail.value!)}
                    placeholder="Search by author"
                ></IonSearchbar>
                <IonList>
                    {books && books
                        .filter((book) => {
                            const titleMatch = book.title.toLowerCase().includes(titleSearch.toLowerCase());
                            const authorMatch = book.author.toLowerCase().includes(authorSearch.toLowerCase());
                            return titleMatch && authorMatch;
                        })
                        .slice(0, loadedBooks)
                        .map(({ id, title, author, publicationDate, isAvailable, price }) => (
                            <Book
                                key={id}
                                id={id}
                                title={title}
                                author={author}
                                publicationDate={publicationDate}
                                isAvailable={isAvailable}
                                price={price}
                                onEdit={id => history.push(`/book/${id}`)}
                            />
                        ))
                    }
                </IonList>
                <IonInfiniteScroll
                    threshold="50px"
                    disabled={disableInfiniteScroll}
                    onIonInfinite={(e: CustomEvent<void>) => {
                        loadMoreData();
                        (e.target as HTMLIonInfiniteScrollElement).complete();
                    }}
                >
                    <IonInfiniteScrollContent loadingText="Loading more data..."></IonInfiniteScrollContent>
                </IonInfiniteScroll>
            </IonContent>
        </IonPage>
    );
};

export default BookSearch;
