from LogisticRegression import MyLogisticRegression


def learnModel(trainInputs, trainOutputs, classes):
    # model initialisation
    # # using sklearn
    # from sklearn import linear_model
    # classifier = linear_model.LogisticRegression()

    # using developed code
    # from LogisticRegression import MyLogisticRegression
    # model initialisation
    classifier = MyLogisticRegression()

    # train the classifier (fit in on the training data)
    classifier.fit(trainInputs, trainOutputs, classes)

    # classifier.fit(trainInputs, trainOutputs)

    # parameters of the liniar regressor
    w0, w1, w2 = classifier.intercept_[0], classifier.coef_[0][0], classifier.coef_[0][1]
    print('classification model: y(feat1, feat2) = ', w0, ' + ', w1, ' * feat1 + ', w2, ' * feat2')
    return classifier
