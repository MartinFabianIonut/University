import numpy as np
from sklearn.datasets import load_iris
from sklearn.datasets import load_digits

from methods.accuracy import accuracy, evalMultiClass
from methods.learning import learnModel, learnModelDigits
from methods.ploting import plot, plotNormalisedData, plotPredictions, plotConfusionMatrix, plotFlattenNormalisedData

if __name__ == '__main__':
    data = load_iris()
    inputs = data['data']
    outputs = data['target']
    outputNames = data['target_names']
    featureNames = list(data['feature_names'])
    feature1 = [feat[featureNames.index('sepal length (cm)')] for feat in inputs]
    feature2 = [feat[featureNames.index('petal length (cm)')] for feat in inputs]
    inputs = [[feat[featureNames.index('sepal length (cm)')], feat[featureNames.index('petal length (cm)')]] for feat in inputs]
    plot(inputs, outputs, feature1, feature2, outputNames)
    inputs, outputs, feature1test, feature2test, testInputs, testOutputs = \
        plotNormalisedData(inputs, outputs, outputNames)
    classes = [0, 1, 2]

    classifier = learnModel(inputs, outputs)

    computedTestOutputs = classifier.predict(testInputs)

    computedClasses = [classes[pred] for pred in computedTestOutputs]

    plotPredictions(outputs, feature1test, feature2test, testOutputs, computedTestOutputs,
                    "real test data", outputNames)
    accuracy(computedTestOutputs, testOutputs)
    acc, precision, recall, cm = evalMultiClass(np.array(testOutputs), computedTestOutputs, outputNames)

    plotConfusionMatrix(cm, outputNames, "iris classification")

    print('acc: ', acc)
    print('precision: ', precision)
    print('recall: ', recall)

    data = load_digits()
    inputs = data.images
    outputs = data['target']
    outputNames = data['target_names']

    noData = len(inputs)
    permutation = np.random.permutation(noData)
    inputs = inputs[permutation]
    outputs = outputs[permutation]

    inputs, outputs, testInputs, testOutputs = plotFlattenNormalisedData(inputs, outputs, outputNames)
    classifier = learnModelDigits(inputs, outputs)

    computedTestOutputs = classifier.predict(testInputs)

    acc, precision, recall, cm = evalMultiClass(np.array(testOutputs), computedTestOutputs, outputNames)

    plotConfusionMatrix(cm, outputNames, "digits classification")

    print('acc: ', acc)
    print('precision: ', precision)
    print('recall: ', recall)


