# Artificial Intelligence - Laboratory Work Collection

This repository contains a comprehensive collection of Artificial Intelligence laboratory work covering fundamental and advanced topics in AI, machine learning, deep learning, and optimization algorithms. Each laboratory includes detailed implementations, documentation, and practical examples.

## 📚 Table of Contents

- [Laboratory 1 - Data Structures and Algorithms](#laboratory-1---data-structures-and-algorithms)
- [Laboratory 2 - Optimal Subgroup Identification](#laboratory-2---optimal-subgroup-identification)
- [Laboratory 3 - Evolutionary Algorithms for Community Detection](#laboratory-3---evolutionary-algorithms-for-community-detection)
- [Laboratory 4 - Shortest Route Finder (TSP)](#laboratory-4---shortest-route-finder-tsp)
- [Laboratory 4 Optional - Ant Colony Optimization](#laboratory-4-optional---ant-colony-optimization)
- [Laboratory 5 - Machine Learning Evaluation Methods](#laboratory-5---machine-learning-evaluation-methods)
- [Laboratory 6 - Linear Regression (Least Squares)](#laboratory-6---linear-regression-least-squares)
- [Laboratory 7 - Gradient Descent Regression](#laboratory-7---gradient-descent-regression)
- [Laboratory 8 - Logistic Regression Classification](#laboratory-8---logistic-regression-classification)
- [Laboratory 9 - Artificial Neural Networks (ANN)](#laboratory-9---artificial-neural-networks-ann)
- [Laboratory 10 - K-Means Clustering](#laboratory-10---k-means-clustering)
- [Laboratory 11 - Emotion Classification](#laboratory-11---emotion-classification)
- [Laboratory 12 - Recurrent Neural Networks (RNN)](#laboratory-12---recurrent-neural-networks-rnn)

## 🔬 Laboratories Overview

### Laboratory 1 - Data Structures and Algorithms

**Topics:** Fundamental problem-solving using data structures and algorithms

**Key Implementations:**
- Alphabetical word sorting
- Euclidean distance calculation
- Sparse vector operations
- Text processing and analysis
- Array manipulation algorithms

[📖 View Details](./Laboratory%201/README.md)

---

### Laboratory 2 - Optimal Subgroup Identification

**Topics:** Community detection in networks, graph algorithms

**Key Features:**
- Processes `.gml` graph files
- Identifies optimal subgroups within communities
- Generates visualizations using matplotlib
- Analyzes various network structures (actors, musicians, painters, writers)

**Technologies:** NetworkX, Matplotlib, Python

[📖 View Details](./Laboratory%202/README.md)

---

### Laboratory 3 - Evolutionary Algorithms for Community Detection

**Topics:** Genetic algorithms, community detection, optimization

**Key Implementations:**
- Custom Chromosome class for genetic representation
- GeneticAlgorithm framework with selection, crossover, and mutation
- Multiple fitness functions including modularity
- Support for various graph datasets (lesmis.gml, netscience.gml)

**Highlights:**
- Elitism-based population evolution
- Custom fitness calculation for ideal community division
- Comprehensive visualization of community structures

[📖 View Details](./Laboratory%203/README.md)

---

### Laboratory 4 - Shortest Route Finder (TSP)

**Topics:** Traveling Salesman Problem, genetic algorithms, optimization

**Key Features:**
- Solves TSP using genetic algorithms
- Multiple crossover strategies (simple, complex, large-scale)
- Handles various difficulty levels (easy, medium, hard)
- Support for standard TSP file format

**Datasets:**
- `easy.txt`, `medium.txt` - Small to medium networks
- `mona-lisa100K.tsp` - Large-scale optimization problem

[📖 View Details](./Laboratory%204/README.md)

---

### Laboratory 4 Optional - Ant Colony Optimization

**Topics:** Ant Colony Optimization (ACO), swarm intelligence

**Key Implementations:**
- Ant class with probability-based movement
- Pheromone trail management
- BestAntTour optimization framework
- Dynamic and static graph support

**Datasets:**
- Static graphs: `test1.txt`, `test2.txt`
- Dynamic graphs: `aves-sparrow-social.edges`, `insecta-ant-colony1.edges`

[📖 View Details](./Laboratory%204%20Optional/README.md)

---

### Laboratory 5 - Machine Learning Evaluation Methods

**Topics:** Model evaluation, regression metrics, classification metrics

**Key Features:**
- Multi-target regression with RMSE evaluation
- Multi-class classification with accuracy, precision, and recall
- Cross-entropy loss for binary and multi-class problems
- Multi-label cross-entropy implementation

**Input Datasets:**
- `sport.csv` - Regression problem
- `flowers.csv` - Multi-class classification
- Probability files for various classification scenarios

[📖 View Details](./Laboratory%205/README.md)

---

### Laboratory 6 - Linear Regression (Least Squares)

**Topics:** Linear regression, least squares method, prediction models

**Key Implementations:**
- `MyLinearUnivariateRegression` - Single variable regression
- `MyLinearBivariateRegression` - Two variable regression
- Custom implementation without sklearn dependency
- 3D visualization for bivariate models

**Use Case:**
- World Happiness Report 2017 analysis
- GDP-based happiness prediction
- Freedom and GDP multi-factor analysis

[📖 View Details](./Laboratory%206/README.md)

---

### Laboratory 7 - Gradient Descent Regression

**Topics:** Stochastic Gradient Descent (SGD), Batch Gradient Descent (BGD)

**Key Implementations:**
- `MySGDRegression` - Stochastic gradient descent
- `BatchGDRegression` - Batch gradient descent
- `MultiTargetRegressor` - Multiple output variables
- Feature normalization and standardization

**Modules:**
- Learning methods (custom and sklearn-based)
- Statistical normalization
- 3D plotting for visualization

[📖 View Details](./Laboratory%207/README.md)

---

### Laboratory 8 - Logistic Regression Classification

**Topics:** Logistic regression, multi-class classification

**Key Implementations:**
- `OneLogisticRegression` - Single class classification
- `MyLogisticRegression` - Multi-class classification
- Custom implementation with SGD optimization

**Use Case:**
- Iris dataset classification
- Multi-class flower species identification
- Confusion matrix generation
- Performance metrics: accuracy, precision, recall

[📖 View Details](./Laboratory%208/README.md)

---

### Laboratory 9 - Artificial Neural Networks (ANN)

**Topics:** Neural networks, deep learning, image classification

**Key Implementations:**
- Custom ANN class with forward/backward propagation
- Iris classification (3 categories)
- Digit recognition (10 classes)
- Image filter detection (sepia vs. original)

**Architectures:**
- MLPClassifier (sklearn) - Multi-layer perceptron
- Sequential CNN (TensorFlow/Keras) - Convolutional neural network

**Datasets:**
- Iris dataset (2 features, 3 classes)
- Digits dataset (64 features, 10 classes)
- Paris photographs (56 images with/without sepia filter)

[📖 View Details](./Laboratory%209/README.md)

---

### Laboratory 10 - K-Means Clustering

**Topics:** Unsupervised learning, clustering, text classification

**Key Implementations:**
- Custom `KMeansClustering` algorithm
- Multiple feature extraction methods:
  - Bag of Words
  - Bag of Words with 2-grams
  - TF-IDF
  - Word2Vec

**Machine Learning Approaches:**
- **Unsupervised:** K-means clustering
- **Supervised:** SGD, SVM, Decision Tree classifiers
- **Hybrid:** Semi-supervised learning (MLPClassifier + K-means)

**Use Case:**
- Text emotion classification (positive/negative reviews)
- Review sentiment analysis

[📖 View Details](./Laboratory%2010/README.md)

---

### Laboratory 11 - Emotion Classification

**Topics:** Deep learning, transfer learning, emotion detection, CNN

**Key Components:**

1. **Emoji Emotion Classification**
   - Dataset: Happy and sad emoji images
   - Architecture: CNN (Convolutional Neural Network)
   - Training/testing split with image preprocessing

2. **Real Face Emotion Classification (Pre-trained)**
   - Model: `model_v6_23.hdf5`
   - Emotions: Anger, Disgust, Fear, Happiness, Neutral, Sadness, Surprise
   - Confusion matrix evaluation

3. **Real Face Emotion Classification (Trained from Scratch)**
   - **Manual features:** Histogram of Oriented Gradients (HOG)
   - **Automatic features:** Facenet embeddings
   - Custom ImageClassifier implementations

4. **Multi-label Image Classification**
   - Framework: PyTorch Lightning
   - GPU acceleration support (NVIDIA GeForce RTX 3070)
   - Labels: motorcycle, truck, boat, bus, cycle, sitar, ektara, flutes, tabla, harmonium

[📖 View Details](./Laboratory%2011/README.md)

---

### Laboratory 12 - Recurrent Neural Networks (RNN)

**Topics:** RNN, LSTM, GRU, text generation, DCGAN

**Key Algorithms:**

1. **Algo1 - Sentiment Prediction**
   - `alg1.py`: LSTM-based text sentiment prediction
   - `alg1img.py`: Image sentiment prediction (angry/happy)
   - Dataset: `reviews_mixed.csv`, angry/happy image dataset

2. **Algo2 - Text Generation**
   - Architecture: Embedding + GRU + Dense layers
   - Training: 50 epochs on TensorFlow
   - Temperature-based generation (low=probable, high=random)
   - Output: `generated_text.txt`
   - Model: `model_for_generating_text.h5`

3. **Algo3 - Image Generation (DCGAN)**
   - Architecture: Deep Convolutional GAN
   - Generator and Discriminator networks
   - Dataset: 50,000 celebrity photographs
   - Output: 64x64 generated images
   - Training: 5 epochs on NVIDIA GeForce RTX 3070
   - Visualization: TensorBoard logs
   - Reference: [DCGAN Paper by Radford & Metz](https://arxiv.org/pdf/1511.06434.pdf)

[📖 View Details](./Laboratory%2012/README.md)

---

## 🛠️ Technologies and Tools

### Programming Languages
- **Python** - Primary language for all implementations

### Machine Learning & Deep Learning Frameworks
- **TensorFlow** / **Keras** - Deep learning models
- **PyTorch** / **PyTorch Lightning** - Neural network training
- **scikit-learn** - Classical ML algorithms
- **NetworkX** - Graph algorithms and analysis

### Computer Vision & Image Processing
- **OpenCV (cv2)** - Image preprocessing
- **PIL** - Image manipulation
- **HOG** - Feature extraction
- **Facenet** - Face embeddings

### Natural Language Processing
- **Tokenizer** (Keras) - Text preprocessing
- **TF-IDF** - Text vectorization
- **Word2Vec** - Word embeddings

### Data Processing & Visualization
- **Pandas** - Data manipulation
- **NumPy** - Numerical computations
- **Matplotlib** - Data visualization
- **TensorBoard** - Training visualization

### Optimization & Evolutionary Algorithms
- Custom genetic algorithm implementations
- Ant Colony Optimization
- Gradient Descent (SGD, BGD)

## 📊 Datasets Used

- **UCI Iris Dataset** - Classification
- **Digits Dataset** - Handwritten digit recognition
- **World Happiness Report 2017** - Regression analysis
- **Celebrity Faces Dataset** - GAN training (50,000 images)
- **Emoji Dataset** - Emotion classification
- **Facial Emotions Dataset** - 7 emotion categories
- **Review Datasets** - Sentiment analysis
- **Graph Datasets** - Community detection (GML format)
- **TSP Datasets** - Route optimization

## 🚀 Getting Started

### Prerequisites

```bash
pip install tensorflow keras pytorch torchvision scikit-learn
pip install opencv-python pandas numpy matplotlib networkx
pip install pytorch-lightning
```

### GPU Support (Optional but Recommended)

For laboratories 11 and 12, GPU acceleration significantly improves training speed:
- NVIDIA GPU with CUDA support
- TensorFlow-GPU or PyTorch with CUDA

## 📝 Repository Structure

Each laboratory contains:
- `README.md` - Detailed documentation
- `AI-lab##.md` - Additional problem specifications
- Source code organized in subdirectories
- Input/output data files
- Visualization scripts

## 🎯 Learning Outcomes

By completing these laboratories, you will gain practical experience in:

1. **Classical AI & Optimization**
   - Genetic algorithms and evolutionary computation
   - Swarm intelligence (ACO)
   - Graph algorithms and community detection

2. **Machine Learning Fundamentals**
   - Regression (linear, gradient descent)
   - Classification (logistic regression, SVM, decision trees)
   - Clustering (k-means, hierarchical)
   - Model evaluation and validation

3. **Deep Learning**
   - Artificial Neural Networks (ANN)
   - Convolutional Neural Networks (CNN)
   - Recurrent Neural Networks (RNN, LSTM, GRU)
   - Generative Adversarial Networks (GAN)

4. **Computer Vision**
   - Image preprocessing and feature extraction
   - Emotion detection in faces and emojis
   - Multi-label image classification
   - Image generation with GANs

5. **Natural Language Processing**
   - Text classification and sentiment analysis
   - Feature extraction (TF-IDF, Word2Vec)
   - Text generation with RNNs

## 📄 License

This is an educational repository containing university laboratory work. All implementations are for learning purposes.

## 👤 Author

Martin Fabian Ionut
- GitHub: [@MartinFabianIonut](https://github.com/MartinFabianIonut)

## 🙏 Acknowledgments

- University course materials and instructors
- Research papers referenced in individual laboratories
- Open-source communities for TensorFlow, PyTorch, and scikit-learn
- Network Data Repository (for graph datasets)

---

**Note:** Each laboratory folder contains detailed README files with specific implementation details, usage instructions, and results. Please refer to individual laboratory documentation for more information.
