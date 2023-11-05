import React, { memo } from 'react';
import { IonItem, IonLabel } from '@ionic/react';
import { format } from 'date-fns'; // Import the format function
import { getLogger } from '../core';
import { BookProps } from './BookProps';

const log = getLogger('Book');

interface BookPropsExt extends BookProps {
    onEdit: (bookId?: string) => void;
}

const Book: React.FC<BookPropsExt> = ({ id, title, author, publicationDate, isAvailable, price, onEdit }) => {
    // Format the date using date-fns
    const formattedDate = publicationDate ? format(new Date(publicationDate), 'dd/MM/yyyy') : '';
    log('render book ' + id);
    return (
        <IonItem className="book-item" onClick={() => onEdit(id)}>
            <IonLabel>
                <h2>{title}</h2>
                <p>{`Author: ${author}`}</p>
                <p>{`Publication Date: ${formattedDate}`}</p>
                <p>{`Available: ${isAvailable ? 'Yes' : 'No'}`}</p>
                <p>{`Price: ${price}`}</p>
            </IonLabel>
        </IonItem>
    );
};

export default memo(Book);
