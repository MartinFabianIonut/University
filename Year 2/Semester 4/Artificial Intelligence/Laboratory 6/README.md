# Prediction algorithm based on the method of least squares

## Introduction

This project implements least squares regression for both univariate and bivariate cases. It provides two classes, `MyLinearUnivariateRegression` and `MyLinearBivariateRegression`, which can be used to fit regression models and make predictions without relying on the sklearn library. As part of the problem, I also use the `LinearRegression` provided by the sklearn library.

## Table of Contents

- [MyLinearUnivariateRegression](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%206/Happiness/classes/MyLinearUnivariateRegression.py)
- [MyLinearBivariateRegression](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%206/Happiness/classes/MyLinearBivariateRegression.py)

### MyLinearUnivariateRegression

This class implements least squares regression for the univariate case, where the relationship between the input (x) and output (y) is modeled as `y = w0 + w1 * x`.

The class provides the following methods:

- `fit(x, y)`: Fit a linear univariate regression model to the training data.
- `predict(x)`: Make predictions for new inputs using the learnt model.

### MyLinearBivariateRegression

This class implements least squares regression for the bivariate case, where the relationship between two input variables (x1 and x2) and the output (y) is modeled as `y = w0 + w1 * x1 + w2 * x2`.

The class provides the following methods:

- `fit(X1, X2, Y)`: Fit a linear bivariate regression model to the training data.
- `predict(X)`: Make predictions for new inputs using the learnt model.

## Plotting Functions

In addition to the regression classes, the project includes several functions for plotting data and models. These functions are located in the `solutions` folder and are used to visualize the training and validation data, the learnt model, and the predictions.

- `plotTrainingValidation`: Plots the training and validation data for univariate regression.
- `plotLinearModel`: Plots the learnt model for univariate regression.
- `makePrediction`: Makes predictions for the validation data and computes the error for univariate regression.
- `plotTrainingValidationBivariate`: Plots the training and validation data for bivariate regression.
- `plot3Ddata`: Plots data points in a 3D space, including the training data, the learnt model, and the validation data.
- `plotModelBivariate`: Plots the learnt model for bivariate regression.
- `makePredictionBivariate`: Makes predictions for the validation data and computes the error for bivariate regression.

## Input

Using the data for the year 2017, we will make predictions for the happiness score based on:

1. Only the Gross Domestic Product (GDP) (detailed live example - demo).
2. Both the Gross Domestic Product (GDP) and the degree of freedom (assignment).

Input files:

- `v1_world-happiness-report-2017.csv`
- `v2_world-happiness-report-2017.csv`
- `v3_world-happiness-report-2017.csv`

## Output

The algorithm generates the following output:

- `.txt` files, both with _code_ and _tool_: Each file contains:
  - `the learnt model`
  - `the error`
