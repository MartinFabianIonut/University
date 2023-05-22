import urllib.request
import cv2
import numpy as np
import tensorflow as tf
import glob

from keras.utils import img_to_array
from sklearn.metrics import confusion_matrix

from methods.ploting import plotConfusionMatrix


# Function to classify emotions in an image
def classify_emotion(image_path, model):
    # img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    # img = cv2.resize(img, (48, 48))
    # alternative to cv2.imread
    from PIL import Image

    img = Image.open(image_path).convert('L')
    img = np.array(img)
    img = img.astype('float32') / 255.0
    img = img_to_array(img)
    img = cv2.resize(img, (48, 48))
    img = np.expand_dims(img, axis=0)

    prediction = model.predict(img, verbose=None)[0]
    # emotion_labels = ['Anger', 'Disgust', 'Fear', 'Happiness', 'Neutral', 'Sadness', 'Surprise']
    emotion_indices = [0, 1, 2, 3, 4, 5, 6]
    predicted_label = emotion_indices[prediction.argmax()]
    return predicted_label


if __name__ == '__main__':
    # Download pre-trained model
    model_url = "https://github.com/priya-dwivedi/face_and_emotion_detection/blob/master/emotion_detector_models/model_v6_23.hdf5?raw=true"
    urllib.request.urlretrieve(model_url, "model_v6_23.hdf5")

    # Load pre-trained model
    model = tf.keras.models.load_model("model_v6_23.hdf5")
    # emotion_labels = ['Anger', 'Disgust', 'Fear', 'Happiness', 'Neutral', 'Sadness', 'Surprise']
    # index = classify_emotion('facial_emotions/mine/20230521_170859.jpg', model)
    # print(emotion_labels[index])

    # Example usage
    # get all images from the folder facial_emotions, subfolder test
    # for each subfolder, get the images
    # for each image, predict the emotion
    # print the predicted emotion
    testLabels = []
    predictedLabels = []
    folder_path = 'facial_emotions/test/angry/*.jpg'
    image_paths = glob.glob(folder_path)
    # shuffle the images randomly every time
    # get 20% of the images for testing, float to int
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    # get 20% of the images for testing, float to int
    testLabels += [0 for i in range(len(image_paths))]

    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with angry')
    folder_path = 'facial_emotions/test/disgust/*.jpg'
    image_paths = glob.glob(folder_path)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    testLabels += [1 for i in range(len(image_paths))]
    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with disgust')
    folder_path = 'facial_emotions/test/fear/*.jpg'
    image_paths = glob.glob(folder_path)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    testLabels += [2 for i in range(len(image_paths))]
    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with fear')
    folder_path = 'facial_emotions/test/happy/*.jpg'
    image_paths = glob.glob(folder_path)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    testLabels += [3 for i in range(len(image_paths))]
    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with happy')
    folder_path = 'facial_emotions/test/neutral/*.jpg'
    image_paths = glob.glob(folder_path)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    testLabels += [4 for i in range(len(image_paths))]
    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with neutral')
    folder_path = 'facial_emotions/test/sad/*.jpg'
    image_paths = glob.glob(folder_path)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    testLabels += [5 for i in range(len(image_paths))]
    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with sad')
    folder_path = 'facial_emotions/test/surprise/*.jpg'
    image_paths = glob.glob(folder_path)
    shuffle = np.random.permutation(len(image_paths))
    indices = shuffle[:int(len(image_paths) * 0.2)]
    image_paths = [image_paths[i] for i in indices]
    testLabels += [6 for i in range(len(image_paths))]
    for image_path in image_paths:
        predicted_emotion = classify_emotion(image_path, model)
        predictedLabels.append(predicted_emotion)
    print('Done with surprise')
    # calculate the accuracy
    accuracy = np.mean(np.equal(testLabels, predictedLabels))
    print('Accuracy: ', accuracy)

    plotConfusionMatrix(confusion_matrix(testLabels, predictedLabels),
                        ['Anger', 'Disgust', 'Fear', 'Happiness', 'Neutral', 'Sadness', 'Surprise'],
                        '- Emotion Confusion Matrix')
