from sklearn.preprocessing import StandardScaler
from statistics import mean, stdev


def normalisation(trainInputs, testInputs):
    scaler = StandardScaler()
    if not isinstance(trainInputs[0], list):
        # encode each sample into a list
        trainData = [[d] for d in trainInputs]
        testData = [[d] for d in testInputs]

        scaler.fit(trainData)  # fit only on training data
        normalisedTrainData = scaler.transform(trainData)  # apply same transformation to train data
        normalisedTestData = scaler.transform(testData)  # apply same transformation to test data

        # decode from list to raw values
        normalisedTrainData = [el[0] for el in normalisedTrainData]
        normalisedTestData = [el[0] for el in normalisedTestData]
    else:
        scaler.fit(trainInputs)  # fit only on training data
        normalisedTrainData = scaler.transform(trainInputs)  # apply same transformation to train data
        normalisedTestData = scaler.transform(testInputs)  # apply same transformation to test data
    return normalisedTrainData, normalisedTestData


# statistical normalisation (centered around meand and standardisation)
def statisticalNormalisation(features, meanValue=None, stdDevValue=None):
    # meanValue = sum(features) / len(features)
    true = False
    if meanValue is None:
        meanValue = mean(features)
        true = True
    # stdDevValue = (1 / len(features) * sum([ (feat - meanValue) ** 2 for feat in features])) ** 0.5
    if stdDevValue is None:
        stdDevValue = stdev(features)
        true = True
    normalisedFeatures = [(feat - meanValue) / stdDevValue for feat in features]
    if true:
        return normalisedFeatures, meanValue, stdDevValue
    else:
        return normalisedFeatures
