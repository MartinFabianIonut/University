import glob
import cv2
import numpy as np
from sklearn.metrics import accuracy_score, confusion_matrix

from imageClassifier import ImageClassifier, ImageClassifierFacenet
from methods.ploting import plotConfusionMatrix


def getTrainData(filePath, train_data, train_labels, label):
    image_paths = glob.glob(filePath)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:80]
    image_paths = [image_paths[i] for i in indices]
    for image_path in image_paths:
        image = cv2.imread(image_path)
        train_data.append(image)
        train_labels.append(label)
    return train_data, train_labels


def getTestData(filePath, test_data, test_labels, label):
    image_paths = glob.glob(filePath)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:20]
    image_paths = [image_paths[i] for i in indices]
    for image_path in image_paths:
        image = cv2.imread(image_path)
        test_data.append(image)
        test_labels.append(label)
    return test_data, test_labels


if __name__ == '__main__':
    train_data = []
    test_data = []
    train_labels = []
    test_labels = []

    train_data, train_labels = getTrainData('facial_emotions/train/angry/*.jpg', train_data, train_labels, 0)
    # train_data2, train_labels2 = getTrainData('facial_emotions/train/disgust/*.jpg',train_data,train_labels,1)
    # train_data += train_data2
    # train_labels += train_labels2
    # train_data2, train_labels2 = getTrainData('facial_emotions/train/fear/*.jpg',train_data,train_labels,2)
    # train_data += train_data2
    # train_labels += train_labels2
    train_data2, train_labels2 = getTrainData('facial_emotions/train/happy/*.jpg', train_data, train_labels, 3)
    train_data += train_data2
    train_labels += train_labels2
    # train_data2, train_labels2 = getTrainData('facial_emotions/train/neutral/*.jpg', train_data, train_labels, 4)
    # train_data += train_data2
    # train_labels += train_labels2
    # train_data2, train_labels2 = getTrainData('facial_emotions/train/sad/*.jpg',train_data,train_labels,5)
    # train_data += train_data2
    # train_labels += train_labels2
    train_data2, train_labels2 = getTrainData('facial_emotions/train/surprise/*.jpg', train_data, train_labels, 6)
    train_data += train_data2
    train_labels += train_labels2

    test_data, test_labels = getTestData('facial_emotions/test/angry/*.jpg', test_data, test_labels, 0)
    # test_data2, test_labels2 = getTestData('facial_emotions/test/disgust/*.jpg',test_data,test_labels,1)
    # test_data += test_data2
    # test_labels += test_labels2
    # test_data2, test_labels2 = getTestData('facial_emotions/test/fear/*.jpg',test_data,test_labels,2)
    # test_data += test_data2
    # test_labels += test_labels2
    test_data2, test_labels2 = getTestData('facial_emotions/test/happy/*.jpg', test_data, test_labels, 3)
    test_data += test_data2
    test_labels += test_labels2
    # test_data2, test_labels2 = getTestData('facial_emotions/test/neutral/*.jpg', test_data, test_labels, 4)
    # test_data += test_data2
    # test_labels += test_labels2
    # test_data2, test_labels2 = getTestData('facial_emotions/test/sad/*.jpg',test_data,test_labels,5)
    # test_data += test_data2
    # test_labels += test_labels2
    test_data2, test_labels2 = getTestData('facial_emotions/test/surprise/*.jpg', test_data, test_labels, 6)
    test_data += test_data2
    test_labels += test_labels2

    classifier = ImageClassifier()
    classifier.fit(train_data, train_labels)

    predictions = classifier.predict(test_data)
    accuracy = accuracy_score(test_labels, predictions)

    print("Accuracy for ImageClassifier:", accuracy)

    plotConfusionMatrix(confusion_matrix(test_labels, predictions),
                        ['Anger', 'Happiness', 'Surprise'],
                        '- Emotion Confusion Matrix 2')

    classifier = ImageClassifierFacenet()
    classifier.fit(train_data, train_labels)
    predictions = classifier.predict(test_data)
    accuracy = accuracy_score(test_labels, predictions)

    print("Accuracy for ImageClassifierFacenet:", accuracy)

    plotConfusionMatrix(confusion_matrix(test_labels, predictions),
                        ['Anger', 'Happiness', 'Surprise'],
                        '- Emotion Confusion Matrix 3')
