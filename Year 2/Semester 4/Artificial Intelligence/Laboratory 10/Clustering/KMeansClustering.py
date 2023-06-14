import numpy as np


class KMeansClustering:
    def __init__(self, k=2, tol=0.001, max_iter=800):
        """
        K-means clustering initialization.
        Parameters:
            k (int): Number of clusters.
            tol (float): Tolerance for convergence.
            max_iter (int): Maximum number of iterations.
        """
        self.centroids = {}
        self.classifications = {}
        self.k = k
        self.tol = tol
        self.max_iter = max_iter

    def fit(self, data):
        """
        Fit the K-means clustering algorithm to the given data.
        data (array-like): Input data.
        """
        for i in range(self.k):
            # initialize the centroids with the first k data points
            self.centroids[i] = data[i].copy()

        for _ in range(self.max_iter):
            # an empty dictionary to store the data points for each cluster
            for i in range(self.k):
                self.classifications[i] = []

            # assign each data point to the closest cluster
            for featureSet in data:
                distances = [np.linalg.norm(featureSet - self.centroids[centroid]) for centroid in self.centroids]
                # find the index of the closest cluster
                classification = distances.index(min(distances))
                # add the data point to the cluster
                self.classifications[classification].append(featureSet)

            for classification in self.classifications:
                # update the centroids by taking the average of the data points in each cluster
                self.centroids[classification] = np.average(self.classifications[classification], axis=0)

            optimized = True

            # check if the centroids have moved more than the tolerance
            for featureSet in data:
                # find the index of the closest cluster
                distances = [np.linalg.norm(featureSet - self.centroids[centroid]) for centroid in self.centroids]
                classification = distances.index(min(distances))
                try:
                    # check if the data point is in the cluster
                    self.classifications[classification].index(featureSet)
                except ValueError:
                    # if the data point is not in the cluster, the algorithm has not converged,
                    #  means that the data points in each cluster do not change
                    optimized = False
                    break

            if optimized:
                break

    def predict_one(self, data):
        """
        Predict the cluster label for a single data point.
        """
        distances = [np.linalg.norm(data - self.centroids[centroid]) for centroid in self.centroids]
        classification = distances.index(min(distances))
        return classification

    def predict(self, data):
        """
        Predict the cluster labels for a set of data points.
        """
        # for each data point, find the closest cluster
        return [self.predict_one(el) for el in data]
