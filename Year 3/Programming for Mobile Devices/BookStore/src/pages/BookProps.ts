export interface BookProps {
    id?: string;
    title: string;
    author: string;
    publicationDate: Date;
    isAvailable: boolean;
    price : number;
}