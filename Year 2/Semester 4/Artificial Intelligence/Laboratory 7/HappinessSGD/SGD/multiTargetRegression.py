import numpy as np


class MultiTargetRegressor:
    def __init__(self):
        self.weights = None

    def fit(self, X, y):
        n_samples, n_features = X.shape
        X = np.concatenate((np.ones((n_samples, 1)), X), axis=1)
        self.weights = np.linalg.pinv(X) @ y

    def predict(self, X):
        n_samples = X.shape[0]
        X = np.concatenate((np.ones((n_samples, 1)), X), axis=1)
        return X @ self.weights