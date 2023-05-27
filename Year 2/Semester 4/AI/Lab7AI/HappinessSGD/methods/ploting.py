import matplotlib.pyplot as plt
import numpy as np

from methods.normalisation import normalisation, statisticalNormalisation


def plot3Ddata(x1Train, x2Train, yTrain, x1Model=None, x2Model=None, yModel=None, x1Test=None, x2Test=None, yTest=None,
               title=None):
    ax = plt.axes(projection='3d')
    if x1Train:
        plt.scatter(x1Train, x2Train, yTrain, c='r', marker='o', label='train data')
    if x1Model:
        plt.scatter(x1Model, x2Model, yModel, c='b', marker='_', label='learnt model')
    if x1Test:
        plt.scatter(x1Test, x2Test, yTest, c='g', marker='^', label='test data')
    plt.title(title)
    ax.set_xlabel("capita")
    ax.set_ylabel("freedom")
    ax.set_zlabel("happiness")
    plt.show()


def plotTrainingTestBivariate(inputsGDP, inputsFreedom, outputsPlot):
    np.random.seed(5)
    indexes = [i for i in range(len(inputsGDP))]
    trainSample = np.random.choice(indexes, int(0.8 * len(inputsGDP)), replace=False)
    testSample = [i for i in indexes if not i in trainSample]

    trainInputs = [[inputsGDP[i], inputsFreedom[i]] for i in trainSample]
    trainOutputs = [outputsPlot[i] for i in trainSample]
    testInputs = [[inputsGDP[i], inputsFreedom[i]] for i in testSample]
    testOutputs = [outputsPlot[i] for i in testSample]

    # trainInputs, testInputs = normalisation(trainInputs, testInputs)
    # trainOutputs, testOutputs = normalisation(trainOutputs, testOutputs)

    feature1Train = [feature[0] for feature in trainInputs]
    feature2Train = [feature[1] for feature in trainInputs]

    feature1Test = [feature[0] for feature in testInputs]
    feature2Test = [feature[1] for feature in testInputs]

    feature1Test = list(feature1Test)
    feature2Test = list(feature2Test)

    feature1Train, meanValue1, stdValue1 = statisticalNormalisation(feature1Train)
    feature2Train, meanValue2, stdValue2 = statisticalNormalisation(feature2Train)
    feature1Test = statisticalNormalisation(feature1Test, meanValue1, stdValue1)
    feature2Test = statisticalNormalisation(feature2Test, meanValue2, stdValue2)
    trainOutputs, meanValue3, stdValue3 = statisticalNormalisation(trainOutputs)
    testOutputs = statisticalNormalisation(testOutputs, meanValue3, stdValue3)

    plot3Ddata(x1Train=feature1Train,
               x2Train=feature2Train,
               yTrain=trainOutputs,
               x1Test=feature1Test,
               x2Test=feature2Test,
               yTest=testOutputs,
               title='train and test data')
    return feature1Train, feature2Train, trainInputs, trainOutputs, feature1Test, feature2Test, \
           testInputs, testOutputs


def plotTrainingTestUnivariate(inputsPlot, outputsPlot):
    np.random.seed(5)
    indexes = [i for i in range(len(inputsPlot))]
    trainSample = np.random.choice(indexes, int(0.8 * len(inputsPlot)), replace=False)
    testSample = [i for i in indexes if not i in trainSample]

    trainInputs = [inputsPlot[i] for i in trainSample]
    trainOutputs = [outputsPlot[i] for i in trainSample]

    testInputs = [inputsPlot[i] for i in testSample]
    testOutputs = [outputsPlot[i] for i in testSample]

    trainInputs, meanValue1, stdValue1 = statisticalNormalisation(trainInputs)
    testInputs = statisticalNormalisation(testInputs, meanValue1, stdValue1)
    trainOutputs, meanValue2, stdValue2 = statisticalNormalisation(trainOutputs)
    testOutputs = statisticalNormalisation(testOutputs, meanValue2, stdValue2)

    plt.plot(trainInputs, trainOutputs, 'ro', label='training data')  # train data are plotted by red and circle sign
    plt.plot(testInputs, testOutputs, 'g^',
             label='validation data')  # test data are plotted by green and a triangle sign
    plt.title('train and validation data')
    plt.xlabel('GDP capita')
    plt.ylabel('happiness')
    plt.legend()
    plt.show()
    return trainInputs, trainOutputs, testInputs, testOutputs


def plotModelBivariate(w0, w1, w2, trainOutputs, feature1, feature2, feature1Train, feature2Train):
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
    return xref1, xref2, yref


def plotModelUnivariate(w0, w1, trainOutputs, trainInputs):
    # plot the model
    xref = []
    noOfPoints = 1000
    val = min(trainInputs)
    step = (max(trainInputs) - min(trainInputs)) / noOfPoints
    for _ in range(1, noOfPoints):
        xref.append(val)
        val += step
    yref = [w0 + w1 * el for el in xref]
    plt.plot(trainInputs, trainOutputs, 'ro', label='training data')  # train data are plotted by red and circle sign
    plt.plot(xref, yref, 'b-', label='learnt model')  # the model is plotted by blue and line sign
    plt.title('train data and the learnt model')
    plt.xlabel('GDP capita')
    plt.ylabel('happiness')
    plt.legend()
    plt.show()
    return xref, yref


def makePredictionBivariate(testInputs, testOutputs, regressor, feature1Test, feature2Test):
    # computedTestOutputs = [w0 + w1 * el[0] + w2 * el[1] for el in validationInputs]
    # makes predictions for test data (by tool)
    computedTestOutputs = regressor.predict(testInputs)

    plot3Ddata([], [], [], feature1Test, feature2Test, computedTestOutputs, feature1Test,
               feature2Test, testOutputs,
               'predictions vs real test data')
    # compute the differences between the predictions and real classes
    error = 0.0
    for t1, t2 in zip(computedTestOutputs, testOutputs):
        error += (t1 - t2) ** 2
    error = error / len(testOutputs)
    return error


def makePredictionUnivariate(testInputs, testOutputs, regressor):
    # computedTestOutputs = [w0 + w1 * el for el in validationInputs]
    # makes predictions for test data (by tool)
    computedTestOutputs = regressor.predict(testInputs)

    plt.plot(testInputs, testOutputs, 'ro', label='real test data')  # real test data are plotted by red and circle sign
    plt.plot(testInputs, computedTestOutputs, 'b^',
             label='computed test data')  # computed test data are plotted by blue and triangle sign
    plt.title('real test data vs. computed test data')
    plt.xlabel('GDP capita')
    plt.ylabel('happiness')
    plt.legend()
    plt.show()
    # compute the differences between the predictions and real classes
    error = 0.0
    for t1, t2 in zip(computedTestOutputs, testOutputs):
        error += (t1 - t2) ** 2
    error = error / len(testOutputs)
    return error


def plotOptional(testOutputs, predict):
    plt.scatter(testOutputs, predict)
    plt.plot([testOutputs.min(), testOutputs.max()], [predict.min(), predict.max()], color='red')
    plt.xlabel('Real')
    plt.ylabel('Predicted')
    plt.title('Learnt model')
    plt.show()
