package com.mlvd.model;

public final class LogisticRegression {
  private LogisticRegression() {
  }

  public static double sigmoid(double z) {
    return 1.0 / (1.0 + Math.exp(-z));
  }

  public static double predictProba(double[] weights, double bias, double[] x) {
    double z = bias;
    for (int i = 0; i < x.length; i += 1) {
      z += weights[i] * x[i];
    }
    return sigmoid(z);
  }

  public static TrainedModel train(double[][] x, int[] y, double learningRate, int epochs) {
    int nSamples = x.length;
    int nFeatures = x[0].length;

    double[] weights = new double[nFeatures];
    double bias = 0.0;

    for (int e = 0; e < epochs; e += 1) {
      double[] gradW = new double[nFeatures];
      double gradB = 0.0;

      for (int i = 0; i < nSamples; i += 1) {
        double p = predictProba(weights, bias, x[i]);
        double err = p - y[i];

        for (int j = 0; j < nFeatures; j += 1) {
          gradW[j] += err * x[i][j];
        }
        gradB += err;
      }

      for (int j = 0; j < nFeatures; j += 1) {
        weights[j] -= (learningRate * gradW[j]) / nSamples;
      }
      bias -= (learningRate * gradB) / nSamples;
    }

    return new TrainedModel(weights, bias);
  }

  public record TrainedModel(double[] weights, double bias) {
  }
}
