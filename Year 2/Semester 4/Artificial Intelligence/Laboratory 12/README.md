## Table of Contents

- [alg1](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2012/RNN/alg1.py)
- [alg1img](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2012/RNN/alg1img.py)
- [alg2](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2012/RNN/alg2.py)
- [alg3](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%2012/RNN/alg3.py)

## Algo1

Algo1 is an algorithm that predicts whether a user is likely to post a negative or positive message and/or image based on their previous posts.

### Solution

The solution for Algo1 can be found in the following files:

- `alg1.py`: This file predicts the sentiment based on previous posts. It uses a Long Short-Term Memory (LSTM) model and takes input data from `reviews_mixed.csv` and `reviews_mixed2.csv`. The negative reviews were mostly removed for training and testing purposes.

- `alg1img.py`: This file uses images from the previous lab, consisting of 450 angry and happy images. To obtain diverse predictions, different proportions of images were used (20% - 80%, 80% - 20%, and 50% - 50%). The probabilities of future images were set with lower human intuition.

## Algo2

Algo2 is an algorithm that generates a synthetic message containing the most probable text to be posted.

### Solution

The solution for Algo2 can be found in the following file:

- `alg2.py`: This file uses the vocabulary from `reviews_mixed.csv`. The model, trained with TensorFlow, consists of Embedding, GRU, and Dense layers. The training is performed for 50 epochs, and the trained model is saved as `model_for_generating_text.h5`. In subsequent runs, the saved model is loaded to generate new text. The generation process involves providing a starting string (the initial words of the generated phrase) and setting a temperature (lower values generate more probable text, while higher values introduce more randomness). The generated texts are saved in `generated_text.txt`.

## Algo3

Algo3 is an algorithm that generates an image.

### Solution

The solution for Algo3 can be found in the following file:

- `alg3.py`: This file implements a Deep Convolutional Generative Adversarial Network (DCGAN) with a Discriminator and Generator, following the guidelines presented in the paper [UNSUPERVISED REPRESENTATION LEARNING WITH DEEP CONVOLUTIONAL GENERATIVE ADVERSARIAL NETWORKS](https://arxiv.org/pdf/1511.06434.pdf), by Alec Radford & Luke Metz. The algorithm generates 64x64 images, and the Discriminator determines if an image is real or fake. The dataset used consists of 50,000 photographs of celebrities. The results are saved as log files, and TensorBoard can be used for better visualization (`tensorboard --logdir logs`). The training was successfully performed for 5 epochs using an NVIDIA® GeForce RTX™ 3070 GPU with 8GB GDDR6 memory.
