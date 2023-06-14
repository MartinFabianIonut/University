import random


class BatchGDRegression:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []

    # simple batch GD
    def fit(self, x, y, learningRate=0.0003, noEpochs=1000, batchSize=10):
        self.coef_ = [0.0 for _ in range(len(x[0]) + 1)]  # w0,w1,w2
        noBatches = len(x) // batchSize

        for _ in range(noEpochs): # for each epoch
            indexes = random.sample(range(len(x)), len(x))
            x_shuffle = [x[i] for i in indexes]
            y_shuffle = [y[i] for i in indexes]
            for i in range(noBatches):
                start = i * batchSize
                end = (i + 1) * batchSize

                batch_x = [x_shuffle[i] for i in range(start, end)]  # features for each batch
                batch_y = [y_shuffle[i] for i in range(start, end)]  # true y for each feature

                ycomputed = [self.eval(xi) for xi in batch_x]  # predicted y for each feature
                errors = [ycomputed[i] - batch_y[i] for i in range(len(batch_x))]  # error for each feature (gdp,freedom)

                for j in range(0, len(x[0])):  # update coefficients iterating through for each feature
                    grad = sum(
                        [errors[i] * batch_x[i][j] for i in range(len(batch_y))])  # mean the gradient for each feature
                    self.coef_[j] -= learningRate * grad / len(batch_y)

                self.coef_[-1] -= learningRate * sum(errors) / len(batch_y)

        self.intercept_ = self.coef_[-1]
        self.coef_ = self.coef_[:-1]

    def eval(self, xi):
        yi = self.coef_[-1]
        for j in range(len(xi)):
            yi += self.coef_[j] * xi[j]
        return yi

    def predict(self, x):
        yComputed = [self.eval(xi) for xi in x]
        return yComputed