import matplotlib.pyplot as plt
import numpy as np
from sklearn import linear_model


from classes.MyLinearUnivariateRegression import MyLinearUnivariateRegression


def plotTrainingValidation(inputsPlot, outputsPlot):
    np.random.seed(5)
    indexes = [i for i in range(len(inputsPlot))]
    trainSample = np.random.choice(indexes, int(0.8 * len(inputsPlot)), replace=False)
    validationSample = [i for i in indexes if not i in trainSample]

    trainInputs = [inputsPlot[i] for i in trainSample]
    trainOutputs = [outputsPlot[i] for i in trainSample]

    validationInputs = [inputsPlot[i] for i in validationSample]
    validationOutputs = [outputsPlot[i] for i in validationSample]

    plt.plot(trainInputs, trainOutputs, 'ro', label='training data')  # train data are plotted by red and circle sign
    plt.plot(validationInputs, validationOutputs, 'g^',
             label='validation data')  # test data are plotted by green and a triangle sign
    plt.title('train and validation data')
    plt.xlabel('GDP capita')
    plt.ylabel('happiness')
    plt.legend()
    plt.show()
    return trainInputs, trainOutputs, validationInputs, validationOutputs


def plotLinearModel(trainInputs, trainOutputs):
    xx = [[el] for el in trainInputs]

    # regressor = linear_model.LinearRegression()
    # regressor.fit(xx, trainOutputs)
    # w0, w1 = regressor.intercept_, regressor.coef_[0]

    regressor = MyLinearUnivariateRegression()
    regressor.fit(trainInputs, trainOutputs)
    w0, w1 = regressor.intercept_, regressor.coef_

    noOfPoints = 1000
    xref = []
    val = min(trainInputs)
    step = (max(trainInputs) - min(trainInputs)) / noOfPoints
    for i in range(1, noOfPoints):
        xref.append(val)
        val += step
    yref = [w0 + w1 * el for el in xref]
    plt.plot(trainInputs, trainOutputs, 'ro', label='training data')  # train data are plotted by red and circle sign
    plt.plot(xref, yref, 'b-', label='learnt model')  # model is plotted by a blue line
    plt.title('train data and the learnt model')
    plt.xlabel('GDP capita')
    plt.ylabel('happiness')
    plt.legend()
    plt.show()
    return regressor, w0, w1


def makePrediction(validationInputs, validationOutputs, regressor):
    computedValidationOutputs = regressor.predict([[x] for x in validationInputs])
    # compute the differences between the predictions and real classes
    error = 0.0
    for t1, t2 in zip(computedValidationOutputs, validationOutputs):
        error += (t1 - t2) ** 2
    error = error / len(validationOutputs)
    # plot the computed classes (see how far they are from the real classes)
    plt.plot(validationInputs, computedValidationOutputs, 'yo',
             label='computed test data')  # computed test data are plotted yellow red and circle sign
    plt.plot(validationInputs, validationOutputs, 'g^',
             label='real test data')  # real test data are plotted by green triangles
    plt.title('computed validation and real validation data')
    plt.xlabel('GDP capita')
    plt.ylabel('happiness')
    plt.legend()
    plt.show()
    return error
