import os
import numpy as np
import pandas as pd
import tensorflow as tf
from keras.callbacks import CSVLogger


# Function to read posts and labels from a CSV file
def readPosts(filePath):
    dataLocal = pd.read_csv(filePath)
    return dataLocal['Text'].tolist(), dataLocal['Sentiment'].tolist()

# Function to preprocess the data
def preprocess_data(posts):
    # Combine texts from array into one string
    text = ' '.join(posts)
    # The unique characters in the text
    vocab = sorted(set(text))
    # Creating a mapping from unique characters to indices
    char2idx = {u: i for i, u in enumerate(vocab)}
    idx2char = np.array(vocab)
    # Convert text to numerical representation
    text_as_int = np.array([char2idx[c] for c in text])
    return text, text_as_int, vocab, char2idx, idx2char

# Function to split input and target text for each sequence
def split_input_target(chunk):
    # Input is all but the last character
    input_text = chunk[:-1]
    # Target is all but the first character
    target_text = chunk[1:]
    return input_text, target_text

# Function to create a dataset from the text
def create_dataset(text_as_int, seq_length):
    # Create training examples / targets (input and target are the same, but shifted one character)
    char_dataset = tf.data.Dataset.from_tensor_slices(text_as_int)
    sequences = char_dataset.batch(seq_length + 1, drop_remainder=True)
    # Split input and target text for each sequence
    dataset = sequences.map(split_input_target)
    return dataset

# Function to build the model
def build_model(vocab_size, embedding_dim, rnn_units, batch_size):
    model = tf.keras.Sequential([
        # Embedding layer converts character indices to dense vectors of a fixed embedding size
        tf.keras.layers.Embedding(vocab_size, embedding_dim, batch_input_shape=[batch_size, None]),
        # GRU layer with rnn_units number of units, used for stateful=True to preserve state between batches
        tf.keras.layers.GRU(rnn_units, return_sequences=True, stateful=True, recurrent_initializer='glorot_uniform'),
        # Dense layer with vocab_size number of units
        tf.keras.layers.Dense(vocab_size)
    ])
    return model

# Loss function
def loss(labels, logits):
    # Sparse categorical cross entropy loss function for integer labels (character indices)
    return tf.keras.losses.sparse_categorical_crossentropy(labels, logits, from_logits=True)

# Function to generate text using the trained model
def generate_text(model, start_string, temperature):
    # Evaluation step (generating text using the learned model)

    # Number of characters to generate
    num_generate = 100

    # Converting our start string to numbers (vectorizing)
    input_eval = [char2idx[s] for s in start_string]
    input_eval = tf.expand_dims(input_eval, 0)

    # Empty string to store our results
    text_generated = []

    # Here batch size == 1
    model.reset_states()
    for i in range(num_generate):
        predictions = model(input_eval)
        # Remove the batch dimension
        predictions = tf.squeeze(predictions, 0)

        # Using a categorical distribution to predict the character returned by the model
        predictions = predictions / temperature
        predicted_id = tf.random.categorical(predictions, num_samples=1)[-1, 0].numpy()

        # Pass the predicted character as the next input to the model
        input_eval = tf.expand_dims([predicted_id], 0)

        text_generated.append(idx2char[predicted_id])

    return start_string + ''.join(text_generated)

if __name__ == '__main__':
    # Read the posts and labels from the CSV file
    posts, labels = readPosts('inputs/reviews_mixed.csv')

    # Preprocess the data
    text, text_as_int, vocab, char2idx, idx2char = preprocess_data(posts)

    # Hyperparameters
    # seq_length = 100
    # embedding_dim = 256
    # rnn_units = 1024
    # batch_size = 10
    # epochs = 50
    #
    # # Create the dataset from the text and labels arrays and shuffle and batch it for training
    # # (drop_remainder=True to ensure all batches are the same size)
    # dataset = create_dataset(text_as_int, seq_length)
    # dataset = dataset.shuffle(10000).batch(batch_size, drop_remainder=True)
    #
    # # Length of the vocabulary in chars
    # vocab_size = len(vocab)
    #
    # # Build the model
    # model = build_model(vocab_size, embedding_dim, rnn_units, batch_size)
    # for input_example_batch, target_example_batch in dataset.take(1):
    #     example_batch_predictions = model(input_example_batch)
    #     print(example_batch_predictions.shape, "# (batch_size, sequence_length, vocab_size)")
    # model.summary()
    #
    # example_batch_loss = loss(target_example_batch, example_batch_predictions)
    #
    # # Compile the model with the loss function and Adam optimizer
    # # and save the loss and accuracy metrics during training
    # model.compile(optimizer='adam', loss=loss, metrics=['accuracy'])
    #
    #
    # # Directory where the checkpoints will be saved
    # checkpoint_dir = './training_checkpoints'
    #
    # # Callback to save checkpoints during training (save weights only) every epoch (overwrite previous checkpoint)
    # checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(filepath=os.path.join(checkpoint_dir, "ckpt_{epoch}"),save_weights_only=True)
    #
    # # Train the model
    # history = model.fit(dataset, epochs=epochs, callbacks=[checkpoint_callback])
    #
    # # Build a new model with batch_size=1 for text generation
    # model = build_model(vocab_size, embedding_dim, rnn_units, batch_size=1)
    #
    # # Load the weights from the latest checkpoint
    # model.load_weights(tf.train.latest_checkpoint(checkpoint_dir))
    #
    # model.build(tf.TensorShape([1, None]))
    #
    # model.save('model_for_generating_text.h5')

    # get the model from the file
    model = tf.keras.models.load_model('model_for_generating_text.h5')

    # Generate text using the trained model
    start_string = "Very comfortable "
    temperature = 0.3
    generated_text = generate_text(model, start_string, temperature)
    print("Generated Text:", generated_text)

    # Save the generated text to a file
    with open('generated_text.txt', 'a') as f:
        f.write(generated_text)
        f.write('\n')

