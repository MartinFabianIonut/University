import glob
import cv2
import numpy as np
from keras.layers import LSTM, Dense, Conv2D, MaxPooling2D, Flatten, Reshape
from keras.models import Sequential
from keras.utils import to_categorical
from sklearn.model_selection import train_test_split
from sklearn.utils import shuffle


def preprocess_image(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    resized_image = cv2.resize(gray, (48, 48))
    preprocessed_image = np.expand_dims(resized_image, axis=-1)
    return preprocessed_image / 255.0


def readPhotoWithCV2(fileName):
    # read the image with cv2
    imgs = []
    for image in fileName:
        img = cv2.imread(image)
        img = preprocess_image(img)
        imgs.append(img)
    return imgs


def createDataSet(images_angry, images_happy, angry_percent, happy_percent):
    images = []
    labels = []
    i = 0
    for image in images_angry:
        images.append(image)
        labels.append(1)
        if i == len(images_happy)*angry_percent:
            break
    i = 0
    for image in images_happy:
        images.append(image)
        labels.append(0)
        if i == len(images_happy)*happy_percent:
            break
    return images, labels


def splitDataSet(images, labels, test_size=0.2):
    # split the data set into train and test
    train_data, test_data, train_labels, test_labels = train_test_split(images, labels, test_size=test_size,
                                                                        random_state=42)
    return train_data, test_data, train_labels, test_labels


def calibrateDataSet(train_data, train_labels, test_data, test_labels):
    if len(train_data) > len(test_data) * 4:
        train_data = train_data[:len(test_data) * 4]
        train_labels = train_labels[:len(test_data) * 4]
    elif len(train_data) < len(test_data) * 4:
        test_data = test_data[:len(train_data) // 4]
        test_labels = test_labels[:len(train_data) // 4]
        train_data = train_data[:len(test_data) * 4]
        train_labels = train_labels[:len(test_data) * 4]
    return train_data, train_labels, test_data, test_labels


def predictSentiment(images_angry, images_happy, angry_percent, happy_percent):
    # Create the model
    model = Sequential()
    model.add(Conv2D(32, (3, 3), activation='relu', input_shape=(48, 48, 1)))
    model.add(MaxPooling2D((2, 2)))
    model.add(Conv2D(64, (3, 3), activation='relu'))
    model.add(Flatten())
    # Reshape output of Flatten layer to match LSTM input shape
    model.add(Dense(256, activation='relu'))
    model.add(Reshape((1, 256)))
    # Add LSTM layer with 256 units and return sequences equal to False since we are only interested in the last output
    model.add(LSTM(256, return_sequences=False))
    model.add(Dense(2, activation='softmax'))
    model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

    # Create the data set
    np.random.seed(5)
    images, labels = createDataSet(images_angry, images_happy, angry_percent, happy_percent)
    images, labels = shuffle(images, labels)
    train_data, test_data, train_labels, test_labels = splitDataSet(images, labels)
    train_data, train_labels, test_data, test_labels = calibrateDataSet(train_data, train_labels, test_data,
                                                                        test_labels)

    train_data = np.asarray(train_data)
    train_labels = np.asarray(train_labels)
    test_data = np.asarray(test_data)
    test_labels = np.asarray(test_labels)

    train_labels = to_categorical(train_labels)
    test_labels = to_categorical(test_labels)

    model.fit(train_data, train_labels, epochs=8, verbose=2, validation_data=(test_data, test_labels), batch_size=6)

    # Generating captions for test images:
    predicted_captions = model.predict(test_data)

    # predict the sentiment of the images
    predicted_classes = np.argmax(predicted_captions, axis=1)
    # print last 5 predictions
    print("Last 5 predictions: ", predicted_classes[-5:])
    sentiment_score = predicted_classes[-1]
    sentiment_label_mapping = {0: 'negative', 1: 'positive'}
    print("Predicted sentiment: ", sentiment_label_mapping[sentiment_score])


if __name__ == '__main__':
    # read the images from the folder images
    folder_path = 'images/angry/*.jpg'
    image_paths = glob.glob(folder_path)
    images_angry = readPhotoWithCV2(image_paths)

    folder_path = 'images/happy/*.jpg'
    image_paths = glob.glob(folder_path)
    images_happy = readPhotoWithCV2(image_paths)

    # predict the sentiment of the images
    predictSentiment(images_angry, images_happy, 0.2, 0.8) # 20% angry, 80% happy

    predictSentiment(images_angry, images_happy, 0.8, 0.2) # 80% angry, 20% happy

    predictSentiment(images_angry, images_happy, 0.5, 0.5) # 50% angry, 50% happy, more random

