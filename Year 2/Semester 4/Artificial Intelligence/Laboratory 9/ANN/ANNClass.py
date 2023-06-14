import numpy as np
from sklearn.preprocessing import LabelBinarizer


def softmax(X):
    exp_x = np.exp(X - np.max(X))
    return exp_x / np.sum(exp_x, axis=1, keepdims=True)


def sigmoid(X):
    return 1 / (1 + np.exp(-X))


class ANN:
    def __init__(self, input_size, hidden_size, output_size):
        self.hidden = None
        self.output_activation = None
        self.output = None
        self.hidden_activation = None
        self.input_size = input_size
        self.hidden_size = hidden_size
        self.output_size = output_size
        self.W1 = np.random.randn(self.input_size, self.hidden_size)
        self.W2 = np.random.randn(self.hidden_size, self.output_size)

    def forward(self, X):
        self.hidden = np.dot(X, self.W1)
        self.hidden_activation = sigmoid(self.hidden)
        self.output = np.dot(self.hidden_activation, self.W2)
        self.output_activation = softmax(self.output)
        return self.output_activation

    def backward(self, X, y, output, learningRate):
        error = output - y
        dW2 = np.dot(self.hidden_activation.T, error)
        dW1 = np.dot(X.T, np.dot(error, self.W2.T) * self.hidden_activation * (1 - self.hidden_activation))
        self.W1 -= learningRate * dW1
        self.W2 -= learningRate * dW2

    def loss(self, y, output):
        eps = 1e-9
        output = np.clip(output, eps, 1 - eps)
        return -np.mean(np.sum(y * np.log(output), axis=1))

    def train(self, X, y, lr, epochs):
        lb = LabelBinarizer()
        y = lb.fit_transform(y)
        for i in range(epochs):
            output = self.forward(X)
            self.backward(X, y, output, lr)
            loss = self.loss(y, output)
            if i % 50 == 0:
                print(f'Iteration {i}: loss {loss}')

    def predict(self, X):
        return np.argmax(self.forward(X), axis=1)
