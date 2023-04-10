import matplotlib.pyplot as plt
import numpy as np
from sklearn import linear_model


from classes.MyLinearBivariateRegression import MyLinearBivariateRegression


def plotTrainingValidationBivariate(inputsGDP, inputsFreedom, outputsPlot):
    np.random.seed(5)
    indexes = [i for i in range(len(inputsGDP))]
    trainSample = np.random.choice(indexes, int(0.8 * len(inputsGDP)), replace=False)
    validationSample = [i for i in indexes if not i in trainSample]

    feature1train = [inputsGDP[i] for i in trainSample]
    feature2train = [inputsFreedom[i] for i in trainSample]
    trainOutputs = [outputsPlot[i] for i in trainSample]

    feature1validation = [inputsGDP[i] for i in validationSample]
    feature2validation = [inputsFreedom[i] for i in validationSample]
    validationOutputs = [outputsPlot[i] for i in validationSample]

    trainInputs = [[feature1train[i], feature2train[i]] for i in range(len(feature1train))]
    validationInputs = [[feature1validation[i], feature2validation[i]] for i in range(len(feature1validation))]

    plot3Ddata(x1Train=feature1train,
               x2Train=feature2train,
               yTrain=trainOutputs,
               x1Test=feature1validation,
               x2Test=feature2validation,
               yTest=validationOutputs,
               title='train and validation data')
    return feature1train, feature2train, trainInputs, trainOutputs, feature1validation, feature2validation, \
           validationInputs, validationOutputs


def plot3Ddata(x1Train, x2Train, yTrain, x1Model=None, x2Model=None, yModel=None, x1Test=None, x2Test=None, yTest=None,
               title=None):
    ax = plt.axes(projection='3d')
    if x1Train:
        plt.scatter(x1Train, x2Train, yTrain, c='r', marker='o', label='train data')
    if x1Model:
        plt.scatter(x1Model, x2Model, yModel, c='b', marker='_', label='learnt model')
    if x1Test:
        plt.scatter(x1Test, x2Test, yTest, c='g', marker='^', label='validation data')
    plt.title(title)
    ax.set_xlabel("capita")
    ax.set_ylabel("freedom")
    ax.set_zlabel("happiness")
    plt.legend()
    plt.show()


def plotModelBivariate(trainInputs, trainOutputs, feature1, feature2, feature1Train, feature2Train):
    # model initialisation
    # regressor = linear_model.LinearRegression()
    # regressor.fit(trainInputs, trainOutputs)
    # w0, w1, w2 = regressor.intercept_, regressor.coef_[0], regressor.coef_[1]

    regressor = MyLinearBivariateRegression()
    regressor.fit(feature1Train, feature2Train, trainOutputs)
    w0, w1, w2 = regressor.w0, regressor.w1, regressor.w2

    noOfPoints = 50
    xref1 = []
    val = min(feature1)
    step1 = (max(feature1) - min(feature1)) / noOfPoints
    for _ in range(1, noOfPoints):
        for _ in range(1, noOfPoints):
            xref1.append(val)
        val += step1

    xref2 = []
    val = min(feature2)
    step2 = (max(feature2) - min(feature2)) / noOfPoints
    for _ in range(1, noOfPoints):
        aux = val
        for _ in range(1, noOfPoints):
            xref2.append(aux)
            aux += step2
    yref = [w0 + w1 * el1 + w2 * el2 for el1, el2 in zip(xref1, xref2)]
    plot3Ddata(feature1Train, feature2Train, trainOutputs, xref1, xref2, yref, [], [], [],
               'train data and the learnt model')
    return regressor, xref1, xref2, yref, w0, w1, w2


def makePredictionBivariate(validationInputs, validationOutputs, regressor, feature1Validation, feature2Validation):
    # computedTestOutputs = [w0 + w1 * el[0] + w2 * el[1] for el in validationInputs]
    # makes predictions for test data (by tool)
    computedTestOutputs = regressor.predict(validationInputs)

    plot3Ddata([], [], [], feature1Validation, feature2Validation, computedTestOutputs, feature1Validation,
               feature2Validation, validationOutputs,
               'predictions vs real test data')
    # compute the differences between the predictions and real classes
    error = 0.0
    for t1, t2 in zip(computedTestOutputs, validationOutputs):
        error += (t1 - t2) ** 2
    error = error / len(validationOutputs)
    return error
