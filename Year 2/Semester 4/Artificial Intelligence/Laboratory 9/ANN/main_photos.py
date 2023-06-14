import cv2
import numpy as np
from sklearn.neural_network import MLPClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import cross_val_score
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
from tensorflow import keras
from tensorflow.keras import layers

from methods.accuracy import evalMultiClass
from methods.ploting import plotConfusionMatrix


def readImages(path):
    import os
    no_filter = path + '/no_filter'
    with_filter = path + '/with_filter'
    labels = [0 for _ in os.listdir(no_filter)] + [1 for _ in os.listdir(with_filter)]
    images = []
    for filename in os.listdir(no_filter):
        image = cv2.imread(os.path.join(no_filter, filename))
        resized = cv2.resize(image, (30, 40))
        images.append(resized)
    for filename in os.listdir(with_filter):
        image = cv2.imread(os.path.join(with_filter, filename))
        resized = cv2.resize(image, (30, 40))
        images.append(resized)
    return images, labels


def flatten(image):
    return image.flatten()


if __name__ == '__main__':
    inputs, outputs = readImages("C:/GIT/ai-lab09-MartinFabianIonut/ANN2/photos")

    trainInputs, testInputs, trainOutputs, testOutputs = train_test_split(inputs, outputs, test_size=0.2, random_state=5)

    trainInputsFlatten = [flatten(el) for el in trainInputs]
    testInputsFlatten = [flatten(el) for el in testInputs]

    scaler = StandardScaler()
    scaler.fit(trainInputsFlatten)
    trainInputsNormalized = scaler.transform(trainInputsFlatten)
    testInputsNormalized = scaler.transform(testInputsFlatten)

    print(len(trainInputsNormalized))
    print(len(trainOutputs))

    # acronyms: MLP - Multi-Layer Perceptron, SGD - Stochastic Gradient Descent
    # ANN classifier, 2 hidden layers, 100 neurons each, 300 iterations, learning rate 0.001, SGD solver
    mlp = MLPClassifier(hidden_layer_sizes=(100, 100), max_iter=300, alpha=0.0001,
                        solver='sgd', verbose=10, random_state=5,
                        learning_rate_init=0.001)

    # cross validation because the dataset is small
    scores = cross_val_score(mlp, trainInputsNormalized, trainOutputs, cv=3, scoring='accuracy')

    mlp.fit(trainInputsNormalized, trainOutputs)
    predictedLabels = mlp.predict(testInputsNormalized)

    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), predictedLabels, ['normal', 'sepia'])
    plotConfusionMatrix(cm, ['normal', 'sepia'], "sepia classification")

    print('acc: ', acc)
    print('precision: ', prec)
    print('recall: ', recall)

    # CNN
    # convert to numpy arrays
    testInputs = np.array(testInputs)
    testOutputs = np.array(testOutputs)

    trainInputs = np.array(trainInputs)
    trainOutputs = np.array(trainOutputs)

    train_ratio = 0.8
    num_train_samples = int(len(trainInputs) * train_ratio)

    train_indices = np.random.choice(len(trainInputs), num_train_samples, replace=False)
    # the rest of the data will be used for validation
    validation_indices = np.array(list(set(range(len(trainInputs))) - set(train_indices)))

    train_data = trainInputs[train_indices]
    train_outputs = trainOutputs[train_indices]
    validation_data = trainInputs[validation_indices]
    validation_outputs = trainOutputs[validation_indices]

    model = keras.Sequential([
        # 3 convolutional layers with relu activation function and 3 max pooling layers ...
        # pooling layers reduce the dimensionality of the data
        layers.Conv2D(32, (3, 3), activation='relu', input_shape=(40, 30, 3)),
        layers.Conv2D(64, (3, 3), activation='relu'),
        layers.MaxPooling2D((2, 2)),
        layers.Conv2D(128, (3, 3), activation='relu'),
        layers.MaxPooling2D((2, 2)),
        layers.Flatten(),
        layers.Dense(64, activation='relu'),
        layers.Dense(1, activation='sigmoid')
    ])

    # compile the model with adam optimizer, binary crossentropy loss function and accuracy metric
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

    # train the model with batch_size = 11 (the number of training samples)
    history = model.fit(train_data, train_outputs, epochs=100, batch_size=11,
                        validation_data=(validation_data, validation_outputs))

    # evaluate the model on the test data, verbose=3 means that the evaluation will be displayed
    test_loss, test_acc = model.evaluate(testInputs, testOutputs, verbose=2)

    print('Test accuracy:', test_acc)

    pred_labels = model.predict(testInputs)
    # round the predictions to 0 or 1 because the output is a probability between 0 and 1 (sigmoid activation function)
    pred_labels = np.round(pred_labels).flatten()

    plotConfusionMatrix(confusion_matrix(testOutputs, pred_labels), ['normal', 'sepia'], "sepia classification")


