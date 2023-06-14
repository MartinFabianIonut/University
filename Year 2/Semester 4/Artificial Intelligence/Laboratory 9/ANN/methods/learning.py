import numpy as np
from ANNClass import ANN


def learnModel(trainInputs, trainOutputs):
    # using developed code
    # model initialisation
    classifier = ANN(input_size=2, hidden_size=2 * 4, output_size=3)
    trainInputs = np.array(trainInputs)
    trainOutputs = np.array(trainOutputs)
    classifier.train(trainInputs, trainOutputs, 0.01, 1000)
    return classifier


def learnModelDigits(trainInputs, trainOutputs):
    # using developed code
    # model initialisation
    classifier = ANN(input_size=64, hidden_size=2*64, output_size=10)
    classifier.train(trainInputs, trainOutputs, 0.01, 1000)
    return classifier
