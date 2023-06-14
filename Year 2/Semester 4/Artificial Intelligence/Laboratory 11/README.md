# Emotion Classification

## Introduction

This project aims to classify emotions in images containing emoticons and real faces using (pre-)trained models.
It includes four components:

1. `emoji emotion classification`
2. `real face emotion classification with a pre-trained model`
3. `real face emotion classification with a trained model from scratch` and
   - manually extracted features: `HOG`
   - automatically extracted features: `Facenet`
4. `multi-label classification`

## Table of Contents

- [Emotion Classification in Emoji Images](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2011/Emotion/main.py)
- [Emotion Classification in Real Face Images](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2011/Emotion/main_emo.py)
- [Emotion Classification in Real Face Images using a Classifier Trained from Scratch](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2011/Emotion/main_classifier.py)
  - [Image Classifiers for both manually and automatically extracted features](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2011/Emotion/imageClassifier.py)
- [Multi-label Image Classification](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2011/Emotion/main_multilabel.py)

## Emotion Classification in Emoji Images

It performs the following steps:

1. Download the emoji data file (`emoji.json`) from the internet.
2. Create two folders for storing happy and sad emojis: `happy_emojis` and `sad_emojis`.
3. Preprocess the emoji images, converting them to grayscale and resizing them to a consistent size (48x48).
4. Split the emoji data into training and testing sets.
5. Train a deep learning model to classify emotions in the emoji images using a Convolutional Neural Network (CNN) architecture: `Sequential`.
6. Evaluate the trained model on the testing set and calculate the accuracy.
7. Make predictions on the testing set and generate a `confusion matrix` to visualize the classification results.

## Emotion Classification in Real Face Images

It utilizes a pre-trained model (`model_v6_23.hdf5`) for emotion detection. The steps involved are as follows:

1. Load the pre-trained model.
2. Iterate over the test images in different emotion categories (`Anger`, `Disgust`, `Fear`, `Happiness`, `Neutral`, `Sadness`, `Surprise`) located in the `facial_emotions/test` folder.
3. Shuffle the images.
4. Classify emotions in each test image using the pre-trained model.
5. Evaluate the trained model on the testing set and calculate the accuracy.
6. Compare the predicted labels with the actual labels and generate a `confusion matrix` for evaluation.

## Emotion Classification in Real Face Images using a Classifier Trained from Scratch

To classify emotions in real face images, we can train a classifier (model) using extracted image features. There are two approaches to extract features:

- **Manual**: `Histogram of Oriented Gradients (HOG)`
- **Automatic**: `Facenet`

### 1. Manual Feature Extraction - HOG

### ImageClassifier

Here is a brief description of its key methods:

- `extract_hog_features`: Extracts HOG features from an input image.
- `fit`: Trains the classifier on a set of images and their corresponding labels.
- `predict`: Predicts the emotions for a set of input images.

### 2. Automatic Feature Extraction - Facenet

### ImageClassifierFacenet

Here is a brief description of its key methods:

- `preprocess_image`: Preprocesses an input image for the Facenet model.
- `extract_face_embeddings`: Extracts face embeddings from an input image using the Facenet model.
- `fit`: Trains the classifier on a set of images and their corresponding labels.
- `predict`: Predicts the emotions for a set of input images.

## Multi-label Image Classification

! It uses `PyTorch Lightning` and GPU (`NVIDIA® GeForce RTX™ 3070 GPU` with 8GB GDDR6 memory) to speed up calculations.

Steps:

1. Prepare the dataset:
   - Organize the image dataset with corresponding label information.
   - Update the file paths for the training images and CSV file containing label data.
     - labels like: motorcycle, truck, boat, bus, cycle, sitar, ektara, flutes, tabla and harmonium
2. Run the training and evaluation:
   - Initialize the _ImageDataset_ with the dataset and optional transformations.
   - Split the dataset into training, validation, and test sets.
   - Create data loaders for each set using `torch.utils.data.DataLoader`.
   - Configure the model and the trainer.
   - Call the trainer's _fit_ to train the model on the training and validation sets.
   - Use the trainer's _predict_ to get predictions on the test set.
   - Calculate the _accuracy score_ for the predictions.
