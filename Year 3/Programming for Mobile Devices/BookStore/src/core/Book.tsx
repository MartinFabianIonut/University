import React, { memo } from 'react';
import { IonImg, IonItem, IonLabel } from '@ionic/react';
import { format } from 'date-fns';
import { getLogger } from '.';
import { BookProps } from './BookProps';

const log = getLogger('Book');

interface BookPropsExt extends BookProps {
    onEdit: (bookId?: string) => void;
}

const Book: React.FC<BookPropsExt> = ({ id, title, author, publicationDate, isAvailable, price, photo, lat, lng, onEdit }) => {
    const formattedDate = publicationDate ? format(new Date(publicationDate), 'dd/MM/yyyy') : '';
    const webviewPath = `data:image/jpeg;base64,${photo}`;
    log('render book ' + id);
    return (
        <IonItem className="book-item" onClick={() => onEdit(id)}>
            <IonLabel>
                <h2>{title}</h2>
                <p>{`Author: ${author}`}</p>
                <p>{`Publication: ${formattedDate}`}</p>
                <p>{`Available: ${isAvailable ? 'Yes' : 'No'}`}</p>
                <p>{`Price: ${price}`}</p>
                <p>{`Coord: ${lat?.toFixed(3)} - ${lng?.toFixed(3)}`}</p>
            </IonLabel>
            {photo && (<IonImg
                src={webviewPath}
                alt={`${id}.jpeg`}
                style={{ width: '50%', height: 'auto' }}
            />)}

        </IonItem>
    );
};

export default memo(Book);
