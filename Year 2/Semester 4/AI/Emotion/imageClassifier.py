import os
import sys
import cv2
import numpy as np
from keras.utils import to_categorical
from skimage.feature import hog
from sklearn.svm import SVC
from keras_facenet import FaceNet

# # first implementation, using hog and svc classifier
# class ImageClassifier:
#     def __init__(self):
#         self.clf = SVC(kernel='linear')
#
#     def extract_hog_features(self, image):
#         gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
#         hog_features = hog(gray, orientations=9, pixels_per_cell=(8, 8), cells_per_block=(2, 2))
#         return hog_features
#
#     def fit(self, images, labels):
#         data = []
#         for image in images:
#             hog_features = self.extract_hog_features(image)
#             data.append(hog_features)
#         data = np.array(data)
#         labels = np.array(labels)
#         self.clf.fit(data, labels)
#
#     def predict(self, images):
#         data = []
#         for image in images:
#             hog_features = self.extract_hog_features(image)
#             data.append(hog_features)
#         data = np.array(data)
#         predictions = self.clf.predict(data)
#         return predictions

from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense

class ImageClassifier:
    def __init__(self):
        self.model = Sequential()
        self.model.add(Conv2D(32, (3, 3), activation='relu', input_shape=(48, 48, 1)))
        self.model.add(MaxPooling2D((2, 2)))
        self.model.add(Conv2D(64, (3, 3), activation='relu'))
        self.model.add(MaxPooling2D((2, 2)))
        self.model.add(Flatten())
        self.model.add(Dense(64, activation='relu'))
        self.model.add(Dense(7, activation='softmax'))  # Assuming 7 classes for facial emotions
        self.model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    def preprocess_image(self, image):
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        resized_image = cv2.resize(gray, (48, 48))
        preprocessed_image = np.expand_dims(resized_image, axis=-1)
        return preprocessed_image / 255.0

    def fit(self, images, labels):
        data = []
        for image in images:
            preprocessed_image = self.preprocess_image(image)
            data.append(preprocessed_image)
        data = np.array(data)
        labels = to_categorical(labels)

        self.model.fit(data, labels, epochs=10, batch_size=32)

    def predict(self, images):
        data = []
        for image in images:
            preprocessed_image = self.preprocess_image(image)
            data.append(preprocessed_image)
        data = np.array(data)

        predictions = self.model.predict(data)
        predicted_labels = np.argmax(predictions, axis=1)

        return predicted_labels


class ImageClassifierFacenet:
    def __init__(self):
        self.facenet = FaceNet()
        self.classifier = SVC(verbose=0)

    def preprocess_image(self, image):
        # Preprocess the image for FaceNet model input
        resized_image = cv2.resize(image, (48, 48))
        preprocessed_image = np.expand_dims(resized_image, axis=0)
        return preprocessed_image

    def extract_face_embeddings(self, image):
        preprocessed_image = self.preprocess_image(image)
        # Redirect standard output to a null device
        original_stdout = sys.stdout
        sys.stdout = open(os.devnull, 'w')

        embeddings = self.facenet.embeddings(preprocessed_image)[0]

        # Restore standard output
        sys.stdout.close()
        sys.stdout = original_stdout
        return embeddings

    def fit(self, images, labels):
        train_embeddings = []
        train_labels = []
        for image, label in zip(images, labels):
            embeddings = self.extract_face_embeddings(image)
            train_embeddings.append(embeddings)
            train_labels.append(label)
        self.classifier.fit(train_embeddings, train_labels)

    def predict(self, images):
        predictions = []
        for image in images:
            embeddings = self.extract_face_embeddings(image)
            prediction = self.classifier.predict(embeddings.reshape(1, -1))
            predictions.append(prediction)
        return predictions