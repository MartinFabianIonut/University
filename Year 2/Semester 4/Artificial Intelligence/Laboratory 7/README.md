# Prediction algorithm based on the Gradient Descent method

## Introduction

The task entails solving regression problems using machine learning methods. This project provides a set of classes and methods for solving regression problems using machine learning methods.

## Table of Contents

- [MySGDRegression](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%207/HappinessSGD/SGD/sgdRegression.py) : A class for performing simple stochastic gradient descent (SGD) regression.
- [BatchGDRegression](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%207/HappinessSGD/SGD/batchGDRegression.py) : A class for performing batch gradient descent (BGD) regression.
- [MultiTargetRegressor](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%207/HappinessSGD/SGD/multiTargetRegression.py) : A class for performing regression with multiple target variables.

### MySGDRegression

Here is a brief description of its key methods:

- `fit(x, y, learningRate, noEpochs)`: Fits the regression model to the given training data using stochastic gradient descent.
- `eval(xi)`: Evaluates the regression model for a given input xi.
- `predict(x)`: Predicts the output for the given input x.

### BatchGDRegression

Here is a brief description of its key methods:

- same as those for `MySGDRegression`

### MultiTargetRegressor

Here is a brief description of its key methods:

- `fit(X, y)`: Fits the regression model to the given training data using the Moore-Penrose pseudoinverse.
- `predict(X)`: Predicts the output for the given input X.

## Usage

### Methods in modules

- `learning`
  - _learnModel_: Fits a regression model to the training data using the `BatchGDRegression` class, for univariate/bivariate regression, based on the specified uniOrMulti parameter.
  - _toolLearning_: Fits a regression model to the training data using the `SGDRegressor` class from scikit-learn, for univariate/bivariate regression, based on the specified uniOrMulti parameter.
  - _toolLearningAdapted_: Fits a regression model to the training data using an adapted version of `SGDRegressor` class from scikit-learn, for univariate/bivariate regression, based on the specified uniOrMulti parameter.
- `normalization`
  - _normalisation_: Performs feature normalization on the training and test data using the `StandardScaler` from scikit-learn.
  - _statisticalNormalisation_: Performs statistical normalization on the features by subtracting the mean and dividing by the standard deviation.
- `plotting`
  - _plot3Ddata_: Plots the 3D data points for visualization.
  - _plotTrainingTestUnivariate_: Plots the training and test data for univariate regression problems.
  - _plotTrainingTestBivariate_: Plots the training and test data for bivariate regression problems.
