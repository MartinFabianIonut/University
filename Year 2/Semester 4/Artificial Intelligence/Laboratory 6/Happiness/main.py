# prerequisites
import csv

import matplotlib.pyplot as plt

from solutions.bivariate_regression import plotTrainingValidationBivariate, plotModelBivariate, \
    makePredictionBivariate
from solutions.univariate_regression import plotTrainingValidation, plotLinearModel, makePrediction


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


def plotDataHistogram(x, variableName):
    n, bins, patches = plt.hist(x, 10)
    plt.title('Histogram of ' + variableName)
    plt.show()


def writeToFile(fileName, textLocal):
    with open(fileName, 'a') as file:
        file.write(textLocal)
    print(textLocal)


def choosePaths(nr, typeOfSolution):
    if typeOfSolution == 'tool':
        if nr == 1:
            iPathLocal = inputsPaths[0]
            oPathLocal = outputPaths[0]
        elif nr == 2:
            iPathLocal = inputsPaths[1]
            oPathLocal = outputPaths[1]
        else:
            iPathLocal = inputsPaths[2]
            oPathLocal = outputPaths[2]
    else:
        if nr == 1:
            iPathLocal = inputsPaths[0]
            oPathLocal = outputPaths[3]
        elif nr == 2:
            iPathLocal = inputsPaths[1]
            oPathLocal = outputPaths[4]
        else:
            iPathLocal = inputsPaths[2]
            oPathLocal = outputPaths[5]
    return iPathLocal, oPathLocal


if __name__ == '__main__':
    inputsPaths = ['inputs/v1_world-happiness-report-2017.csv', 'inputs/v2_world-happiness-report-2017.csv',
                   'inputs/v3_world-happiness-report-2017.csv']
    outputPaths = ['outputs/v1_tool.txt', 'outputs/v2_tool.txt',
                   'outputs/v3_tool.txt', 'outputs/v1_code.txt', 'outputs/v2_code.txt', 'outputs/v3_code.txt']

    iPath, oPath = choosePaths(2, 'code')

    GDP = 'Economy..GDP.per.Capita.'
    freedom = 'Freedom'
    happiness = 'Happiness.Score'
    inputsGDP_Freedom, outputsHappiness = loadData(iPath, GDP, freedom, happiness)

    feature1 = inputsGDP_Freedom[GDP]
    feature2 = inputsGDP_Freedom[freedom]
    output = outputsHappiness[happiness]
    combined = list([el1, el2] for el1, el2 in zip(feature1, feature2))
    i1 = 0
    i2 = 0
    # for pair in combined:
    #     for otherPair in combined:
    #         if pair != otherPair and pair[0] != 0 and otherPair[0] != 0 and pair[1] != 0 and otherPair[1] != 0:
    #             if pair[0] / otherPair[0] == pair[1] / otherPair[1]:
    #                 combined.remove(otherPair)
    #                 output.pop(i2)
    #         i2 += 1
    #     i1 += 1
    #     i2 = 0
    # print(len(combined))
    # print(len(output))

    # feature1 = [el[0] for el in combined]
    # feature2 = [el[1] for el in combined]

    # print('feature1: ', feature1)
    # print('feature2: ', feature2)
    # print('output: ', output)

    # Single Linear Regression
    trainedInputs, trainedOutputs, validatedInputs, validatedOutputs = plotTrainingValidation(feature1, output)
    reg, w0, w1 = plotLinearModel(trainedInputs, trainedOutputs)
    error = makePrediction(validatedInputs, validatedOutputs, reg)
    text = 'GDP -> the learnt model: f(x) = ' + str(w0) + ' + ' + str(w1) + ' * x\nerror = ' + str(error) + '\n'
    writeToFile(oPath, text)

    # Multiple Linear Regression
    # plot3Ddata(feature1, feature2, output, [], [], [], [], [], [], 'capita and freedom vs happiness')
    feature1Train, feature2Train, trainInput, trainOutput, feature1validation, feature2validation, validationInput, validationOutput = \
        plotTrainingValidationBivariate(feature1, feature2, output)
    reg, x1, x2, y, w0, w1, w2 = plotModelBivariate(trainInput, trainOutput, feature1, feature2, feature1Train, feature2Train)
    error = makePredictionBivariate(validationInput, validationOutput, reg, feature1validation, feature2validation)
    text2 = 'GDP & Freedom -> the learnt model: f(x) = ' + str(w0) + ' + ' + str(w1) + ' * x1 + ' + str(w2) + ' * x2 \nerror = ' + str(error) + '\n\n'
    writeToFile(oPath, text2)
