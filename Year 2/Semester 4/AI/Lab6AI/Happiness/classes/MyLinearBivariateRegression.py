class MyLinearBivariateRegression:
    # y = w0 + w1 * x1 + w2 * x2
    def __init__(self):
        self.w0 = 0.0
        self.w1 = 0.0
        self.w2 = 0.0

    # learn a linear bivariate regression model by using training inputs (x) and outputs (y)
    def fit(self, X1, X2, Y):
        # Observation:
        # yi = Yi - Y'
        # x1i = X1i - X1'
        # x2i = X2i - X2'

        n = len(X1)
        # Y' = sY / n
        sY = sum(Y)
        yi = [Y[i] - sY / n for i in range(n)]

        # X1' = sX1 / n
        sX1 = sum(X1)
        x1i = [X1[i] - sX1 / n for i in range(n)]

        # X2' = sX2 / n
        sX2 = sum(X2)
        x2i = [X2[i] - sX2 / n for i in range(n)]

        sx1y = sum([i * j for i, j in zip(x1i, yi)])
        sx2x2 = sum([i * i for i in x2i])
        sx1x2 = sum([i * j for i, j in zip(x1i, x2i)])
        sx1x1 = sum([i * i for i in x1i])
        sx2y = sum([i * j for i, j in zip(x2i, yi)])

        self.w1 = (sx1y * sx2x2 - sx2y * sx1x2) / (sx1x1 * sx2x2 - sx1x2 ** 2)
        self.w2 = (sx2y * sx1x1 - sx1y * sx1x2) / (sx1x1 * sx2x2 - sx1x2 ** 2)
        self.w0 = sY / n - self.w1 * sX1 / n - self.w2 * sX2 / n

    # predict the outputs for some new inputs (by using the learnt model)
    def predict(self, X):
        return [self.w0 + self.w1 * x1 + self.w2 * x2 for x1, x2 in X]


