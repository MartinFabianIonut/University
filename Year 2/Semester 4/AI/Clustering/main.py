from sklearn import neural_network
from KMeansClustering import KMeansClustering
from methods.data_management import splitData, extractCharacteristics
from sklearn.metrics import accuracy_score
from sklearn.datasets import load_iris
import numpy as np


def readCsv(filePath):
    import csv
    dataLocal = []
    dataNamesLocal = []
    with open(filePath, 'r') as csvFile:
        csvReader = csv.reader(csvFile)
        for row in csvReader:
            # if first row, get the names of the features
            if not dataNamesLocal:
                dataNamesLocal = row
                continue
            # if not first row, add the data
            dataLocal.append(row)
    return dataLocal, dataNamesLocal


def shuffleData(inputs, outputs):
    permutation = np.random.permutation(len(inputs))
    inputs = [inputs[el] for el in permutation]
    outputs = [outputs[el] for el in permutation]
    return inputs, outputs


if __name__ == '__main__':
    # k-means numeric
    iris = load_iris()
    inputs = iris.data
    outputs = iris.target
    inputs, outputs = shuffleData(inputs, outputs)
    var = trainInputs, trainOutputs, testInputs, testOutputs = splitData(inputs, outputs)
    # 3 clusters for the 3 types of flowers - setosa, versicolor, virginica
    kmeans = KMeansClustering(k=3)
    kmeans.fit(trainInputs)
    computedTestOutputs = kmeans.predict(testInputs)
    # just supposing that we have the true labels
    print("KMeansClustering - mine : acc: ", accuracy_score(testOutputs, computedTestOutputs))
    print("----------------------------------------------------------")

    data, dataNames = readCsv('inputs/reviews_mixed.csv')
    inputs = [data[i][0] for i in range(len(data))]
    outputs = [data[i][1] for i in range(len(data))]
    labelNames = list(set(outputs))

    trainInputs, trainOutputs, testInputs, testOutputs = splitData(inputs, outputs)

    # word2vecModel300 = embedded()
    # trainFeatures = featureComputation(word2vecModel300, trainInputs)
    # testFeatures = featureComputation(word2vecModel300, testInputs)

    trainFeatures, testFeatures = extractCharacteristics(trainInputs, testInputs)

    from sklearn.cluster import KMeans

    # 2 clusters for the 2 types of reviews - positive, negative
    unsupervisedClassifier = KMeans(n_clusters=2, random_state=0)
    unsupervisedClassifier.fit(trainFeatures)

    computedTestIndexes = unsupervisedClassifier.predict(testFeatures)
    computedTestOutputs = [labelNames[value] for value in computedTestIndexes]

    from sklearn.metrics import accuracy_score

    # just supposing that we have the true labels
    print("KMeans acc: ", accuracy_score(testOutputs, computedTestOutputs))
    print("----------------------------------------------------------")

    # supervised classifier - SGD
    from sklearn.linear_model import SGDClassifier
    from sklearn.metrics import accuracy_score

    supervisedClassifier = SGDClassifier(loss='hinge', penalty='l2', alpha=1e-3, random_state=42, max_iter=5,
                                         tol=None)
    supervisedClassifier.fit(trainFeatures, trainOutputs)
    computedTestOutputs = supervisedClassifier.predict(testFeatures)
    print("SGDClassifier acc: ", accuracy_score(testOutputs, computedTestOutputs))
    print("----------------------------------------------------------")

    # supervised classifier - SVM
    from sklearn.svm import SVC
    from sklearn.metrics import accuracy_score

    supervisedClassifier = SVC(kernel='linear')
    supervisedClassifier.fit(trainFeatures, trainOutputs)
    computedTestOutputs = supervisedClassifier.predict(testFeatures)
    print("SVC acc: ", accuracy_score(testOutputs, computedTestOutputs))
    print("----------------------------------------------------------")

    # supervised classifier - Decision Tree
    from sklearn.tree import DecisionTreeClassifier
    from sklearn.metrics import accuracy_score

    supervisedClassifier = DecisionTreeClassifier(random_state=0)
    supervisedClassifier.fit(trainFeatures, trainOutputs)
    computedTestOutputs = supervisedClassifier.predict(testFeatures)
    print("DecisionTreeClassifier acc: ", accuracy_score(testOutputs, computedTestOutputs))
    print("----------------------------------------------------------")

    # semi-supervised classifier - KMeans applied on the features
    # transform to array for representation 1,2 and 3 for the characteristics
    trainFeatures = trainFeatures.toarray()
    testFeatures = testFeatures.toarray()

    # this classifier is supervised, but it uses the unsupervised classifier
    # to predict the labels for the unlabeled data
    classifier = neural_network.MLPClassifier()
    # 100 clusters for the 100 types of reviews
    classifier.fit(trainFeatures[:100], trainOutputs[:100])
    computedTestOutputs = classifier.predict(testFeatures)
    prev_accuracy = accuracy_score(testOutputs, computedTestOutputs)
    print("Accuracy for supervised learning 1: ", prev_accuracy)

    unsupervisedClassifier = KMeans(n_clusters=100, random_state=0)
    x = unsupervisedClassifier.fit_transform(trainFeatures)
    # pos is the index of the closest centroid for each input
    pos = np.argmin(x, axis=0)
    chosenInputs = [trainFeatures[index] for index in pos]
    chosenOutputs = [list(trainOutputs)[index] for index in pos]
    classifier = neural_network.MLPClassifier()
    classifier.fit(chosenInputs, chosenOutputs)
    computedTestOutputs = classifier.predict(testFeatures)

    new_accuracy = accuracy_score(testOutputs, computedTestOutputs)
    print("Accuracy for semi-supervised learning 2: ", new_accuracy)
