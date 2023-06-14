import urllib.request

import cv2
import tensorflow as tf
from keras.layers import Dense, Flatten
from keras.models import Sequential
from keras.preprocessing.text import Tokenizer
from keras.utils import pad_sequences, to_categorical
import pandas as pd
import numpy as np
import json
import os
from methods.ploting import plotConfusionMatrix
from sklearn.metrics import confusion_matrix
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense


# import numpy as np


def writeToFile(fileName, textLocal):
    with open(fileName, 'w') as file:
        file.write(textLocal)


def readFromFilePhoto(fileName):
    # read the file line by line and return the list of lines withouth the \n
    with open(fileName) as file:
        lines = file.readlines()
    lines = [line.replace('\n', '') for line in lines]
    # add .png to each line
    lines = [line + '.png' for line in lines]
    # split fileName by /
    fileName = fileName.split('/')
    # get the first part of the fileName
    fileName = fileName[0]
    # add the fileName to each line at the beginning
    lines = [fileName + '/' + line for line in lines]
    return lines


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


if __name__ == '__main__':
    """ 
    
    # from here I download the emoji.json file
    url = "https://raw.githubusercontent.com/iamcal/emoji-data/master/emoji.json"
    urllib.request.urlretrieve(url, "emoji.json")
    
    # load the data
    with open("emoji.json") as f:
        data = json.load(f)
    
    # folders for happy and sad faces
    happy_folder = "happy_emojis"
    sad_folder = "sad_emojis"
    os.makedirs(happy_folder, exist_ok=True)
    os.makedirs(sad_folder, exist_ok=True)

    # Define Unicode character ranges for happy and sad faces
    happy_face_ranges = [("1F600", "1F607"),
                         ("1F609", "1F60F"),
                         ("1F617", "1F61D"),
                         ("1F638", "1F63D"),
                         ("1F642", "1F643"),
                         ("1F917", "1F917"),
                         ("1F920", "1F921"),
                         ("1F924", "1F924"),
                         ("1F929", "1F92A"),
                         ("1F973", "1F973")
                         ]
    sad_face_ranges = [("1F613", "1F615"),
                       ("1F617", "1F619"),
                       ("1F622", "1F625"),
                       ("1F629", "1F629"),
                       ("1F62B", "1F62B"),
                       ("1F62D", "1F62D"),
                       ("1F630", "1F630"),
                       ("1F63F", "1F63F"),
                       ("1F910", "1F910"),
                       ("1F922", "1F922"),
                       ("1F927", "1F928"),
                       ("1F92E", "1F92F"),
                       ("1F974", "1F976"),
                       ]

    # split into two lists, happy and sad
    happy_emoji = []
    sad_emoji = []
    happy_emojis_text = ""
    sad_emojis_text = ""

    for emoji in data:
        unified = emoji["unified"]
        category = emoji["category"]
        if any(start <= unified <= end for start, end in happy_face_ranges) and category == "Smileys & Emotion":
            happy_emoji.append(unified)
            happy_emojis_text += unified + "\n"
            image_url = f"https://raw.githubusercontent.com/iamcal/emoji-data/master/img-apple-64/{emoji['image']}"
            image_filename = f"{emoji['unified']}.png"
            urllib.request.urlretrieve(image_url, os.path.join(happy_folder, image_filename))
        elif any(start <= unified <= end for start, end in sad_face_ranges) and category == "Smileys & Emotion":
            sad_emoji.append(unified)
            sad_emojis_text += unified + "\n"
            image_url = f"https://raw.githubusercontent.com/iamcal/emoji-data/master/img-apple-64/{emoji['image']}"
            image_filename = f"{emoji['unified']}.png"
            urllib.request.urlretrieve(image_url, os.path.join(sad_folder, image_filename))

    writeToFile(os.path.join(happy_folder, 'happy.txt'), happy_emojis_text)
    writeToFile(os.path.join(sad_folder, 'sad.txt'), sad_emojis_text)
    """

    happy_emoji = readFromFilePhoto("happy_emojis/happy.txt")
    sad_emoji = readFromFilePhoto("sad_emojis/sad.txt")
    happy_emoji += readFromFilePhoto("happy_emojis/happy.txt")
    happy_emoji += readFromFilePhoto("happy_emojis/happy.txt")
    sad_emoji += readFromFilePhoto("sad_emojis/sad.txt")

    happy_emoji = readPhotoWithCV2(happy_emoji)
    sad_emoji = readPhotoWithCV2(sad_emoji)

    # create training data
    np.random.seed(5)
    indexes = [i for i in range(len(happy_emoji))]
    np.random.shuffle(indexes)
    trainSample = np.random.choice(indexes, int(0.8 * len(happy_emoji)), replace=False)
    testSample = [i for i in indexes if not i in trainSample]

    train_data = [happy_emoji[i] for i in trainSample]
    train_labels = [1 for i in trainSample]
    test_data = [happy_emoji[i] for i in testSample]
    test_labels = [1 for i in testSample]

    indexes = [i for i in range(len(sad_emoji))]
    np.random.shuffle(indexes)
    trainSample = np.random.choice(indexes, int(0.8 * len(sad_emoji)), replace=False)
    testSample = [i for i in indexes if not i in trainSample]

    train_data += [sad_emoji[i] for i in trainSample]
    train_labels += [0 for i in trainSample]
    test_data += [sad_emoji[i] for i in testSample]
    test_labels += [0 for i in testSample]

    # if len(train_data) != len(test_data)*4 make them equal
    if len(train_data) > len(test_data) * 4:
        train_data = train_data[:len(test_data) * 4]
        train_labels = train_labels[:len(test_data) * 4]
    elif len(train_data) < len(test_data) * 4:
        test_data = test_data[:len(train_data) // 4]
        test_labels = test_labels[:len(train_data) // 4]
        train_data = train_data[:len(test_data) * 4]
        train_labels = train_labels[:len(test_data) * 4]

    # # if reading texts
    # # create a tokenizer
    # tokenizer = Tokenizer()
    # tokenizer.fit_on_texts(train_data)
    # word_index = tokenizer.word_index
    # vocab_size = len(word_index) + 1
    #
    # # create training sequences
    # train_sequences = tokenizer.texts_to_sequences(train_data)
    # train_padded = pad_sequences(train_sequences, maxlen=8, padding='post')
    #
    # # create test sequences
    # test_sequences = tokenizer.texts_to_sequences(test_data)
    # test_padded = pad_sequences(test_sequences, maxlen=8, padding='post')
    #
    # # create the model
    # model = Sequential()
    # model.add(Flatten(input_shape=(train_padded.shape[1],)))
    # model.add(Dense(16, activation='relu'))
    # model.add(Dense(1, activation='sigmoid'))
    # model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
    #
    # # train the model
    # # to array
    # train_padded = np.asarray(train_padded)
    # train_labels = np.asarray(train_labels)
    # test_padded = np.asarray(test_padded)
    # test_labels = np.asarray(test_labels)
    # model.fit(train_padded, train_labels, epochs=45, verbose=2, validation_data=(test_padded, test_labels), batch_size=6)


    # if reading images
    # create the model
    model = Sequential()
    model.add(Conv2D(32, (3, 3), activation='relu', input_shape=(48, 48, 1)))
    model.add(MaxPooling2D((2, 2)))
    model.add(Conv2D(64, (3, 3), activation='relu'))
    model.add(Flatten())
    model.add(Dense(64, activation='relu'))
    model.add(Dense(2, activation='softmax'))
    model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])

    # reshape data

    # train the model
    # to array
    train_data = np.asarray(train_data)
    train_labels = np.asarray(train_labels)
    test_data = np.asarray(test_data)
    test_labels = np.asarray(test_labels)

    train_labels = to_categorical(train_labels)
    test_labels = to_categorical(test_labels)

    model.fit(train_data, train_labels, epochs=45, verbose=2, validation_data=(test_data, test_labels), batch_size=6)

    # evaluate the model
    loss, accuracy = model.evaluate(test_data, test_labels, verbose=2)
    print('Accuracy: %f' % (accuracy * 100))

    # make a prediction
    predictions = model.predict(test_data)

    # plot the results as a confusion matrix
    cm = confusion_matrix(test_labels.argmax(axis=1), predictions.argmax(axis=1))
    plotConfusionMatrix(cm, ['happy', 'sad'], 'Confusion Matrix for Happy and Sad Emojis')
