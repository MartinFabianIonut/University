import { useCamera } from './useCamera';
import { useFilesystem } from './useFilesystem';
import { useEffect } from 'react';

export interface MyPhoto {
  filepath: string;
  webviewPath?: string;
}


export function usePhotos() {
  const { getPhoto } = useCamera();
  const { writeFile, deleteFile } = useFilesystem();

  useEffect(() => {
    // // make a setup function that will run once
    // setup();

    // async function setup() {
    //   const photos = await readPhotos();
    //   for (const photo of photos) {
    //     await deletePhoto(photo);
    //   }
    // }
  }, []);

  return {
    takePhoto,
    deletePhoto,
    savePhotoLocally,
  };

  async function takePhoto() {
    const { base64String } = await getPhoto();
    return base64String;
  }

  async function deletePhoto(filepath: string) {
    await deleteFile(filepath);
  }

  async function savePhotoLocally(bookId: string, base64String: string) {
    const filepath =bookId + '.jpeg';
    await writeFile(filepath, base64String);
  }
}
