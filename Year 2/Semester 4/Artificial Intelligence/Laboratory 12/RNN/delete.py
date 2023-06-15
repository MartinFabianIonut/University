import os
import glob

def delete_images_in_range(folder_path):
    # Get a list of all image files in the folder
    image_files = glob.glob(os.path.join(folder_path, "*.jpg"))  # Change the file extension as per your requirement

    for file_path in image_files:
        filename = os.path.basename(file_path)
        # Extract the number part from the filename
        file_number = int(os.path.splitext(filename)[0])

        # Check if the file number is within the specified range
        if file_number > 50000:
            os.remove(file_path)  # Delete the image file

if __name__ == "__main__":
    folder_path = "celeb_dataset/images"
    delete_images_in_range(folder_path)
