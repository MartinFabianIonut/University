import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.utils import shuffle
from keras.preprocessing.text import Tokenizer
from keras.utils import pad_sequences
from keras.models import Sequential, load_model
from keras.layers import Embedding, LSTM, Dense


def preprocess_posts(previous_posts):
    # Initialize tokenizer
    tokenizer = Tokenizer()
    # Fit tokenizer on previous posts
    tokenizer.fit_on_texts(previous_posts)
    # Convert text to sequences
    sequences = tokenizer.texts_to_sequences(previous_posts)
    # Pad sequences to ensure consistent length
    max_sequence_length = 28
    # padded_sequences is a 2D array where each row is a sequence of integers representing the words in the post
    padded_sequences = pad_sequences(sequences, maxlen=max_sequence_length)
    return padded_sequences


def predict_sentiment_and_generate_text(previous_posts, model):
    encoded_posts = preprocess_posts(previous_posts)
    # Preprocess the new post as needed
    predicted_probabilities = model.predict(encoded_posts)
    # Get the sentiment score for the new post (0 = negative, 1 = positive) based on the predicted probabilities
    # for each class predicted_probabilities[-1] is the last element of the array
    sentiment_score = np.argmax(predicted_probabilities[-1])
    return sentiment_score


def readPosts(filePath):
    dataLocal = pd.read_csv(filePath)
    return dataLocal['Text'].tolist(), dataLocal['Sentiment'].tolist()


if __name__ == '__main__':
    # Example usage for previous_posts from inputs/reviews_mixed.csv read in main.py
    previous_posts, labels = readPosts('inputs/reviews_mixed.csv')

    # Shuffle the data and labels
    previous_posts, labels = shuffle(previous_posts, labels)

    # Split the data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(previous_posts, labels, test_size=0.2, random_state=42)

    # Preprocess the training and testing data
    encoded_inputs = preprocess_posts(X_train)
    encoded_test_inputs = preprocess_posts(X_test)

    # Define the LSTM model
    model = Sequential()
    model.add(Embedding(input_dim=1000, output_dim=64, input_length=encoded_inputs.shape[1]))
    # LSTM = Long Short Term Memory
    model.add(LSTM(units=128))
    model.add(Dense(units=2, activation='softmax'))

    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    model.summary()

    # Convert labels to numerical representation
    label_mapping = {'negative': 0, 'positive': 1}
    encoded_labels = np.array([label_mapping[label] for label in y_train])
    encoded_test_labels = np.array([label_mapping[label] for label in y_test])

    encoded_labels = tf.keras.utils.to_categorical(encoded_labels)
    encoded_test_labels = tf.keras.utils.to_categorical(encoded_test_labels)

    # Train the model
    model.fit(encoded_inputs, encoded_labels, validation_data=(encoded_test_inputs, encoded_test_labels),
              epochs=30, batch_size=32)

    # Save the trained model
    # if it is already saved, it will be overwritten
    model.save("sentiment_model.h5")

    # Load the saved model
    loaded_model = load_model("sentiment_model.h5")

    # Example usage to predict sentiment for the next post
    next_post_sentiment = predict_sentiment_and_generate_text(previous_posts, loaded_model)

    sentiment_label_mapping = {0: 'negative', 1: 'positive'}
    print("Predicted sentiment for the next post:", sentiment_label_mapping[next_post_sentiment])
