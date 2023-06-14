from math import exp
import numpy as np


def sigmoid(x):
    return 1 / (1 + exp(-x))


class OneLogisticRegression:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []

    def fit(self, x, y, learningRate=0.008, noEpochs=1000):
        self.coef_ = [0.0 for _ in range(1 + len(x[0]))]  # beta or w coefficients y = w0 + w1 * x1 + w2 * x2 + ...
        for epoch in range(noEpochs):
            # TBA: shuffle the trainind examples in order to prevent cycles
            for i in range(len(x)):  # for each sample from the training data
                ycomputed = sigmoid(self.eval(x[i], self.coef_))  # estimate the output
                crtError = ycomputed - y[i]  # compute the error for the current sample
                for j in range(0, len(x[0])):  # update the coefficients
                    self.coef_[j + 1] = self.coef_[j + 1] - learningRate * crtError * x[i][j]
                self.coef_[0] = self.coef_[0] - learningRate * crtError * 1

        self.intercept_ = self.coef_[0]
        self.coef_ = self.coef_[1:]

    def eval(self, xi, coef):
        yi = coef[0]
        for j in range(len(xi)):
            yi += coef[j + 1] * xi[j]
        return yi

    def predictOneSample(self, features):
        threshold = 0.5
        coef = [self.intercept_] + [c for c in self.coef_]
        yi = sigmoid(self.eval(features, coef))
        label = 0 if yi < threshold else 1
        return label

    def predict(self, inputs):
        computed = [self.predictOneSample(sample) for sample in inputs]
        return computed


class MyLogisticRegression:
    def __init__(self):
        self.intercept_ = [0.0]
        self.coef_ = []

    # use the gradient descent method
    # simple stochastic GD
    def fit(self, x, y, classes):
        self.intercept_ = []
        self.coef_ = []
        # mark classes
        for classLabel in classes:
            yi = [int(val == classLabel) for val in y]
            classifier = OneLogisticRegression()
            classifier.fit(x, yi)

            self.intercept_.append(classifier.intercept_)
            self.coef_.append(classifier.coef_)

    def eval(self, xi, coef):
        yi = coef[0]
        for j in range(len(xi)):
            yi += coef[j + 1] * xi[j]
        return yi

    def predict(self, inTest, classes):
        predictions = []
        for sample in inTest:
            predictionsForClasses = []
            for i in range(len(classes)):
                coefficients = [self.intercept_[i]]
                coefficients = coefficients + self.coef_[i]
                yi = self.eval(sample, coefficients)
                predictionsForClasses.append(sigmoid(yi))
            predictions.append(classes[np.argmax(predictionsForClasses)])
        return predictions

