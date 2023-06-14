# K-Means Clustering

This project focuses on developing, training, and testing a K-Means clustering algorithm for text data classification. The goal is to classify texts and label them with emotions using various feature extraction techniques and supervised and unsupervised learning algorithms.

## Input

1.  ```python
    from sklearn.datasets import load_iris
    ```
2.  `review_mixed.csv`

## Table of Contents

- [KMeansClustering](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2010/Clustering/KMeansClustering.py)

### K-Means Clustering

The `KMeansClustering` class implements the K-Means clustering algorithm. It initializes with the number of clusters (`k`), tolerance for convergence (`tol`), and the maximum number of iterations (`max_iter`). The algorithm's main methods are:

- `fit(data)`: Fit the K-Means clustering algorithm to the given numeric data.
- `predict_one(data)`: Predict the cluster label for a single data point.
- `predict(data)`: Predict the cluster labels for a set of data points.

### Feature Extraction

The `extractCharacteristics` function performs feature extraction on text data using different representations such as:

1. Bag of Words
2. Bag of Words with 2-grams
3. TF-IDF and
4. Word2Vec

The function takes train inputs and test inputs as arguments and returns the extracted train and test features.

### Emotion Classification with Supervised, Unsupervised, and Hybrid Learning

This repository contains code for classifying text and labeling them with emotions using different machine learning algorithms. The classification approaches include supervised learning, unsupervised learning (specifically, k-means clustering), and a hybrid approach that combines supervised learning with rule-based techniques.

## Usage

Here's a breakdown of the different approaches and classifiers used:

### Unsupervised Learning (k-means clustering):

- The code uses the `KMeans` class from the `sklearn.cluster` module.
- It initializes a KMeans classifier with 2 clusters for the 2 types of reviews (_positive and negative_).
- The classifier is trained on the extracted features from the training data using the fit method.
- Predictions are made on the test data using the predict method, and the computed test outputs are obtained by mapping the predicted cluster labels to the corresponding label names.
- The accuracy of the k-means classifier is evaluated using the `accuracy_score` function from the `sklearn.metrics` module.

### Supervised Learning (SGD, SVM, Decision Tree):

- The code demonstrates the use of three different supervised classifiers: `SGDClassifier`, `SVC`, and `DecisionTreeClassifier`, all available in the sklearn library.
- For each classifier, the classifier is initialized with the desired parameters.
- The classifier is then trained on the extracted features and corresponding labels from the training data using the fit method.
- Predictions are made on the test data using the predict method, and the accuracy of the classifier is evaluated using the accuracy_score function.

### Hybrid Learning (Semi-supervised):

- The code showcases a semi-supervised approach that combines unsupervised and supervised learning.
- Initially, the features are transformed into arrays for representation purposes.
- A _supervised classifier_ (`MLPClassifier`) is trained on a subset of the labeled training data using the fit method.
- The trained supervised classifier is then used to predict labels for the unlabeled data.
- An _unsupervised classifier_ (`KMeans`) is used to find the closest centroids for each input in the training data.
- Inputs and outputs corresponding to the chosen centroids are selected.
- Another _supervised classifier_ (`MLPClassifier`) is trained on the selected inputs and outputs.
- Finally, predictions are made on the test data using the trained **semi-supervised classifier**, and the accuracy is calculated using the `accuracy_score` function.
