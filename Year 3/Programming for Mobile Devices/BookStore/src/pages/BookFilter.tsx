import React, { useState, useContext, useEffect } from 'react';
import {
    IonContent,
    IonHeader,
    IonItem,
    IonLabel,
    IonList,
    IonSelect,
    IonSelectOption,
    IonPage,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonRange
} from '@ionic/react';
import { getLogger } from '../core';
import { BookContext } from '../providers/BookProvider';
import CustomToolbar from '../components/CustomToolbar';
import Book from '../core/Book';
import { RouteComponentProps } from 'react-router';
import { BookProps } from '../core/BookProps';

const log = getLogger('BookSearch');

const BookFilter: React.FC<RouteComponentProps> = ({ history }) => {
    const { books } = useContext(BookContext);
    const [filteredBooks, setFilteredBooks] = useState<BookProps[] | undefined>(undefined);
    const [loading, setLoading] = useState(true);

    log('entered');

    // Calculate the minimum and maximum prices from the existing books
    const minPrice = books ? Math.min(...(books.map(book => book.price || 0))) : 0;
    const maxPrice = books ? Math.max(...(books.map(book => book.price || 0))) : 0;

    const [priceFilter, setPriceFilter] = useState<{ lower: number; upper: number }>({
        lower: minPrice,
        upper: maxPrice,
    });

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

    const [availabilityFilter, setAvailabilityFilter] = useState<boolean | undefined>(undefined);

    async function filterBooks() {
        const filtered = books?.filter((book) => {
            const priceMatch =
                (priceFilter.lower === undefined || book.price >= priceFilter.lower) &&
                (priceFilter.upper === undefined || book.price <= priceFilter.upper);

            const availabilityMatch =
                availabilityFilter === undefined ||
                (book.isAvailable && availabilityFilter) ||
                (!book.isAvailable && !availabilityFilter);

            return priceMatch && availabilityMatch;
        });
        return filtered;
    }

    useEffect(() => {
        setLoading(true);
        filterBooks().then((result) => {
            setFilteredBooks(result);
            setLoading(false);
        });
    }, [priceFilter, availabilityFilter]);

    log('render');

    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Filter books" titleStyle="title" />
            </IonHeader>
            <IonContent fullscreen>
                <IonItem>
                    <IonSelect
                        value={availabilityFilter}
                        placeholder="Select Availability"
                        onIonChange={(e) => setAvailabilityFilter(e.detail.value)}
                        aria-label="Select Availability"
                        interface="alert"
                    >
                        <IonSelectOption value={true}>Available</IonSelectOption>
                        <IonSelectOption value={false}>Not Available</IonSelectOption>
                    </IonSelect>
                </IonItem>

                <IonItem>
                    <IonLabel>Filter by price:</IonLabel>
                    <IonRange
                        title='Dual Knobs Range'
                        pin={true}
                        aria-label='Dual Knobs Range'
                        dualKnobs={true}
                        value={priceFilter}
                        min={minPrice}
                        max={maxPrice}
                        onIonChange={(e) => setPriceFilter(e.detail.value as { lower: number; upper: number })}
                    ></IonRange>
                </IonItem>
                <IonItem>
                    <IonLabel>Lower Price: {priceFilter.lower}</IonLabel>
                    <IonLabel>Upper Price: {priceFilter.upper}</IonLabel>
                </IonItem>


                <IonList>
                    {!loading && filteredBooks && filteredBooks.slice(0, loadedBooks).map(({ id, title, author, publicationDate, isAvailable, price, photo, lat, lng }) => (
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
                            onEdit={() => history.push(`/book/${id}`)}
                        />
                    ))}
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

export default BookFilter;
