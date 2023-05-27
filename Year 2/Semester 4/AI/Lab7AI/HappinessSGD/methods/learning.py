from SGD import sgdRegression
from SGD.batchGDRegression import BatchGDRegression
from SGD.sgdRegression import MySGDRegression


def learnModel(trainInputs, trainOutputs, uniOrMulti):
    # model initialisation
    regressor = BatchGDRegression()

    regressor.fit(trainInputs, trainOutputs)
    # print(regressor.coef_)
    # print(regressor.intercept_)

    # parameters of the liniar regressor
    if uniOrMulti == 0:
        w0, w1 = regressor.intercept_, regressor.coef_[0]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x')
        return w0, w1, regressor
    else:
        w0, w1, w2 = regressor.intercept_, regressor.coef_[0], regressor.coef_[1]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x1 + ', w2, ' * x2')
        return w0, w1, w2, regressor


def toolLearning(trainInputs, trainOutputs, uniOrMulti):
    from sklearn import linear_model
    regressor = linear_model.SGDRegressor()
    regressor.fit(trainInputs, trainOutputs)
    # print(regressor.coef_)
    # print(regressor.intercept_)

    # parameters of the liniar regressor
    if uniOrMulti == 0:
        w0, w1 = regressor.intercept_[0], regressor.coef_[0]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x')
        return w0, w1, regressor
    else:
        w0, w1, w2 = regressor.intercept_[0], regressor.coef_[0], regressor.coef_[1]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x1 + ', w2, ' * x2' )
        return w0, w1, w2, regressor


def toolLearningAdapted(trainInputs, trainOutputs, uniOrMulti):
    from sklearn import linear_model
    regressor = linear_model.SGDRegressor(max_iter=1000, alpha=0.0001, shuffle=False, penalty=None, loss='squared_error')
    regressor.fit(trainInputs, trainOutputs)
    # print(regressor.coef_)
    # print(regressor.intercept_)

    # parameters of the liniar regressor
    if uniOrMulti == 0:
        w0, w1 = regressor.intercept_[0], regressor.coef_[0]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x')
        return w0, w1, regressor
    else:
        w0, w1, w2 = regressor.intercept_[0], regressor.coef_[0], regressor.coef_[1]
        print('the learnt model: f(x) = ', w0, ' + ', w1, ' * x1 + ', w2, ' * x2' )
        return w0, w1, w2, regressor