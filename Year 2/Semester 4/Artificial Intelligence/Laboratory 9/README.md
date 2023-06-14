# Artificial Neural Networks (ANN)

## Introduction

This repository contains code implementations for solving iris and number classification problems using Artificial Neural Networks (ANN). Also, the project classifies images with or without sepia filter, using classifications based on ANN (`MLPClassifier` - from sklearn.neural_network) and CNN (`Sequential` - from tensorflow.keras) tools.

## Table of Contents

- [ANN](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%209/ANN/ANNClass.py)
- [Classification of irises and numbers](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%209/ANN/main.py)
- [Classification of pictures](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%209/ANN/main_photos.py)

### ANN

Here is a brief description of its key methods:

- `forward`: Performs forward propagation through the neural network.
- `backward`: Performs backward propagation (backpropagation) to update the weights.
- `loss`: Computes the loss function based on the predicted outputs and the ground truth labels.
- `fit`: Trains the neural network on the input data and labels.
- `predict`: Predicts the labels for new input data.

# Classification of irises and numbers

## Iris Classification

For the iris classification problem, the following settings are considered:

- _input_size_ = 2 (as there are 2 features for the iris dataset).
- _hidden_size_ = 8.
- _output_size_ = 3 (as there are 3 categories of irises).

## Number Classification

For the number classification problem, the following settings are considered:

- _input_size_ = 64 (as there are 64 images).
- _hidden_size_ = 128.
- _output_size_ = 10 (as there are 10 possible digits).

# Classification of pictures

## Image Classification with(out) Sepia Filter

To evaluate the image classification with a sepia filter, a dataset of 56 photographs taken in `Paris` is used. The dataset is split into two categories: images without a filter and images with a sepia filter. The sepia filter is applied to the images with varying intensities:

- 50% of the images have a filter intensity of 100%.
- 25% have an intensity of 70%.
- The remaining 25% have an intensity of 30%.

This diverse anomalies in the dataset are used to train the classifiers.
