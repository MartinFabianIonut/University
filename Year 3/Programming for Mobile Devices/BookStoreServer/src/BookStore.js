
import { open } from 'sqlite';

import sqlite3 from 'sqlite3';

export class BookStore {
    constructor({ filename }) {
        this.filename = filename;
    }

    async init() {
        this.db = await open({
            filename: this.filename,
            driver: sqlite3.Database,
        });

        await this.db.run(`
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                author TEXT,
                publicationDate TEXT,
                isAvailable INTEGER,
                price NUMERIC,
                userId TEXT
            );
        `);
    }

    async find(props) {
        const { userId } = props;
        return this.db.all('SELECT * FROM books WHERE userId = ?', [userId]);
    }

    async findOne(props) {
        const { id } = parseInt(props.id);
        return this.db.get('SELECT * FROM books WHERE id = ?', [id]);
    }

    async insert(book) {
        if (!book.title || !book.author || !book.userId) {
            throw new Error("Title, Author, and UserId are required");
        }

        const { title, author, publicationDate, isAvailable, price, userId } = book;

        await this.db.run(
            'INSERT INTO books (title, author, publicationDate, isAvailable, price, userId) VALUES (?, ?, ?, ?, ?, ?)',
            [title, author, publicationDate, isAvailable, price, userId]
        );

        // Get the last inserted row id
        const { lastID } = await this.db.get('SELECT last_insert_rowid() as lastID');
        // put as string
        book.id = lastID.toString();
        return book;
    }

    async update(props, book) {
        const { title, author, publicationDate, isAvailable, price } = book;
        await this.db.run('UPDATE books SET title = ?, author = ?, publicationDate = ?, isAvailable = ?, price  = ? WHERE id = ?', 
        [title, author, publicationDate, isAvailable, price, props.id]);
        // return number of rows updated
        return 1;
    }

    async remove(props) {
        const { id } = parseInt(props.id);
        await this.db.run('DELETE FROM books WHERE id = ?', [id]);
    }
}

const bookStore = new BookStore({ filename: './db/books.db' });
await bookStore.init(); 

export { bookStore };
