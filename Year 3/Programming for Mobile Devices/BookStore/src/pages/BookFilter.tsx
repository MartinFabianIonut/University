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

const BookFilter: React.FC<RouteComponentProps> = ({ history }) => {
    const { books } = useContext(BookContext);

    log('entered');

    // Calculate the minimum and maximum prices from the existing books
    const minPrice = books ? Math.min(...(books.map(book => book.price || 0))) : 0;
    const maxPrice = books ? Math.max(...(books.map(book => book.price || 0))) : 0;

    const [priceFilter, setPriceFilter] = useState<number | undefined>(undefined);
    const [availabilityFilter, setAvailabilityFilter] = useState<boolean | undefined>(undefined);

    const priceRangeOptions: number[] = [];
    for (let i = minPrice; i <= maxPrice; i += 5) {
        priceRangeOptions.push(i);
    }

    log('render');

    return (
        <IonPage>
            <IonHeader>
                <CustomToolbar title="Filter books" titleStyle="title" />
            </IonHeader>
            <IonContent fullscreen>
                <IonSelect
                    value={priceFilter}
                    placeholder="Select Price Range"
                    onIonChange={(e) => setPriceFilter(e.detail.value)}
                >
                    {priceRangeOptions.map((price) => (
                        <IonSelectOption key={price} value={price}>
                            {`$${price} - $${price + 4}`}
                        </IonSelectOption>
                    ))}
                </IonSelect>
                <IonSelect
                    value={availabilityFilter}
                    placeholder="Select Availability"
                    onIonChange={(e) => setAvailabilityFilter(e.detail.value === 'true')}
                >
                    <IonSelectOption value={true}>Available</IonSelectOption>
                    <IonSelectOption value={false}>Not Available</IonSelectOption>
                </IonSelect>
                <IonList>
                    {books &&
                        books
                            .filter((book) => {
                                const priceMatch =
                                    priceFilter === undefined ||
                                    (book.price !== undefined && book.price >= priceFilter && book.price < priceFilter + 5);
                                const availabilityMatch = availabilityFilter === undefined ||
                                    (book.isAvailable && availabilityFilter)
                                    ||
                                    (!book.isAvailable && !availabilityFilter);
                                return priceMatch && availabilityMatch;
                            })
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
                            ))}
                </IonList>
            </IonContent>
        </IonPage>
    );
};

export default BookFilter;
