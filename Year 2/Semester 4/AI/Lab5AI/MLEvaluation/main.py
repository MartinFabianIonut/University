import csv
import math
from math import sqrt


def readCsvSport(realOutputs, computedOutputs, realMatrix, computedMatrix):
    with open('inputs/sport.csv', mode='r') as csv_file:
        csv_reader = csv.DictReader(csv_file)
        for row in csv_reader:
            realOutputs['Weight'].append(row['Weight'])
            realOutputs['Waist'].append(row['Waist'])
            realOutputs['Pulse'].append(row['Pulse'])
            lineR = [int(row['Weight']), int(row['Waist']), int(row['Pulse'])]
            realMatrix.append(lineR)
            computedOutputs['PredictedWeight'].append(row['PredictedWeight'])
            computedOutputs['PredictedWaist'].append(row['PredictedWaist'])
            computedOutputs['PredictedPulse'].append(row['PredictedPulse'])
            lineC = [int(row['PredictedWeight']), int(row['PredictedWaist']), int(row['PredictedPulse'])]
            computedMatrix.append(lineC)


def readCsvFlowers(realMatrix, computedMatrix, classes):
    with open('inputs/flowers.csv', mode='r') as csv_file:
        csv_reader = csv.DictReader(csv_file)
        for row in csv_reader:
            realMatrix.append(row['Type'])
            computedMatrix.append(row['PredictedType'])
            if row['Type'] not in classes:
                classes.append(row['Type'])


def evaluateClass(realLabels, computedLabels, label):
    acc = sum([1 if realLabels[i] == computedLabels[i] else 0 for i in range(0, len(realLabels))]) / len(realLabels)
    TP = sum([1 if (realLabels[i] == label and computedLabels[i] == label) else 0 for i in range(len(realLabels))])
    FP = sum([1 if (realLabels[i] != label and computedLabels[i] == label) else 0 for i in range(len(realLabels))])
    TN = sum([1 if (realLabels[i] != label and computedLabels[i] != label) else 0 for i in range(len(realLabels))])
    FN = sum([1 if (realLabels[i] == label and computedLabels[i] != label) else 0 for i in range(len(realLabels))])

    precisionPos = TP / (TP + FP)
    recallPos = TP / (TP + FN)
    precisionNeg = TN / (TN + FN)
    recallNeg = TN / (TN + FP)
    precision = (precisionPos + precisionNeg) / 2
    recall = (recallPos + recallNeg) / 2

    return acc, precision, recall


def readProbabilities(filePathTrue, filePathComputed):
    computed = []
    true = []
    with open(filePathComputed, mode='r') as file:
        for line in file:
            newLine = line.strip().split(' ')
            row = []
            for pos in range(len(newLine)):
                row.append(float(newLine[pos]))
            computed.append(row)
    with open(filePathTrue, mode='r') as file:
        for line in file:
            newLine = line.strip().split(' ')
            row = []
            for pos in range(len(newLine)):
                row.append(int(newLine[pos]))
            true.append(row)
    return true, computed


def writeCrossEntropy(outputPath, true, computed):
    # Cross entropy
    CE = 0
    suma = 0.0
    for i in range(len(computed)):
        for pi, qi in zip(true[i], computed[i]):
            suma += pi * math.log2(qi)

    CE = -suma
    CE /= len(computed)
    with open(outputPath, 'w') as write:
        write.write('Cost = ' + str(CE))


def writeMultiLabelCrossEntropy(outputPath, true, computed):
    # Multi-Label Cross entropy
    suma = 0.0
    for i in range(len(computed)):
        for pi, qi in zip(true[i], computed[i]):
            suma += pi * math.log(qi) + (1-pi) * math.log(1-qi)

    MLCE = -suma
    MLCE /= len(computed)
    with open(outputPath, 'w') as write:
        write.write('Cost = ' + str(MLCE))


if __name__ == '__main__':
    real = {'Weight': [], 'Waist': [], 'Pulse': []}
    computed = {'PredictedWeight': [], 'PredictedWaist': [], 'PredictedPulse': []}
    realM = []
    computedM = []
    readCsvSport(real, computed, realM, computedM)
    # RMSE

    error = 0
    for i in range(len(realM)):
        error += sqrt(sum((r - c) ** 2 for r, c in zip(realM[i], computedM[i])) / len(realM[i]))
    error /= len(realM)

    realFlower = []
    computedFlower = []
    classes = []
    readCsvFlowers(realFlower, computedFlower, classes)
    MAA = 0  # Macro Average Accuracy
    MAP = 0  # Macro Average Precision
    MAR = 0  # Macro Average Recall
    for c in classes:
        acc, pre, rec = evaluateClass(realFlower, computedFlower, c)
        MAA += acc
        MAP += pre
        MAR += rec
    MAA /= len(classes)
    MAP /= len(classes)
    MAR /= len(classes)

    true, computed = readProbabilities('inputs/true-binary.txt', 'inputs/probabilities-binary.txt')
    writeCrossEntropy('outputs/binary.txt', true, computed)
    true, computed = readProbabilities('inputs/true-multi-class.txt', 'inputs/probabilities-multi-class.txt')
    writeCrossEntropy('outputs/multi-class.txt', true, computed)
    true, computed = readProbabilities('inputs/true-multi-target.txt', 'inputs/probabilities-multi-target.txt')
    writeMultiLabelCrossEntropy('outputs/multi-target.txt', true, computed)

    # writing the results to output files
    file = open('outputs/sport-error.txt', "w")
    file.write("Error = " + str(error) + '\n')
    file.close()
    file = open('outputs/APR.txt', "w")
    file.write("Macro Average Accuracy = " + str(MAA) + '\n')
    file.write("Macro Average Precision = " + str(MAP) + '\n')
    file.write("Macro Average Recall = " + str(MAR) + '\n')
    file.close()
