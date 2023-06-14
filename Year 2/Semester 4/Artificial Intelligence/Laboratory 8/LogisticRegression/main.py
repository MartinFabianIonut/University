import numpy as np
from sklearn import linear_model
from sklearn.datasets import load_breast_cancer
from sklearn.datasets import load_iris

from methods.accuracy import accuracy, evalMultiClass
from methods.learning import learnModel
from methods.ploting import plot, plotNormalisedData, plotPredictions, plotConfusionMatrix

if __name__ == '__main__':
    # data = load_breast_cancer()
    data = load_iris()
    inputs = data['data']
    outputs = data['target']
    outputNames = data['target_names']
    featureNames = list(data['feature_names'])
    # feature1 = [feat[featureNames.index('mean radius')] for feat in inputs]
    # feature2 = [feat[featureNames.index('mean texture')] for feat in inputs]
    # inputs = [[feat[featureNames.index('mean radius')], feat[featureNames.index('mean texture')]] for feat in inputs]
    feature1 = [feat[featureNames.index('sepal length (cm)')] for feat in inputs]
    feature2 = [feat[featureNames.index('petal length (cm)')] for feat in inputs]
    inputs = [[feat[featureNames.index('sepal length (cm)')], feat[featureNames.index('petal length (cm)')]] for feat in inputs]
    plot(inputs, outputs, feature1, feature2, outputNames)
    inputs, outputs, feature1test, feature2test, testInputs, testOutputs = \
        plotNormalisedData(inputs, outputs, outputNames)
    classes = [0, 1, 2]

    classifier = learnModel(inputs, outputs,classes)

    computedTestOutputs = classifier.predict(testInputs, classes)
    # computedTestOutputs = classifier.predict(testInputs)
    plotPredictions(outputs, feature1test, feature2test, testOutputs, computedTestOutputs, "real test data", outputNames)
    # compute the differences between the predictions and real outputs
    # print("acc score: ", classifier.score(testInputs, testOutputs))
    accuracy(computedTestOutputs, testOutputs)
    acc, prec, recall, cm = evalMultiClass(np.array(testOutputs), computedTestOutputs, outputNames)

    plotConfusionMatrix(cm, outputNames, "iris classification")

    print('acc: ', acc)
    print('precision: ', prec)
    print('recall: ', recall)
