from sklearn.feature_extraction.text import CountVectorizer
import numpy as np


def splitData(inputs, outputs):
    # split data into train and test subsets
    np.random.seed(5)
    indexes = [i for i in range(len(inputs))]
    trainSample = np.random.choice(indexes, int(0.8 * len(inputs)), replace=False)
    testSample = [i for i in indexes if not i in trainSample]

    trainInputs = [inputs[i] for i in trainSample]
    trainOutputs = [outputs[i] for i in trainSample]
    testInputs = [inputs[i] for i in testSample]
    testOutputs = [outputs[i] for i in testSample]

    return trainInputs, trainOutputs, testInputs, testOutputs


def extractCharacteristics(trainInputs, testInputs):
    # # representation 1: Bag of Words
    vectorizer = CountVectorizer()
    trainFeatures = vectorizer.fit_transform(trainInputs)
    testFeatures = vectorizer.transform(testInputs)

    # # representation 2: Bag of Words with 2-grams
    # vectorizer = CountVectorizer(ngram_range=(2, 2))
    # trainFeatures = vectorizer.fit_transform(trainInputs)
    # testFeatures = vectorizer.transform(testInputs)

    # # representation 3: TF-IDF representation
    # from sklearn.feature_extraction.text import TfidfVectorizer
    # vectorizer = TfidfVectorizer()
    # trainFeatures = vectorizer.fit_transform(trainInputs)
    # testFeatures = vectorizer.transform(testInputs)

    # representation 4: Doc2Vec representation - other characteristics
    # import nltk
    #
    # from nltk.tokenize import word_tokenize
    # from gensim.models.doc2vec import Doc2Vec, TaggedDocument
    # taggedTrainInputs = [TaggedDocument(words=word_tokenize(_d.lower()), tags=[str(i)]) for i, _d in
    #                      enumerate(trainInputs)]
    # taggedTestInputs = [TaggedDocument(words=word_tokenize(_d.lower()), tags=[str(i)]) for i, _d in
    #                     enumerate(testInputs)]
    # max_epochs = 50
    # vec_size = 20
    # alpha = 0.025
    #
    # model = Doc2Vec(
    #                 alpha=alpha,
    #                 min_alpha=0.00025,
    #                 min_count=1,
    #                 dm=1)
    #
    # model.build_vocab(taggedTrainInputs)
    #
    # for epoch in range(max_epochs):
    #     model.train(taggedTrainInputs,
    #                 total_examples=model.corpus_count,
    #                 epochs=50)
    #     # decrease the learning rate
    #     model.alpha -= 0.0002
    #     # fix the learning rate, no decay
    #     model.min_alpha = model.alpha
    #
    # trainFeatures = [model.infer_vector(doc.words) for doc in taggedTrainInputs]
    # testFeatures = [model.infer_vector(doc.words) for doc in taggedTestInputs]

    return trainFeatures, testFeatures