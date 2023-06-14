# prerequisites
import csv
from sklearn.datasets import load_linnerud
from sklearn.model_selection import train_test_split
from sklearn.multioutput import MultiOutputRegressor
from sklearn.neighbors import KNeighborsRegressor

from SGD.multiTargetRegression import MultiTargetRegressor
from methods.learning import learnModel, toolLearning, toolLearningAdapted
from methods.ploting import plot3Ddata, plotTrainingTestBivariate, plotModelBivariate, makePredictionBivariate, \
    plotTrainingTestUnivariate, plotModelUnivariate, makePredictionUnivariate, plotOptional


def loadData(fileName, inputVariableName1, inputVariableName2, outputVariabName):
    data = []
    dataNames = []
    with open(fileName) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                dataNames = row
            else:
                data.append(row)
            line_count += 1
    selectedVariable1 = dataNames.index(inputVariableName1)
    selectedVariable2 = dataNames.index(inputVariableName2)
    inputs = {inputVariableName1: [float(data[i][selectedVariable1]) for i in range(len(data))],
              inputVariableName2: [float(data[i][selectedVariable2]) for i in range(len(data))]}
    selectedOutput = dataNames.index(outputVariabName)
    outputs = {outputVariabName: [float(data[i][selectedOutput]) for i in range(len(data))]}
    return inputs, outputs


def writeToFile(fileName, textLocal):
    with open(fileName, 'a') as file:
        file.write(textLocal)
    print(textLocal)


if __name__ == '__main__':
    iPath = 'inputs/2017.csv'
    GDP = 'Economy..GDP.per.Capita.'
    freedom = 'Freedom'
    happiness = 'Happiness.Score'
    inputsGDP_Freedom, outputsHappiness = loadData(iPath, GDP, freedom, happiness)

    feature1 = inputsGDP_Freedom[GDP]
    feature2 = inputsGDP_Freedom[freedom]
    outputs = outputsHappiness[happiness]

    trainInputs, trainOutputs, testInputs, testOutputs = \
        plotTrainingTestUnivariate(feature1, outputs)
    trainInputsList = [[t] for t in trainInputs]
    functions = [learnModel(trainInputsList, trainOutputs, 0), toolLearning(trainInputsList, trainOutputs, 0),
                 toolLearningAdapted(trainInputsList, trainOutputs, 0)]  # 0 for univariate
    names = ['learnModel', 'toolLearning', 'toolLearningAdapted']
    for i in range(len(functions)):
        w0, w1, regressor = functions[i]
        xref, yref = plotModelUnivariate(w0, w1, trainOutputs, trainInputs)
        testInputsList = [[t] for t in testInputs]
        error = makePredictionUnivariate(testInputsList, testOutputs, regressor)
        text = 'Function: ' + names[i] + '\nGDP -> the learnt model: f(x) = ' + str(w0) + ' + ' + str(w1) + '* x\nerror = ' + str(error) + '\n '
        writeToFile("outputs/univariate.txt", text)
    writeToFile("outputs/univariate.txt", '\n')

    plot3Ddata(feature1, feature2, outputs, [], [], [], [], [], [], 'capita vs freedom vs happiness')
    feature1Train, feature2Train, trainInputs, trainOutputs, feature1Test, feature2Test, \
    testInputs, testOutputs = plotTrainingTestBivariate(feature1, feature2, outputs)
    functions2 = [learnModel(trainInputs, trainOutputs, 1), toolLearning(trainInputs, trainOutputs, 1),
                    toolLearningAdapted(trainInputs, trainOutputs, 1)]  # 1 for bivariate
    for i in range(len(functions2)):
        w0, w1, w2, regressor = functions2[i]
        x1ref, x2ref, yref = plotModelBivariate(w0, w1, w2, trainOutputs, feature1, feature2, feature1Train, feature2Train)
        error = makePredictionBivariate(testInputs, testOutputs, regressor, feature1Test, feature2Test)
        text2 = 'Function: ' + names[i] + '\nGDP & Freedom -> the learnt model: f(x) = ' + str(w0) + ' + ' + str(w1) + '* x1 + ' + str(
            w2) + ' * x2 \nerror = ' + str(error) + '\n\n'
        writeToFile("outputs/bivariate.txt", text2)
    writeToFile("outputs/bivariate.txt", '\n')

    # optional

    linnerud = load_linnerud()
    # get train data
    X = linnerud.data
    Y = linnerud.target

    X = (X - X.mean()) / X.std()
    Y = (Y - Y.mean()) / Y.std()

    trainInputs, testInputs, trainOutputs, testOutputs = train_test_split(X, Y, test_size=0.2, random_state=37)
    regressor = MultiOutputRegressor(KNeighborsRegressor())
    regressor.fit(trainInputs, trainOutputs)
    predict = regressor.predict(testInputs)
    plotOptional(testOutputs, predict)

    regressor = MultiTargetRegressor()
    regressor.fit(trainInputs, trainOutputs)
    predict = regressor.predict(testInputs)
    plotOptional(testOutputs, predict)


