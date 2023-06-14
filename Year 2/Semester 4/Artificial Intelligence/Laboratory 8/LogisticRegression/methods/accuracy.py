def accuracy(computedTestOutputs, testOutputs):
    # evalaute the classifier performance

    error = 0.0
    for t1, t2 in zip(computedTestOutputs, testOutputs):
        if (t1 != t2):
            error += 1
    error = error / len(testOutputs)
    print("classification error (manual): ", error)

    from sklearn.metrics import accuracy_score
    error = 1 - accuracy_score(testOutputs, computedTestOutputs)
    print("classification error (tool): ", error)


def evalMultiClass(realLabels, computedLabels, labelNames):
    from sklearn.metrics import confusion_matrix

    confMatrix = confusion_matrix(realLabels, computedLabels)
    acc = sum([confMatrix[i][i] for i in range(len(labelNames))]) / len(realLabels)
    precision = {}
    recall = {}
    for i in range(len(labelNames)):
        precision[labelNames[i]] = confMatrix[i][i] / sum([confMatrix[j][i] for j in range(len(labelNames))])
        recall[labelNames[i]] = confMatrix[i][i] / sum([confMatrix[i][j] for j in range(len(labelNames))])
    return acc, precision, recall, confMatrix