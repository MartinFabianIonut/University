# Logistic Regression Classification

## Introduction

This project focuses on solving classification problems using the logistic regression method. The code has been modularized by separating functions into different files within the `methods` folder, including:

- `accuracy`
- `learning`
- `normalization` and
- `plotting`.

## Table of Contents

- [LogisticRegression](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%208/LogisticRegression/LogisticRegression.py)

### OneLogisticRegression

Class for performing logistic regression on a _single class_. Here is a brief description of its key methods:

- `fit`: Fits the logistic regression model to the given training data using simple stochastic gradient descent.
- `eval`: Evaluates the logistic regression model for a given input xi and coefficients coef.
- `predictOneSample`: Predicts the label for a single sample based on the learned coefficients.
- `predict`: Predicts the labels for a set of input samples.

### MyLogisticRegression: Class for performing logistic regression on _multiple classes_. Here is a brief description of its key methods:

- `fit`: Fits the logistic regression model to the given training data for multiple classes.
- `eval`: Evaluates the logistic regression model for a given input xi and coefficients coef.
- `predict`: Predicts the labels for a set of test inputs based on the learned coefficients.

## Usage

1. Loads the iris dataset and extracts the inputs, outputs, output names, and feature names.
2. Selects the two features (_sepal length_ and _petal length_) and prepares the inputs accordingly.
3. Calls the plot function to visualize the training data.
4. Normalizes the inputs and outputs using the normalisation function.
5. Uses the learnModel function to fit the logistic regression model to the training data and obtain a classifier (or `MyLogisticRegression`, or `LogisticRegression` from sklearn).
6. Predicts the labels for the test inputs using the classifier.
7. Plots the predicted labels in comparison to the actual labels for the test data.
8. Computes and displays the classification error of the predicted outputs.
9. Evaluates the classifier's performance by calculating accuracy, precision, recall, and generating a confusion matrix using the evalMultiClass function.
10. Plots the confusion matrix for visualizing the classification results.
11. Prints the accuracy, precision, and recall scores.
