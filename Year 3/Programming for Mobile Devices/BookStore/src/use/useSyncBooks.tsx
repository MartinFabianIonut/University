// useSyncBooks.ts
import { useEffect, useCallback, useContext } from 'react';
import { useNetwork } from './useNetwork';
import { BookContext } from '../pages/BookProvider';

const SYNC_INTERVAL = 10000; // Adjust the interval as needed

const useSyncBooks = () => {
  const { networkStatus } = useNetwork();
  const { saveBook, books } = useContext(BookContext); // Update with your actual context methods

  const syncBooks = useCallback(async () => {
    if (books) {
      if (networkStatus.connected && books.length > 0) {
        // If online and there are locally stored books, sync them
        for (const localBook of books) {
          try {
            if (saveBook) {
              await saveBook(localBook);
            }
          } catch (error) {
            console.error('Error syncing book:', error);
            // Handle errors as needed
          }
        }
      }
    }
  }, [networkStatus.connected, books, saveBook]);

  useEffect(() => {
    const syncInterval = setInterval(syncBooks, SYNC_INTERVAL);

    return () => clearInterval(syncInterval);
  }, [networkStatus]);
};

export default useSyncBooks;
